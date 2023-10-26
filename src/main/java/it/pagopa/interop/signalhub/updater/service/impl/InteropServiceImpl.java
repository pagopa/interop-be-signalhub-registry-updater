package it.pagopa.interop.signalhub.updater.service.impl;


import it.pagopa.interop.signalhub.updater.exception.PDNDClientException;
import it.pagopa.interop.signalhub.updater.exception.PDNDConnectionResetException;
import it.pagopa.interop.signalhub.updater.exception.PDNDNoEventsException;
import it.pagopa.interop.signalhub.updater.externalclient.InteroperabilityClient;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.*;
import it.pagopa.interop.signalhub.updater.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.updater.mapper.EServiceDescriptorMapper;
import it.pagopa.interop.signalhub.updater.mapper.OrganizationEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.*;
import it.pagopa.interop.signalhub.updater.service.InteropService;
import it.pagopa.interop.signalhub.updater.utility.Predicates;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

import static it.pagopa.interop.signalhub.updater.utility.Const.*;


@Slf4j
@Service
@AllArgsConstructor
public class InteropServiceImpl implements InteropService {
    private final InteroperabilityClient client;
    private final ConsumerEServiceMapper mapperConsumer;
    private final OrganizationEServiceMapper mapperOrganization;
    private final EServiceDescriptorMapper eServiceDescriptorMapper;


    @Override
    public EventsDto getAgreementsAndEServices(Long lastEventId) {
        Events response = null;
        try {
            log.info("Rerieving events from {} eventId", lastEventId);
            response = client.getEventsFromId(lastEventId);
        } catch (WebClientRequestException ex) {
            throw new PDNDConnectionResetException("Connection token was expired", lastEventId);
        } catch (WebClientResponseException ex) {
            throw new PDNDClientException("Error with retrieve events", lastEventId);
        }

        if (response == null || response.getEvents().isEmpty()){
            log.info("Events list is empty");
            throw new PDNDNoEventsException("No events from last event id ".concat(lastEventId.toString()));
        }

        log.info("Total events retrieved {}", response.getEvents().size());
        Set<EventDto> events = response.getEvents()
                                    .parallelStream()
                                    .filter(Predicates.isAgreementOrEServiceEvent())
                                    .map(this::toEventDto)
                                    .collect(Collectors.toSet());

        log.info("Total events filtered {}", events.size());
        EventsDto dto = new EventsDto();
        dto.setEvents(
                events.stream()
                        .sorted(Comparator.comparingInt(event -> event.getEventId().intValue()))
                        .toList()
        );
        dto.setLastEventId(response.getLastEventId());
        return dto;
    }

    @Override
    public ConsumerEServiceDto getConsumerEService(String agreementId, Long eventId) {
        try {
            Agreement agreement = client.getAgreement(agreementId);
            log.info("[{} - {}] Retrieving detail agreement", eventId, agreementId);
            return mapperConsumer.toConsumerEServiceDtoFromAgreement(agreement, eventId);
        } catch (WebClientRequestException ex) {
            throw new PDNDConnectionResetException("Connection token was expired", eventId);
        } catch (WebClientResponseException ex) {
            log.error("[{} - {}] Error with retrieving Agreement detail", eventId, agreementId);
            throw new PDNDClientException("Error with retrieve agreement details", eventId);
        }
    }

    @Override
    public OrganizationEServiceDto getEService(String eserviceId, Long eventId) {
        try {
            EService eService = client.getEService(eserviceId);
            log.info("[{} - {}] Retrieving detail eservice", eventId, eserviceId);
            return mapperOrganization.fromEServiceToOrganizationEServiceDto(eService, eventId);
        }  catch (WebClientRequestException ex) {
            throw new PDNDConnectionResetException("Connection token was expired", eventId);
        } catch (WebClientResponseException ex) {
            log.error("[{} - {}] Error with retrieving Eservice detail", eventId, eserviceId);
            throw new PDNDClientException("Error with retrieve eservice details", eventId);
        }
    }

    @Override
    public EServiceDescriptorDto getEServiceDescriptor(String eserviceId, Long eventId, String descriptorId) {
        try {
            EServiceDescriptor eServiceDescriptor = client.getEServiceDescriptor(eserviceId, descriptorId);
            log.info("[{} - {}] Retrieving detail eservice descriptor", eventId, eserviceId);
            return eServiceDescriptorMapper.fromEServiceDescriptorToEServiceDescriptorDto(eServiceDescriptor);
        }  catch (WebClientRequestException ex) {
            throw new PDNDConnectionResetException("Connection token was expired", eventId);
        } catch (WebClientResponseException ex) {
            log.error("[{} - {}] Error with retrieving Eservice detail", eventId, eserviceId);
            throw new PDNDClientException("Error with retrieve eservice descriptor details", eventId);
        }
    }

    private EventDto toEventDto(Event event){
        if (event.getObjectType().equals(ESERVICE_EVENT)){
            EServiceEventDto dto = new EServiceEventDto();
            dto.setEventId(event.getEventId());
            dto.setEServiceId(event.getObjectId().get(ESERVICE_KEY_ID));
            dto.setEventType(event.getEventType());
            dto.setObjectType(event.getObjectType());
            dto.setDescriptorId(event.getObjectId().get(DESCRIPTOR_ID));
            return dto;
        }
        AgreementEventDto dto = new AgreementEventDto();
        dto.setEventId(event.getEventId());
        dto.setAgreementId(event.getObjectId().get(AGREEMENT_KEY_ID));
        dto.setEventType(event.getEventType());
        dto.setObjectType(event.getObjectType());
        return dto;
    }
}
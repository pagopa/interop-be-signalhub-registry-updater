package it.pagopa.interop.signalhub.updater.service.impl;


import it.pagopa.interop.signalhub.updater.exception.PDNDEventException;
import it.pagopa.interop.signalhub.updater.exception.PDNDConnectionResetException;
import it.pagopa.interop.signalhub.updater.exception.PDNDNoEventsException;
import it.pagopa.interop.signalhub.updater.externalclient.InteroperabilityClient;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.*;
import it.pagopa.interop.signalhub.updater.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.updater.mapper.OrganizationEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.*;
import it.pagopa.interop.signalhub.updater.service.InteropService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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


    @Override
    public EventsDto getEventsByType(Long lastEventId, String type) {
        Events response = null;
        try {
            log.info("Rerieving events from {} eventId", lastEventId);
            response = client.getEventsFromIdAndType(lastEventId, type);
        } catch (WebClientRequestException ex) {
            throw new PDNDConnectionResetException("Connection token was expired {}", lastEventId + 1);
        } catch (WebClientResponseException ex) {
            throw new PDNDEventException("Error with retrieve events", lastEventId);
        }

        if (response == null || response.getEvents().isEmpty()){
            log.info("Events list is empty");
            throw new PDNDNoEventsException("No events from last event id ".concat(lastEventId.toString()));
        }

        log.info("Total events retrieved {}", response.getEvents().size());
        Set<EventDto> events = response.getEvents()
                                    .parallelStream()
                                    .map(this::toEventDto)
                                    .filter(event -> StringUtils.isNotBlank(event.getDescriptorId()))
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
            return mapperConsumer.toConsumerEServiceDtoFromAgreement(agreement, agreementId, eventId);
        } catch (WebClientRequestException ex) {
            throw new PDNDConnectionResetException("Connection token was expired", eventId);
        } catch (WebClientResponseException ex) {
            log.error("[{} - {}] Error with retrieving Agreement detail", eventId, agreementId);
            throw new PDNDEventException("Error with retrieve agreement details", eventId);
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
            throw new PDNDEventException("Error with retrieve eservice details", eventId);
        }
    }

    @Override
    public OrganizationEServiceDto getEServiceDescriptor(OrganizationEServiceDto eServiceDto) {
        try {
            EServiceDescriptor eServiceDescriptor = client.getEServiceDescriptor(eServiceDto.getEserviceId(), eServiceDto.getDescriptorId());
            log.info("[{} - {} - {}] Retrieved detail eservice descriptor with state = {}", eServiceDto.getEventId(), eServiceDto.getEserviceId(), eServiceDto.getDescriptorId(), eServiceDescriptor.getState());
            eServiceDto.setState(eServiceDescriptor.getState().getValue());
            return eServiceDto;
        }  catch (WebClientRequestException ex) {
            throw new PDNDConnectionResetException("Connection token was expired", eServiceDto.getEventId());
        } catch (WebClientResponseException ex) {
            log.error("[{} - {} - {}] Error with retrieving Eservice detail", eServiceDto.getEventId(), eServiceDto.getEserviceId(), eServiceDto.getDescriptorId());
            throw new PDNDEventException("Error with retrieve eservice descriptor details", eServiceDto.getEventId());
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
package it.pagopa.interop.signalhub.updater.service.impl;


import it.pagopa.interop.signalhub.updater.externalclient.InteroperabilityClient;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Event;
import it.pagopa.interop.signalhub.updater.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.updater.mapper.OrganizationEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.*;
import it.pagopa.interop.signalhub.updater.service.InteropService;
import it.pagopa.interop.signalhub.updater.utility.Predicates;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.logging.Level;

import static it.pagopa.interop.signalhub.updater.utility.Const.*;


@Slf4j
@Service
@AllArgsConstructor
public class InteropServiceImpl implements InteropService {
    private final InteroperabilityClient client;
    private final ConsumerEServiceMapper mapperConsumer;
    private final OrganizationEServiceMapper mapperOrganization;

    @Override
    public Mono<EventsDto> getAgreementsAndEservices(Long lastEventId) {
        return client.getEventsFromId(lastEventId)
                .filter(Predicates.isEventsNotEmpty())
                .flatMap(events -> Flux.fromIterable(events.getEvents())
                                            .filter(Predicates.isAgreementOrEServiceEvent())
                                            .map(this::toEventDto)
                                            .distinct()
                                            //.log();
                                            .collectList()
                                            .map(listWithoutDuplicate -> {
                                                EventsDto dto = new EventsDto();
                                                dto.setEvents(listWithoutDuplicate);
                                                dto.setLastEventId(events.getLastEventId());
                                                return dto;
                                            })
                )
                .log(log.getName(), Level.INFO, SignalType.ON_COMPLETE);
    }

    @Override
    public Mono<ConsumerEServiceDto> getConsumerEservice(String agreementId, Long eventId) {
        return client.getAgreement(agreementId)
                .map(response -> mapperConsumer.toConsumerEServiceDtoFromAgreement(response, eventId));
    }

    @Override
    public Mono<OrganizationEServiceDto> getEservice(String eserviceId, Long eventId) {
        return client.getEService(eserviceId)
                .map(response -> mapperOrganization.fromEServiceToOrganizationEServiceDto(response, eventId));
    }

    private EventDto toEventDto(Event event){
        if (event.getObjectType().equals(ESERVICE_EVENT)){
            EServiceEventDTO dto = new EServiceEventDTO();
            dto.setEventId(event.getEventId());
            dto.setEServiceId(event.getObjectId().get(ESERVICE_KEY_ID));
            return dto;
        }
        AgreementEventDto dto = new AgreementEventDto();
        dto.setEventId(event.getEventId());
        dto.setAgreementId(event.getObjectId().get(AGREEMENT_KEY_ID));
        return dto;
    }

}

package it.pagopa.interop.signalhub.updater.service;

import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.model.EventDto;
import it.pagopa.interop.signalhub.updater.model.EventsDto;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InteropService {


    Mono<EventsDto> getAgreementsAndEservices(Long lastEventId);

    Mono<ConsumerEServiceDto> getConsumerEservice(String agreementId, Long eventId);

    Mono<OrganizationEServiceDto> getEservice(String eserviceId, Long eventId);


}

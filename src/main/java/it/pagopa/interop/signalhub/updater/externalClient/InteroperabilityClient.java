package it.pagopa.interop.signalhub.updater.externalClient;

import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.*;
import reactor.core.publisher.Mono;


public interface InteroperabilityClient {
    Mono<Events> getEventsFromId(Long lastEventId);
    Mono<Agreement> getAgreement(String agreementId);
    Mono<EService> getEService(String eserviceId);
}

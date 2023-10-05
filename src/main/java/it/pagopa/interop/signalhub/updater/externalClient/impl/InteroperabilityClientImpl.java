package it.pagopa.interop.signalhub.updater.externalClient.impl;

import it.pagopa.interop.signalhub.updater.externalClient.InteroperabilityClient;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.api.v1.GatewayApi;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Agreement;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.EService;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Events;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.UUID;


@Slf4j
@Component
@AllArgsConstructor
public class InteroperabilityClientImpl implements InteroperabilityClient {
    private GatewayApi gatewayApi;

    public static final Integer MAX_LIMIT_BLOCK = 100;

    @Override
    public Mono<Events> getEventsFromId(Long lastEventId) {
        return gatewayApi.getEventsFromId(lastEventId, MAX_LIMIT_BLOCK);
    }

    @Override
    public Mono<Agreement> getAgreement(String agreementId) {
        UUID uuidAgreementId = UUID.fromString(agreementId);
        return gatewayApi.getAgreement(uuidAgreementId);
    }

    @Override
    public Mono<EService> getEService(String eserviceId) {
        UUID uuidEserviceId = UUID.fromString(eserviceId);
        return gatewayApi.getEService(uuidEserviceId);
    }
}

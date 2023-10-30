package it.pagopa.interop.signalhub.updater.externalclient.impl;

import it.pagopa.interop.signalhub.updater.externalclient.InteroperabilityClient;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.api.v1.GatewayApi;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.net.ConnectException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

@Slf4j
@Component
@AllArgsConstructor
public class InteroperabilityClientImpl implements InteroperabilityClient {
    private GatewayApi gatewayApi;

    public static final Integer MAX_LIMIT_BLOCK = 500;


    @Override
    public Events getEventsFromId(Long lastEventId) {
        return gatewayApi.getEventsFromId(lastEventId, MAX_LIMIT_BLOCK)
                .retryWhen(
                        Retry.backoff(4, Duration.ofMillis(1000))
                                .filter(this.isRetryException())
                )
                .block();
    }

    @Override
    public Agreement getAgreement(String agreementId) {
        UUID uuidAgreementId = UUID.fromString(agreementId);
        return Mono.just(uuidAgreementId)
                .flatMap(item -> gatewayApi.getAgreement(uuidAgreementId)
                        .retryWhen(
                                Retry.backoff(4, Duration.ofMillis(1000))
                                        .filter(this.isRetryException())
                        )
                )
                .block();
    }

    @Override
    public EService getEService(String eserviceId) {
        UUID uuidEserviceId = UUID.fromString(eserviceId);
        return gatewayApi.getEService(uuidEserviceId)
                .retryWhen(
                        Retry.backoff(4, Duration.ofMillis(1000))
                                .filter(this.isRetryException())
                )
                .block();
    }

    @Override
    public EServiceDescriptor getEServiceDescriptor(String eserviceId, String descriptorId) {
        UUID uuidEserviceId = UUID.fromString(eserviceId);
        UUID uuidDescriptorId = UUID.fromString(descriptorId);
        return gatewayApi.getEServiceDescriptor(uuidEserviceId, uuidDescriptorId)
                .retryWhen(
                        Retry.backoff(4, Duration.ofMillis(1000))
                                .filter(this.isRetryException())
                )
                .block();
    }

    private Predicate<Throwable> isRetryException(){
        return ex -> {
            if (ex instanceof TimeoutException) return true;
            if (ex instanceof ConnectException) return true;
            if (ex instanceof WebClientResponseException exWeb ){
                return exWeb.getStatusCode() == HttpStatusCode.valueOf(429);
            }
            return false;
        };
    }

}

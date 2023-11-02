package it.pagopa.interop.signalhub.updater.externalclient;

import it.pagopa.interop.signalhub.updater.config.BaseTest;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.api.v1.GatewayApi;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.EService;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.EServiceDescriptorState;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class InteroperabilityClientImplTest extends BaseTest.WithMockServer {
    private static final String ESERVICE_BAD_REQUEST = "498457c9-f4de-40d2-ad84-aed23c3584fd";
    private static final String ESERVICE_TOO_MANY_REQUEST = "1925cc0d-b65c-4a78-beca-b990b933ecf3";
    private static final String ESERVICE_CONNECTION_TIMEOUT = "cd2b6f58-9a63-45fc-812d-195f05754ac1";
    private static final String ESERVICE_NO_CONNECT = "84871fd4-2fd7-46ab-9d22-f6b452f4b3c5";
    private static final String ESERVICE_OK = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
    @Autowired
    private InteroperabilityClient interoperabilityClient;
    @SpyBean
    private GatewayApi gatewayApi;


    @Test
    void whenRetrieveEserviceIdThenReturnEserviceDetail(){
        EService eService = interoperabilityClient.getEService(ESERVICE_OK);
        assertNotNull(eService);
        assertEquals(EServiceDescriptorState.PUBLISHED, eService.getState());
    }

    @Test
    void whenEserviceIDBadlyFormatThenThrowBadRequestException() {
        WebClientResponseException exception =
                assertThrows(WebClientResponseException.class, () -> interoperabilityClient.getEService(ESERVICE_BAD_REQUEST));

        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }


    @Test
    void whenClientReturn429ThenRetry4Attempts(){
        try {
            interoperabilityClient.getEService(ESERVICE_TOO_MANY_REQUEST);
        } catch (Throwable ex) {
            assertTrue(Exceptions.isRetryExhausted(ex));
            assertTrue(ex.getCause() instanceof WebClientResponseException);
            assertEquals(HttpStatus.TOO_MANY_REQUESTS, ((WebClientResponseException) ex.getCause()).getStatusCode());
        }
    }

    @Test
    void whenClientTimeoutThenRetry4Attempts(){
        Mockito.when(gatewayApi.getEService(UUID.fromString(ESERVICE_CONNECTION_TIMEOUT)))
                .thenReturn(Mono.error(new TimeoutException()));

        try {
            interoperabilityClient.getEService(ESERVICE_CONNECTION_TIMEOUT);
        } catch (Throwable ex) {
            assertTrue(Exceptions.isRetryExhausted(ex));
            assertTrue(ex.getCause() instanceof TimeoutException);
        }
    }

    @Test
    void whenClientThrowConnectionExceptionThenRetry4Attempts(){
        Mockito.when(gatewayApi.getEService(UUID.fromString(ESERVICE_NO_CONNECT)))
                .thenReturn(Mono.error(new ConnectException()));

        try {
            interoperabilityClient.getEService(ESERVICE_NO_CONNECT);
        } catch (Throwable ex) {
            assertTrue(Exceptions.isRetryExhausted(ex));
            assertTrue(ex.getCause() instanceof ConnectException);
        }
    }

//    @Test
    void getEServiceExceptionCaseTest() {
        String eserviceId = "498457c9-f4de-40d2-ad84-aed23c3584fd";
        assertThrows(WebClientResponseException.class, () -> interoperabilityClient.getEService(eserviceId));
    }
}

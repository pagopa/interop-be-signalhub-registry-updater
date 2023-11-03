package it.pagopa.interop.signalhub.updater.externalclient;

import it.pagopa.interop.signalhub.updater.config.BaseTest;
import it.pagopa.interop.signalhub.updater.externalclient.impl.InteroperabilityClientImpl;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.api.v1.GatewayApi;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.*;
import it.pagopa.interop.signalhub.updater.utility.Predicates;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import java.net.ConnectException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import static it.pagopa.interop.signalhub.updater.utility.Const.AGREEMENT_EVENT;
import static it.pagopa.interop.signalhub.updater.utility.Const.ESERVICE_EVENT;
import static org.junit.jupiter.api.Assertions.*;


class InteroperabilityClientImplTest extends BaseTest.WithMockServer {
    private static final Long EVENTSID_OK = 0L;
    private static final Long EVENTSID_OK_EMPTY = 10L;
    private static final Long EVENTSID_BAD_REQUEST = -1L;
    private static final Long EVENTSID_CONNECTION_TIMEOUT = -2L;
    private static final Long EVENTSID_NO_CONNECT = -3L;

    private static final String AGREEMENTID_BAD_REQUEST = "498457c9-f4de-40d2-ad84-aed23c3584fd";
    private static final String AGREEMENTID_TOO_MANY_REQUEST = "1925cc0d-b65c-4a78-beca-b990b933ecf3";
    private static final String AGREEMENTID_CONNECTION_TIMEOUT = "cd2b6f58-9a63-45fc-812d-195f05754ac1";
    private static final String AGREEMENTID_NO_CONNECT = "84871fd4-2fd7-46ab-9d22-f6b452f4b3c5";
    private static final String AGREEMENTID_OK = "9a7e5371-0832-4301-9d97-d762f703dd78";

    private static final String ESERVICEID_BAD_REQUEST = "498457c9-f4de-40d2-ad84-aed23c3584fd";
    private static final String ESERVICEID_TOO_MANY_REQUEST = "1925cc0d-b65c-4a78-beca-b990b933ecf3";
    private static final String ESERVICEID_CONNECTION_TIMEOUT = "cd2b6f58-9a63-45fc-812d-195f05754ac1";
    private static final String ESERVICEID_NO_CONNECT = "84871fd4-2fd7-46ab-9d22-f6b452f4b3c5";
    private static final String ESERVICEID_OK = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
    private static final String ESERVICEID_PRODUCERID_OK = "b50eb24c-bd08-44fd-ba6c-b8c003f29df6";

    private static final String ESERVICE_DESCRIPTOR_ESERVICEID_BAD_REQUEST = "498457c9-f4de-40d2-ad84-aed23c3584fd";
    private static final String ESERVICE_DESCRIPTOR_DESCRIPTORID_BAD_REQUEST = "9a7e5371-0832-4301-9d97-d762f703dd78";
    private static final String ESERVICE_DESCRIPTOR_ESERVICEID_TOO_MANY_REQUEST = "1925cc0d-b65c-4a78-beca-b990b933ecf3";
    private static final String ESERVICE_DESCRIPTOR_DESCRIPTORID_TOO_MANY_REQUEST = "cd2b6f58-9a63-45fc-812d-195f05754ac1";
    private static final String ESERVICE_DESCRIPTOR_ESERVICEID_CONNECTION_TIMEOUT = "cd2b6f58-9a63-45fc-812d-195f05754ac1";
    private static final String ESERVICE_DESCRIPTOR_DESCRIPTORID_CONNECTION_TIMEOUT = "1925cc0d-b65c-4a78-beca-b990b933ecf3";
    private static final String ESERVICE_DESCRIPTOR_ESERVICEID_NO_CONNECT = "84871fd4-2fd7-46ab-9d22-f6b452f4b3c5";
    private static final String ESERVICE_DESCRIPTOR_DESCRIPTORID_NO_CONNECT = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
    private static final String ESERVICE_DESCRIPTOR_ESERVICEID_OK = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
    private static final String ESERVICE_DESCRIPTOR_DESCRIPTORID_OK = "84871fd4-2fd7-46ab-9d22-f6b452f4b3c5";


    @Autowired
    private InteroperabilityClient interoperabilityClient;
    @SpyBean
    private GatewayApi gatewayApi;


    @Test
    void whenRetrieveEventsByLastEventIdThenReturnEventsList() {
        String toMatch = ESERVICE_EVENT.concat("|").concat(AGREEMENT_EVENT);

        Events events = interoperabilityClient.getEventsFromId(EVENTSID_OK);
        assertNotNull(events);

        List<Event> eventList = events.getEvents()
                .stream()
                .filter(Predicates.isAgreementOrEServiceEvent())
                .toList();

        eventList.forEach(event -> {
            assertTrue(event.getObjectType().matches(toMatch));
        });
    }

    @Test
    void whenRetrieveEventsByLastEventIdThenReturnEventsListEmpty() {
        Events events = interoperabilityClient.getEventsFromId(EVENTSID_OK_EMPTY);
        assertNotNull(events);

        List<Event> eventList = events.getEvents()
                .stream()
                .filter(Predicates.isAgreementOrEServiceEvent())
                .toList();

        assertEquals(0, eventList.size());
    }

    @Test
    void whenLastEventIdBadlyFormatThenThrowBadRequestException() {
        WebClientResponseException exception =
                assertThrows(WebClientResponseException.class, () -> interoperabilityClient.getEventsFromId(EVENTSID_BAD_REQUEST));

        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void whenClientCallLastEventIdTimeoutThenRetry4Attempts(){
        Mockito.when(gatewayApi.getEventsFromId(EVENTSID_CONNECTION_TIMEOUT, InteroperabilityClientImpl.MAX_LIMIT_BLOCK))
                .thenReturn(Mono.error(new TimeoutException()));

        try {
            interoperabilityClient.getEventsFromId(EVENTSID_CONNECTION_TIMEOUT);
        } catch (Throwable ex) {
            assertTrue(Exceptions.isRetryExhausted(ex));
            assertTrue(ex.getCause() instanceof TimeoutException);
        }
    }

    @Test
    void whenClientCallLastEventIdThrowConnectionExceptionThenRetry4Attempts(){
        Mockito.when(gatewayApi.getEventsFromId(EVENTSID_NO_CONNECT, InteroperabilityClientImpl.MAX_LIMIT_BLOCK))
                .thenReturn(Mono.error(new ConnectException()));

        try {
            interoperabilityClient.getEventsFromId(EVENTSID_NO_CONNECT);
        } catch (Throwable ex) {
            assertTrue(Exceptions.isRetryExhausted(ex));
            assertTrue(ex.getCause() instanceof ConnectException);
        }
    }


    @Test
    void whenRetrieveEserviceIdThenReturnEserviceDetail(){
        EService eService = interoperabilityClient.getEService(ESERVICEID_OK);
        assertNotNull(eService);
        assertEquals(ESERVICEID_OK, eService.getId().toString());
        assertEquals(ESERVICEID_PRODUCERID_OK, eService.getProducer().getId().toString());
    }

    @Test
    void whenEserviceIDBadlyFormatThenThrowBadRequestException() {
        WebClientResponseException exception =
                assertThrows(WebClientResponseException.class, () -> interoperabilityClient.getEService(ESERVICEID_BAD_REQUEST));

        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void whenClientCallEserviceIDReturn429ThenRetry4Attempts(){
        try {
            interoperabilityClient.getEService(ESERVICEID_TOO_MANY_REQUEST);
        } catch (Throwable ex) {
            assertTrue(Exceptions.isRetryExhausted(ex));
            assertTrue(ex.getCause() instanceof WebClientResponseException);
            assertEquals(HttpStatus.TOO_MANY_REQUESTS, ((WebClientResponseException) ex.getCause()).getStatusCode());
        }
    }

    @Test
    void whenClientCallEserviceIDTimeoutThenRetry4Attempts(){
        Mockito.when(gatewayApi.getEService(UUID.fromString(ESERVICEID_CONNECTION_TIMEOUT)))
                .thenReturn(Mono.error(new TimeoutException()));

        try {
            interoperabilityClient.getEService(ESERVICEID_CONNECTION_TIMEOUT);
        } catch (Throwable ex) {
            assertTrue(Exceptions.isRetryExhausted(ex));
            assertTrue(ex.getCause() instanceof TimeoutException);
        }
    }

    @Test
    void whenClientCallEserviceIDThrowConnectionExceptionThenRetry4Attempts(){
        Mockito.when(gatewayApi.getEService(UUID.fromString(ESERVICEID_NO_CONNECT)))
                .thenReturn(Mono.error(new ConnectException()));

        try {
            interoperabilityClient.getEService(ESERVICEID_NO_CONNECT);
        } catch (Throwable ex) {
            assertTrue(Exceptions.isRetryExhausted(ex));
            assertTrue(ex.getCause() instanceof ConnectException);
        }
    }


    @Test
    void whenRetrieveByAgreementIdThenReturnAgreementDetail(){
        Agreement agreement = interoperabilityClient.getAgreement(AGREEMENTID_OK);
        assertNotNull(agreement);
        assertEquals(AGREEMENTID_OK, agreement.getId().toString());
    }

    @Test
    void whenAgreementIdBadlyFormatThenThrowBadRequestException() {
        WebClientResponseException exception =
                assertThrows(WebClientResponseException.class, () -> interoperabilityClient.getAgreement(AGREEMENTID_BAD_REQUEST));

        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void whenClientCallAgreementIdReturn429ThenRetry4Attempts(){
        try {
            interoperabilityClient.getAgreement(AGREEMENTID_TOO_MANY_REQUEST);
        } catch (Throwable ex) {
            assertTrue(Exceptions.isRetryExhausted(ex));
            assertTrue(ex.getCause() instanceof WebClientResponseException);
            assertEquals(HttpStatus.TOO_MANY_REQUESTS, ((WebClientResponseException) ex.getCause()).getStatusCode());
        }
    }

    @Test
    void whenClientCallAgreementIdTimeoutThenRetry4Attempts(){
        Mockito.when(gatewayApi.getAgreement(UUID.fromString(AGREEMENTID_CONNECTION_TIMEOUT)))
                .thenReturn(Mono.error(new TimeoutException()));

        try {
            interoperabilityClient.getAgreement(AGREEMENTID_CONNECTION_TIMEOUT);
        } catch (Throwable ex) {
            assertTrue(Exceptions.isRetryExhausted(ex));
            assertTrue(ex.getCause() instanceof TimeoutException);
        }
    }

    @Test
    void whenClientCallAgreementIdThrowConnectionExceptionThenRetry4Attempts(){
        Mockito.when(gatewayApi.getAgreement(UUID.fromString(AGREEMENTID_NO_CONNECT)))
                .thenReturn(Mono.error(new ConnectException()));

        try {
            interoperabilityClient.getAgreement(AGREEMENTID_NO_CONNECT);
        } catch (Throwable ex) {
            assertTrue(Exceptions.isRetryExhausted(ex));
            assertTrue(ex.getCause() instanceof ConnectException);
        }
    }


    @Test
    void whenRetrieveByEserviceIdAndDescriptorIdThenReturnEserviceState(){
        EServiceDescriptor eServiceDescriptor = interoperabilityClient.getEServiceDescriptor(ESERVICE_DESCRIPTOR_ESERVICEID_OK, ESERVICE_DESCRIPTOR_DESCRIPTORID_OK);
        assertNotNull(eServiceDescriptor);
        assertEquals(EServiceDescriptorState.PUBLISHED, eServiceDescriptor.getState());
    }

    @Test
    void whenEserviceIdAndDescriptorIdBadlyFormatThenThrowBadRequestException() {
        WebClientResponseException exception =
                assertThrows(WebClientResponseException.class, () -> interoperabilityClient.getEServiceDescriptor(ESERVICE_DESCRIPTOR_ESERVICEID_BAD_REQUEST, ESERVICE_DESCRIPTOR_DESCRIPTORID_BAD_REQUEST));

        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void whenClientCallEserviceIdAndDescriptorIdReturn429ThenRetry4Attempts(){
        try {
            interoperabilityClient.getEServiceDescriptor(ESERVICE_DESCRIPTOR_ESERVICEID_TOO_MANY_REQUEST, ESERVICE_DESCRIPTOR_DESCRIPTORID_TOO_MANY_REQUEST);
        } catch (Throwable ex) {
            assertTrue(Exceptions.isRetryExhausted(ex));
            assertTrue(ex.getCause() instanceof WebClientResponseException);
            assertEquals(HttpStatus.TOO_MANY_REQUESTS, ((WebClientResponseException) ex.getCause()).getStatusCode());
        }
    }

    @Test
    void whenClientCallEserviceIdAndDescriptorIdTimeoutThenRetry4Attempts(){
        Mockito.when(gatewayApi.getEServiceDescriptor(UUID.fromString(ESERVICE_DESCRIPTOR_ESERVICEID_CONNECTION_TIMEOUT), UUID.fromString(ESERVICE_DESCRIPTOR_DESCRIPTORID_CONNECTION_TIMEOUT)))
                .thenReturn(Mono.error(new TimeoutException()));

        try {
            interoperabilityClient.getEServiceDescriptor(ESERVICE_DESCRIPTOR_ESERVICEID_CONNECTION_TIMEOUT, ESERVICE_DESCRIPTOR_DESCRIPTORID_CONNECTION_TIMEOUT);
        } catch (Throwable ex) {
            assertTrue(Exceptions.isRetryExhausted(ex));
            assertTrue(ex.getCause() instanceof TimeoutException);
        }
    }

    @Test
    void whenClientCallEserviceIdAndDescriptorIdThrowConnectionExceptionThenRetry4Attempts(){
        Mockito.when(gatewayApi.getEServiceDescriptor(UUID.fromString(ESERVICE_DESCRIPTOR_ESERVICEID_NO_CONNECT), UUID.fromString(ESERVICE_DESCRIPTOR_DESCRIPTORID_NO_CONNECT)))
                .thenReturn(Mono.error(new ConnectException()));

        try {
            interoperabilityClient.getEServiceDescriptor(ESERVICE_DESCRIPTOR_ESERVICEID_NO_CONNECT, ESERVICE_DESCRIPTOR_DESCRIPTORID_NO_CONNECT);
        } catch (Throwable ex) {
            assertTrue(Exceptions.isRetryExhausted(ex));
            assertTrue(ex.getCause() instanceof ConnectException);
        }
    }

    @Test
    void whenClientCallEserviceIdAndDescriptorIdThrowHttpClientErrorExceptionThenRetry4Attempts() {
        Mockito.when(gatewayApi.getEServiceDescriptor(UUID.fromString(ESERVICE_DESCRIPTOR_ESERVICEID_NO_CONNECT), UUID.fromString(ESERVICE_DESCRIPTOR_DESCRIPTORID_NO_CONNECT)))
                .thenReturn(Mono.error(new HttpClientErrorException(HttpStatus.GATEWAY_TIMEOUT)));

        try {
            interoperabilityClient.getEServiceDescriptor(ESERVICE_DESCRIPTOR_ESERVICEID_NO_CONNECT, ESERVICE_DESCRIPTOR_DESCRIPTORID_NO_CONNECT);
        } catch (Throwable ex) {
            assertTrue(ex instanceof HttpClientErrorException);
            assertEquals(HttpStatus.GATEWAY_TIMEOUT, ((HttpClientErrorException) ex).getStatusCode());
        }
    }
}

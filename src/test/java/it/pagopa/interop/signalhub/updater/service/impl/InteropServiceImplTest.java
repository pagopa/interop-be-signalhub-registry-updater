package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.config.DataBuilder;
import it.pagopa.interop.signalhub.updater.exception.PDNDClientException;
import it.pagopa.interop.signalhub.updater.exception.PDNDConnectionResetException;
import it.pagopa.interop.signalhub.updater.exception.PDNDNoEventsException;
import it.pagopa.interop.signalhub.updater.externalclient.InteroperabilityClient;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.*;
import it.pagopa.interop.signalhub.updater.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.updater.mapper.OrganizationEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.model.EventsDto;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class InteropServiceImplTest {
    private static final WebClientRequestException EXCEPTION_TOKEN_EXPIRED = new WebClientRequestException(new Exception(), HttpMethod.GET, URI.create("localhost:3000"), HttpHeaders.EMPTY);
    private static final WebClientResponseException EXCEPTION_NOT_FOUND = new WebClientResponseException(HttpStatusCode.valueOf(404),"Detail not found", HttpHeaders.EMPTY, "test".getBytes(StandardCharsets.UTF_8), Charset.defaultCharset(), null);
    @InjectMocks
    private InteropServiceImpl interopService;
    @Mock
    private InteroperabilityClient client;
    @Mock
    private ConsumerEServiceMapper mapperConsumer;
    @Mock
    private OrganizationEServiceMapper mapperOrganization;

    /** TEST GET EVENTS DETAIL **/

    @Test
    void getAgreementsAndEServices() {
        Events responseMock = DataBuilder.getEventsWithDuplicate();
        Mockito.when(client.getEventsFromId(Mockito.any()))
                .thenReturn(responseMock);

        EventsDto eventsDto = interopService.getAgreementsAndEServices(1L);
        assertNotNull(eventsDto);
        assertEquals(responseMock.getLastEventId(), eventsDto.getLastEventId());
        assertEquals(responseMock.getEvents().size()-1, eventsDto.getEvents().size());
    }

    @Test
    void whenEventsClientReturnNullOrEmptyThenThrowException(){
        Mockito.when(client.getEventsFromId(Mockito.any())).thenReturn(null);
        PDNDNoEventsException thrownIsNull = assertThrows(PDNDNoEventsException.class,() -> interopService.getAgreementsAndEServices(20L));
        assertEquals("No events from last event id 20", thrownIsNull.getMessage());

        Mockito.when(client.getEventsFromId(Mockito.any())).thenReturn(new Events());
        PDNDNoEventsException thrownIsEmpty = assertThrows(PDNDNoEventsException.class,() -> interopService.getAgreementsAndEServices(20L));
        assertEquals("No events from last event id 20", thrownIsEmpty.getMessage());

    }

    @Test
    void whenTokenWasExpiredEventsThenThrowConnectionResetException() {
        Mockito.when(client.getEventsFromId(Mockito.any()))
                .thenThrow(EXCEPTION_TOKEN_EXPIRED);

        assertThrows(PDNDConnectionResetException.class,
                () -> interopService.getAgreementsAndEServices(2L));
    }

    @Test
    void whenRetrieveEventsNotFoundThenThrowConnectionResetException() {
        Mockito.when(client.getEventsFromId(Mockito.any()))
                .thenThrow(EXCEPTION_NOT_FOUND);

        assertThrows(PDNDClientException.class,
                () -> interopService.getAgreementsAndEServices(2L));
    }

    /** TEST GET ESERVICE DETAIL **/

    @Test
    void whenRetrieveDetailEserviceThenReturnEserviceWithProducerId() {
        Mockito.when(client.getEService(Mockito.any())).thenReturn(DataBuilder.getDetailEservice());

        Mockito.when(mapperOrganization.fromEServiceToOrganizationEServiceDto(Mockito.any(), Mockito.any()))
                .thenReturn(DataBuilder.getEservice());

        OrganizationEServiceDto response = interopService.getEService("123", 1L);
        assertNotNull(response);
        assertEquals(DataBuilder.getEservice().getEserviceId(), response.getEserviceId());
        assertEquals(DataBuilder.getEservice().getDescriptorId(), response.getDescriptorId());
        assertEquals(DataBuilder.getEservice().getEventId(), response.getEventId());
    }

    @Test
    void whenTokenWasExpiredEserviceThenThrowConnectionResetException() {
        Mockito.when(client.getEService(Mockito.any()))
                .thenThrow(EXCEPTION_TOKEN_EXPIRED);

        assertThrows(PDNDConnectionResetException.class,
                () -> interopService.getEService(DataBuilder.getEservice().getEserviceId(), 2L));
    }

    @Test
    void whenDetailEserviceNotFoundThenThrowConnectionResetException() {
        Mockito.when(client.getEService(Mockito.any()))
                .thenThrow(EXCEPTION_NOT_FOUND);

        assertThrows(PDNDClientException.class,
                () -> interopService.getEService(DataBuilder.getEservice().getEserviceId(), 2L));
    }

    /** TEST GET AGREEMENT DETAIL **/

    @Test
    void whenRetrieveDetailAgreementThenReturnConsumer() {
        Mockito.when(client.getAgreement(Mockito.any()))
                .thenReturn(DataBuilder.getDetailAgreement());

        Mockito.when(mapperConsumer.toConsumerEServiceDtoFromAgreement(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(DataBuilder.getConsumerDto());

        ConsumerEServiceDto response = interopService.getConsumerEService(DataBuilder.getConsumerDto().getAgreementId(), 2L);
        assertNotNull(response);
        assertEquals(DataBuilder.getConsumerDto().getEserviceId(), response.getEserviceId());
        assertEquals(DataBuilder.getConsumerDto().getDescriptorId(), response.getDescriptorId());
        assertEquals(DataBuilder.getConsumerDto().getEventId(), response.getEventId());
    }

    @Test
    void whenTokenWasExpiredAgreementThenThrowConnectionResetException() {
        Mockito.when(client.getAgreement(Mockito.any()))
                .thenThrow(EXCEPTION_TOKEN_EXPIRED);

        assertThrows(PDNDConnectionResetException.class,
                () -> interopService.getConsumerEService(DataBuilder.getConsumerDto().getAgreementId(), 2L));
    }

    @Test
    void whenDetailAgreementNotFoundThenThrowConnectionResetException() {
        Mockito.when(client.getAgreement(Mockito.any()))
                .thenThrow(EXCEPTION_NOT_FOUND);

        assertThrows(PDNDClientException.class,
                () -> interopService.getConsumerEService(DataBuilder.getConsumerDto().getAgreementId(), 2L));
    }


    /** TEST GET ESERVICE DESCRIPTOR **/



    @Test
    void whenRetrievedDetailEserviceThenReturnNewState() {
        Mockito.when(client.getEServiceDescriptor(Mockito.any(), Mockito.any())).thenReturn(DataBuilder.getEserviceDescriptor());

        OrganizationEServiceDto response = interopService.getEServiceDescriptor(DataBuilder.getEservice());

        assertNotNull(response);
        assertEquals(EServiceDescriptorState.ARCHIVED.getValue(), response.getState());
        assertEquals(DataBuilder.getEservice().getEserviceId(), response.getEserviceId());
        assertEquals(DataBuilder.getEservice().getDescriptorId(), response.getDescriptorId());
        assertEquals(DataBuilder.getEservice().getEventId(), response.getEventId());
    }

    @Test
    void whenTokenWasExpiredThenThrowConnectionResetException() {
        Mockito.when(client.getEServiceDescriptor(Mockito.any(), Mockito.any()))
                .thenThrow(EXCEPTION_TOKEN_EXPIRED);

        assertThrows(PDNDConnectionResetException.class,
                () -> interopService.getEServiceDescriptor(DataBuilder.getEservice()));
    }

    @Test
    void whenDetailNotFoundThenThrowConnectionResetException() {
        Mockito.when(client.getEServiceDescriptor(Mockito.any(), Mockito.any()))
                .thenThrow(EXCEPTION_NOT_FOUND);

        assertThrows(PDNDClientException.class,
                () -> interopService.getEServiceDescriptor(DataBuilder.getEservice()));
    }


}
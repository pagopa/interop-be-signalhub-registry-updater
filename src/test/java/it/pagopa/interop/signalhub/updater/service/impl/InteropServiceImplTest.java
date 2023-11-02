package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.exception.PDNDClientException;
import it.pagopa.interop.signalhub.updater.exception.PDNDConnectionResetException;
import it.pagopa.interop.signalhub.updater.exception.PDNDNoEventsException;
import it.pagopa.interop.signalhub.updater.externalclient.InteroperabilityClient;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.*;
import it.pagopa.interop.signalhub.updater.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.updater.mapper.OrganizationEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static it.pagopa.interop.signalhub.updater.utility.Const.AGREEMENT_EVENT;
import static it.pagopa.interop.signalhub.updater.utility.Const.ESERVICE_EVENT;
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


    @Test
    void whenCallGetAgreementsAndEServicesAndEventIsNullOrEmpty() {
        Mockito.when(client.getEventsFromId(Mockito.any())).thenReturn(null);
        Long lastEventId = 1L;
        PDNDNoEventsException thrownIsNull = assertThrows(
                PDNDNoEventsException.class,
                () -> {
                    interopService.getAgreementsAndEServices(1L);
                }
        );
        assertEquals("No events from last event id ".concat(lastEventId.toString()), thrownIsNull.getMessage());

        Mockito.when(client.getEventsFromId(Mockito.any())).thenReturn(new Events());

        PDNDNoEventsException thrownIsEmpty = assertThrows(
                PDNDNoEventsException.class,
                () -> {
                    interopService.getAgreementsAndEServices(1L);
                }
        );
        assertEquals("No events from last event id ".concat(lastEventId.toString()), thrownIsEmpty.getMessage());
    }

    @Test
    void getAgreementsAndEServices() {
        Event event= new Event();
        event.setEventId(1L);
        event.setEventType("test");
        event.setObjectId(new HashMap<>());
        event.setObjectType(ESERVICE_EVENT);

        List<Event> eventList= new ArrayList<>();
        eventList.add(event);

        Events events= new Events();
        events.setEvents(eventList);

        Mockito.when(client.getEventsFromId(Mockito.any())).thenReturn(events);
        assertNotNull(interopService.getAgreementsAndEServices(1L));


        event.setObjectType(AGREEMENT_EVENT);
        Mockito.when(client.getEventsFromId(Mockito.any())).thenReturn(events);
        assertNotNull(interopService.getAgreementsAndEServices(1L));
    }

    @Test
    void getConsumerEService() {
        Mockito.when(client.getAgreement(Mockito.any())).thenReturn(new Agreement());
        Mockito.when(mapperConsumer.toConsumerEServiceDtoFromAgreement(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new ConsumerEServiceDto());

        assertNotNull(interopService.getConsumerEService("123", 1L));
    }

    @Test
    void whenRetrieveDetailEserviceThenReturnEserviceWithProducerId() {
        Mockito.when(client.getEService(Mockito.any())).thenReturn(getEserviceResponse());

        Mockito.when(mapperOrganization.fromEServiceToOrganizationEServiceDto(Mockito.any(), Mockito.any()))
                .thenReturn(getEservice());

        OrganizationEServiceDto response = interopService.getEService("123", 1L);
        assertNotNull(response);
        assertEquals(getEservice().getEserviceId(), response.getEserviceId());
        assertEquals(getEservice().getDescriptorId(), response.getDescriptorId());
        assertEquals(getEservice().getEventId(), response.getEventId());
    }

    @Test
    void whenRetrievedDetailEserviceThenReturnNewState() {
        Mockito.when(client.getEServiceDescriptor(Mockito.any(), Mockito.any())).thenReturn(getEserviceDescriptor());

        OrganizationEServiceDto response = interopService.getEServiceDescriptor(getEservice());

        assertNotNull(response);
        assertEquals(EServiceDescriptorState.ARCHIVED.getValue(), response.getState());
        assertEquals(getEservice().getEserviceId(), response.getEserviceId());
        assertEquals(getEservice().getDescriptorId(), response.getDescriptorId());
        assertEquals(getEservice().getEventId(), response.getEventId());
    }

    @Test
    void whenTokenWasExpiredThenThrowConnectionResetException() {
        Mockito.when(client.getEServiceDescriptor(Mockito.any(), Mockito.any()))
                .thenThrow(EXCEPTION_TOKEN_EXPIRED);

        assertThrows(PDNDConnectionResetException.class,
                () -> interopService.getEServiceDescriptor(getEservice()));
    }

    @Test
    void whenDetailNotFoundThenThrowConnectionResetException() {
        Mockito.when(client.getEServiceDescriptor(Mockito.any(), Mockito.any()))
                .thenThrow(EXCEPTION_NOT_FOUND);

        assertThrows(PDNDClientException.class,
                () -> interopService.getEServiceDescriptor(getEservice()));
    }


    private EService getEserviceResponse(){
        EService eService = new EService();
        eService.setId(UUID.fromString("1925cc0d-b65c-4a78-beca-b990b933ecf3"));
        Organization organization = new Organization();
        organization.setId(UUID.fromString("63362ead-f496-4a00-8d1e-1073d744a13f"));
        eService.setProducer(organization);
        return eService;
    }

    private OrganizationEServiceDto getEservice(){
        OrganizationEServiceDto dto = new OrganizationEServiceDto();
        dto.setEserviceId("1925cc0d-b65c-4a78-beca-b990b933ecf3");
        dto.setDescriptorId("3627f106-00c5-4ddc-8c0b-9ba68cd4446b");
        dto.setEventId(2L);
        dto.setProducerId("63362ead-f496-4a00-8d1e-1073d744a13f");
        return dto;
    }

    private EServiceDescriptor getEserviceDescriptor(){
        EServiceDescriptor dto = new EServiceDescriptor();
        dto.setState(EServiceDescriptorState.ARCHIVED);
        return dto;
    }

}
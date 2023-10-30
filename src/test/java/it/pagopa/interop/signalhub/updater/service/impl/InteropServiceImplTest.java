package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.exception.PDNDNoEventsException;
import it.pagopa.interop.signalhub.updater.externalclient.InteroperabilityClient;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.*;
import it.pagopa.interop.signalhub.updater.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.updater.mapper.EServiceDescriptorMapper;
import it.pagopa.interop.signalhub.updater.mapper.OrganizationEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.model.EServiceDescriptorDto;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static it.pagopa.interop.signalhub.updater.utility.Const.AGREEMENT_EVENT;
import static it.pagopa.interop.signalhub.updater.utility.Const.ESERVICE_EVENT;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class InteropServiceImplTest {
    @InjectMocks
    private InteropServiceImpl interopService;
    @Mock
    private InteroperabilityClient client;
    @Mock
    private ConsumerEServiceMapper mapperConsumer;
    @Mock
    private OrganizationEServiceMapper mapperOrganization;
    @Mock
    private EServiceDescriptorMapper eServiceDescriptorMapper;


    @Test
    void whenCallGetAgreementsAndEServicesAndEventIsNullOrEmpty() {
        Mockito.when(client.getEventsFromId(Mockito.any())).thenReturn(null);
        Long lastEventId= 1l;
        PDNDNoEventsException thrownIsNull = assertThrows(
                PDNDNoEventsException.class,
                () -> {
                    interopService.getAgreementsAndEServices(1l);
                }
        );
        assertEquals("No events from last event id ".concat(lastEventId.toString()), thrownIsNull.getMessage());

        Mockito.when(client.getEventsFromId(Mockito.any())).thenReturn(new Events());

        PDNDNoEventsException thrownIsEmpty = assertThrows(
                PDNDNoEventsException.class,
                () -> {
                    interopService.getAgreementsAndEServices(1l);
                }
        );
        assertEquals("No events from last event id ".concat(lastEventId.toString()), thrownIsEmpty.getMessage());
    }

    @Test
    void getAgreementsAndEServices() {
        Event event= new Event();
        event.setEventId(1l);
        event.setEventType("test");
        event.setObjectId(new HashMap<>());
        event.setObjectType(ESERVICE_EVENT);

        List<Event> eventList= new ArrayList<>();
        eventList.add(event);

        Events events= new Events();
        events.setEvents(eventList);

        Mockito.when(client.getEventsFromId(Mockito.any())).thenReturn(events);
        assertNotNull(interopService.getAgreementsAndEServices(1l));


        event.setObjectType(AGREEMENT_EVENT);
        Mockito.when(client.getEventsFromId(Mockito.any())).thenReturn(events);
        assertNotNull(interopService.getAgreementsAndEServices(1l));
    }

    @Test
    void getConsumerEService() {
        Mockito.when(client.getAgreement(Mockito.any())).thenReturn(new Agreement());
        Mockito.when(mapperConsumer.toConsumerEServiceDtoFromAgreement(Mockito.any(), Mockito.any())).thenReturn(new ConsumerEServiceDto());

        assertNotNull(interopService.getConsumerEService("123", 1l));
    }

    @Test
    void getEService() {
        Mockito.when(client.getEService(Mockito.any())).thenReturn(new EService());
        Mockito.when(mapperOrganization.fromEServiceToOrganizationEServiceDto(Mockito.any(), Mockito.any())).thenReturn(new OrganizationEServiceDto());
        assertNotNull(interopService.getEService("123", 1l));
    }

    @Test
    void getEServiceDescriptor() {
        Mockito.when(client.getEServiceDescriptor(Mockito.any(), Mockito.any())).thenReturn(new EServiceDescriptor());
        Mockito.when(eServiceDescriptorMapper.fromEServiceDescriptorToEServiceDescriptorDto(Mockito.any())).thenReturn(new EServiceDescriptorDto());
        assertNotNull(interopService.getEServiceDescriptor("123", 1l, "ABC"));
    }
}
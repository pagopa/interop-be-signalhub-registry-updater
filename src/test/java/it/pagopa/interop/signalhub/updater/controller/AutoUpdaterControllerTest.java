package it.pagopa.interop.signalhub.updater.controller;

import it.pagopa.interop.signalhub.updater.entity.DeadEvent;
import it.pagopa.interop.signalhub.updater.exception.PDNDEventException;
import it.pagopa.interop.signalhub.updater.exception.PDNDConnectionResetException;
import it.pagopa.interop.signalhub.updater.exception.PDNDNoEventsException;
import it.pagopa.interop.signalhub.updater.model.*;
import it.pagopa.interop.signalhub.updater.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.pagopa.interop.signalhub.updater.utility.Const.AGREEMENT_EVENT;
import static it.pagopa.interop.signalhub.updater.utility.Const.ESERVICE_EVENT;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class AutoUpdaterControllerTest {
    @InjectMocks
    private AutoUpdaterController autoUpdaterController;
    @Mock
    private InteropService interopService;
    @Mock
    private OrganizationService organizationService;
    @Mock
    private ConsumerService consumerService;
    @Mock
    private TracingBatchService tracingBatchService;
    @Mock
    private DeadEventService deadEventService;
    private Long batchId;
    private Long startEventId;
    private Long finalEventId;
    private String eventType;
    private String eserviceObjectType;
    private String agreeementObjectType;
    private String eserviceId;
    private String agreementId;


    @BeforeEach
    void preTest(){
        this.setUp();
    }

    @Test
    void scheduleUpdaterWhenEServiceEventIsConsumedTest() {
        TracingBatchDto tracingBatchDto = getTracingBatchDto();

        Mockito.when(tracingBatchService.getLastEventIdByTracingBatchAndType(ESERVICE_EVENT))
                .thenReturn(this.startEventId);

        EventsDto eventsDto1 = getEventsDtoWithEservice();
        EServiceEventDto eServiceEventDto = (EServiceEventDto)eventsDto1.getEvents().get(0);
        Mockito.doReturn(eventsDto1)
                .when(interopService)
                .getEventsByType(tracingBatchDto.getLastEventId(), ESERVICE_EVENT);

        Mockito.when(organizationService.updateOrganizationEService(eServiceEventDto))
                        .thenReturn(new OrganizationEServiceDto());

        tracingBatchDto.setLastEventId(this.finalEventId);
        EventsDto eventsDto2 = new EventsDto();
        eventsDto2.setLastEventId(this.startEventId);
        Mockito.doReturn(eventsDto2)
                .doThrow(new PDNDNoEventsException("No events from last event id ".concat(tracingBatchDto.toString())))
                .when(interopService)
                .getEventsByType(tracingBatchDto.getLastEventId(), ESERVICE_EVENT);

        tracingBatchDto.setState(TracingBatchStateEnum.ENDED);
        Mockito.when(tracingBatchService.terminateTracingBatch(TracingBatchStateEnum.ENDED, tracingBatchDto.getLastEventId(), ESERVICE_EVENT))
                .thenReturn(tracingBatchDto);

        assertDoesNotThrow(() -> autoUpdaterController.scheduleUpdater(ESERVICE_EVENT));
    }

    @Test
    void scheduleUpdaterWhenAgreementEventIsConsumedTest() {
        TracingBatchDto tracingBatchDto = getTracingBatchDto();

        Mockito.when(tracingBatchService.getLastEventIdByTracingBatchAndType(ESERVICE_EVENT))
                .thenReturn(this.startEventId);

        EventsDto eventsDto1 = getEventsDtoWithAgreement();
        AgreementEventDto agreementEventDto = (AgreementEventDto)eventsDto1.getEvents().get(0);
        Mockito.doReturn(eventsDto1)
                .when(interopService)
                .getEventsByType(tracingBatchDto.getLastEventId(), ESERVICE_EVENT);

        Mockito.when(consumerService.updateConsumer(agreementEventDto))
                .thenReturn(new ConsumerEServiceDto());

        tracingBatchDto.setLastEventId(this.finalEventId);
        EventsDto eventsDto2 = new EventsDto();
        eventsDto2.setLastEventId(this.startEventId);
        Mockito.doReturn(eventsDto2)
                .doThrow(new PDNDNoEventsException("No events from last event id ".concat(tracingBatchDto.toString())))
                .when(interopService)
                .getEventsByType(tracingBatchDto.getLastEventId(), ESERVICE_EVENT);

        tracingBatchDto.setState(TracingBatchStateEnum.ENDED);
        Mockito.when(tracingBatchService.terminateTracingBatch(TracingBatchStateEnum.ENDED, tracingBatchDto.getLastEventId(), ESERVICE_EVENT))
                .thenReturn(tracingBatchDto);

        assertDoesNotThrow(() -> autoUpdaterController.scheduleUpdater(ESERVICE_EVENT));
    }

    @Test
    void scheduleUpdaterWhenConnectionResetTest() {
        TracingBatchDto tracingBatchDto = getTracingBatchDto();

        Mockito.when(tracingBatchService.getLastEventIdByTracingBatchAndType(ESERVICE_EVENT))
                .thenReturn(this.startEventId);

        EventsDto eventsDto1 = getEventsDtoWithEservice();
        EServiceEventDto eServiceEventDto = (EServiceEventDto)eventsDto1.getEvents().get(0);

        Mockito.when(interopService.getEventsByType(tracingBatchDto.getLastEventId(), ESERVICE_EVENT))
                .thenReturn(eventsDto1);

        tracingBatchDto.setLastEventId(this.finalEventId);
        EventsDto eventsDto2 = new EventsDto();
        eventsDto2.setLastEventId(this.startEventId);
        Mockito.doReturn(eventsDto2)
                .doThrow(new PDNDConnectionResetException("Connection token was expired", eServiceEventDto.getEventId()))
                .when(interopService)
                .getEventsByType(tracingBatchDto.getLastEventId(), ESERVICE_EVENT);

        tracingBatchDto.setState(TracingBatchStateEnum.ENDED);
        Mockito.when(tracingBatchService.terminateTracingBatch(TracingBatchStateEnum.ENDED, eServiceEventDto.getEventId(), ESERVICE_EVENT))
                .thenReturn(tracingBatchDto);

        Exception exception = assertThrows(PDNDConnectionResetException.class, () -> autoUpdaterController.scheduleUpdater(ESERVICE_EVENT));
        assertEquals("Connection token was expired", exception.getMessage());
    }

    @Test
    void scheduleUpdaterWhenHttpStatusDifferentOf200Test() {
        TracingBatchDto tracingBatchDto = getTracingBatchDto();

        Mockito.when(tracingBatchService.getLastEventIdByTracingBatchAndType(AGREEMENT_EVENT))
                .thenReturn(this.startEventId);

        EventsDto eventsDto = getEventsDtoWithAgreement();
        AgreementEventDto agreementEventDto = (AgreementEventDto)eventsDto.getEvents().get(0);

        Mockito.when(interopService.getEventsByType(tracingBatchDto.getLastEventId(), AGREEMENT_EVENT))
                .thenReturn(eventsDto);

        Mockito.when(consumerService.updateConsumer(agreementEventDto))
                .thenThrow(new PDNDEventException("Error with retrieve agreement details", agreementEventDto.getEventId()));

        Mockito.when(deadEventService.saveDeadEvent(agreementEventDto, AGREEMENT_EVENT))
                .thenReturn(new DeadEvent());

        tracingBatchDto.setState(TracingBatchStateEnum.ENDED_WITH_ERROR);
        Mockito.when(tracingBatchService.terminateTracingBatch(TracingBatchStateEnum.ENDED_WITH_ERROR, agreementEventDto.getEventId(), AGREEMENT_EVENT))
                .thenReturn(tracingBatchDto);

        Exception exception = assertThrows(PDNDEventException.class, () -> autoUpdaterController.scheduleUpdater(AGREEMENT_EVENT));
        assertEquals("Error with retrieve agreement details", exception.getMessage());
    }

    private TracingBatchDto getTracingBatchDto() {
        TracingBatchDto tracingBatchDto = new TracingBatchDto();
        tracingBatchDto.setState(TracingBatchStateEnum.ENDED);
        tracingBatchDto.setLastEventId(this.startEventId);
        tracingBatchDto.setBatchId(this.batchId);
        return tracingBatchDto;
    }

    private EventsDto getEventsDtoWithEservice() {
        EventsDto eventsDto = new EventsDto();
        EServiceEventDto eServiceEventDto = new EServiceEventDto();
        eServiceEventDto.setEventId(this.finalEventId);
        eServiceEventDto.setEventType(this.eventType);
        eServiceEventDto.setObjectType(this.eserviceObjectType);
        eServiceEventDto.setEServiceId(this.eserviceId);
        eventsDto.getEvents().add(eServiceEventDto);
        eventsDto.setLastEventId(this.finalEventId);
        return eventsDto;
    }

    private EventsDto getEventsDtoWithAgreement() {
        EventsDto eventsDto = new EventsDto();
        AgreementEventDto agreementEventDto = new AgreementEventDto();
        agreementEventDto.setEventId(this.finalEventId);
        agreementEventDto.setEventType(this.eventType);
        agreementEventDto.setObjectType(this.agreeementObjectType);
        agreementEventDto.setAgreementId(this.agreementId);
        eventsDto.getEvents().add(agreementEventDto);
        eventsDto.setLastEventId(this.finalEventId);
        return eventsDto;
    }

    private void setUp() {
        this.batchId = 0L;
        this.startEventId = 0L;
        this.finalEventId = 1L;
        this.eventType = "ADDED";
        this.eserviceObjectType = "ESERVICE";
        this.agreeementObjectType = "AGREEMENT";
        this.eserviceId = "515cbcd9-831d-45ca-85e4-802fb2c80112";
        this.agreementId = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
    }
}
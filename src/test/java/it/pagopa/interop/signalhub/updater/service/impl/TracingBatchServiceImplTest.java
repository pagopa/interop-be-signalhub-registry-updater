package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.config.RegistryUpdaterProps;
import it.pagopa.interop.signalhub.updater.entity.TracingBatchEntity;
import it.pagopa.interop.signalhub.updater.mapper.TracingBatchMapper;
import it.pagopa.interop.signalhub.updater.model.TracingBatchDto;
import it.pagopa.interop.signalhub.updater.repository.TracingBatchRepository;
import it.pagopa.interop.signalhub.updater.utility.Const;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static it.pagopa.interop.signalhub.updater.model.TracingBatchStateEnum.ENDED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class TracingBatchServiceImplTest {
    @InjectMocks
    private TracingBatchServiceImpl tracingBatchService;
    @Mock
    private TracingBatchRepository repository;
    @Mock
    private TracingBatchMapper mapper;
    @Mock
    private RegistryUpdaterProps props;


    @Test
    void checkAndCreateTracingBatch() {
        //list==empty and return 0L
        Mockito.when(repository.findByStateAndLastEventIdMaxAndType(Const.ESERVICE_EVENT)).thenReturn(new ArrayList<>());
        assertEquals(tracingBatchService.getLastEventIdByTracingBatchAndType(Const.ESERVICE_EVENT), 0L);

        //state = ENDED
        TracingBatchEntity tracingBatchEntity= new TracingBatchEntity();
        tracingBatchEntity.setState(ENDED.name());
        tracingBatchEntity.setLastEventId(1L);
        List<TracingBatchEntity> list= new ArrayList<>();
        list.add(tracingBatchEntity);
        Mockito.when(repository.findByStateAndLastEventIdMaxAndType(Const.ESERVICE_EVENT)).thenReturn(list);
        assertEquals(tracingBatchService.getLastEventIdByTracingBatchAndType(Const.ESERVICE_EVENT), tracingBatchEntity.getLastEventId());

        //lastEventId==tracingBatchEntity.getLastEventId()+1
        tracingBatchEntity.setState("test");
        list= new ArrayList<>();
        list.add(tracingBatchEntity);
        Mockito.when(repository.findByStateAndLastEventIdMaxAndType(Const.ESERVICE_EVENT)).thenReturn(list);
        Mockito.when(props.getAttemptEvent()).thenReturn(0);
        assertEquals(tracingBatchService.getLastEventIdByTracingBatchAndType(Const.ESERVICE_EVENT), tracingBatchEntity.getLastEventId()+1);

        //lastEventId==tracingBatchEntity.getLastEventId()
        Mockito.when(props.getAttemptEvent()).thenReturn(3);
        assertEquals(tracingBatchService.getLastEventIdByTracingBatchAndType(Const.ESERVICE_EVENT), tracingBatchEntity.getLastEventId());
    }

    @Test
    void countBatchInErrorWithLastEventId() {
        //list==null
        Mockito.when(repository.findAllStateEndedWithErrorAndLastEventIdAndType(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(null);
        assertEquals(0, tracingBatchService.countBatchInErrorWithLastEventIdAndType(1L, Const.ESERVICE_EVENT));

        //list=empty
        Mockito.when(repository.findAllStateEndedWithErrorAndLastEventIdAndType(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>());
        assertEquals(0, tracingBatchService.countBatchInErrorWithLastEventIdAndType(1L, Const.ESERVICE_EVENT));

        List<TracingBatchEntity> tracingBatchEntityList = new ArrayList<>();
        tracingBatchEntityList.add(new TracingBatchEntity());
        Mockito.when(repository.findAllStateEndedWithErrorAndLastEventIdAndType(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(tracingBatchEntityList);
        assertEquals(1, tracingBatchService.countBatchInErrorWithLastEventIdAndType(1L, Const.ESERVICE_EVENT));

    }

    @Test
    void terminateTracingBatch() {
        Mockito.when(repository.saveAndFlush(Mockito.any())).thenReturn(new TracingBatchEntity());
        Mockito.when(mapper.toDto(Mockito.any())).thenReturn(new TracingBatchDto());
        assertNotNull(tracingBatchService.terminateTracingBatch( ENDED, 1L, Const.ESERVICE_EVENT));
    }
}
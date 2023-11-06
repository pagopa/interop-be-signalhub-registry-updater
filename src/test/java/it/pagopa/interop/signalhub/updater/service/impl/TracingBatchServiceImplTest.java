package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.config.RegistryUpdaterProps;
import it.pagopa.interop.signalhub.updater.entity.TracingBatchEntity;
import it.pagopa.interop.signalhub.updater.exception.PDNDBatchAlreadyExistException;
import it.pagopa.interop.signalhub.updater.exception.PDNDEntityNotFound;
import it.pagopa.interop.signalhub.updater.mapper.TracingBatchMapper;
import it.pagopa.interop.signalhub.updater.model.TracingBatchDto;
import it.pagopa.interop.signalhub.updater.repository.TracingBatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static it.pagopa.interop.signalhub.updater.model.TracingBatchStateEnum.ENDED;
import static it.pagopa.interop.signalhub.updater.model.TracingBatchStateEnum.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.*;


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
    void checkAndCreateTracingBatchButBatchAlreadyInRunning() {
        Mockito.when(repository.findByStateProgressAndLastEventIdMax(Mockito.any())).thenReturn(Optional.of(new TracingBatchEntity()));

        PDNDBatchAlreadyExistException thrown = assertThrows(
                PDNDBatchAlreadyExistException.class,
                () -> {
                    tracingBatchService.checkAndCreateTracingBatch();
                }
        );
        assertEquals("Batch already in running", thrown.getMessage());
    }

    @Test
    void checkAndCreateTracingBatch() {
        //lastEventId==0L
        Mockito.when(repository.findByStateProgressAndLastEventIdMax(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(repository.findByStateAndLastEventIdMax()).thenReturn(new ArrayList<>());
        Mockito.when(repository.saveAndFlush(Mockito.any())).thenReturn(new TracingBatchEntity());
        Mockito.when(mapper.toDto(Mockito.any())).thenReturn(new TracingBatchDto());
        assertEquals(tracingBatchService.checkAndCreateTracingBatch().getLastEventId(), 0L);


        //lastEventId==tracingBatchEntity.getLastEventId()
        TracingBatchEntity tracingBatchEntity= new TracingBatchEntity();
        tracingBatchEntity.setState(ENDED.name());
        tracingBatchEntity.setLastEventId(1L);
        List<TracingBatchEntity> list= new ArrayList<>();
        list.add(tracingBatchEntity);
        Mockito.when(repository.findByStateAndLastEventIdMax()).thenReturn(list);
        assertEquals(tracingBatchService.checkAndCreateTracingBatch().getLastEventId(), tracingBatchEntity.getLastEventId());

        //lastEventId==tracingBatchEntity.getLastEventId()+1
        tracingBatchEntity.setState("test");
        list= new ArrayList<>();
        list.add(tracingBatchEntity);
        Mockito.when(repository.findByStateAndLastEventIdMax()).thenReturn(list);
        Mockito.when(props.getAttemptEvent()).thenReturn(0);
        assertEquals(tracingBatchService.checkAndCreateTracingBatch().getLastEventId(), tracingBatchEntity.getLastEventId()+1);

        //lastEventId==tracingBatchEntity.getLastEventId()
        Mockito.when(props.getAttemptEvent()).thenReturn(3);
        assertEquals(tracingBatchService.checkAndCreateTracingBatch().getLastEventId(), tracingBatchEntity.getLastEventId());
    }

    @Test
    void countBatchInErrorWithLastEventId() {
        Mockito.when(repository.findAllStateEndedWithErrorAndLastEventId(Mockito.any(), Mockito.any())).thenReturn(null);
        assertEquals(0, tracingBatchService.countBatchInErrorWithLastEventId(1L));

        Mockito.when(repository.findAllStateEndedWithErrorAndLastEventId(Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>());
        assertEquals(0, tracingBatchService.countBatchInErrorWithLastEventId(1L));

        List<TracingBatchEntity> tracingBatchEntityList = new ArrayList<>();
        tracingBatchEntityList.add(new TracingBatchEntity());
        Mockito.when(repository.findAllStateEndedWithErrorAndLastEventId(Mockito.any(), Mockito.any()))
                .thenReturn(tracingBatchEntityList);
        assertEquals(1, tracingBatchService.countBatchInErrorWithLastEventId(1L));

    }

    @Test
    void terminateTracingBatchButBatchNotFound() {
        Long batchId= 1L;
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.empty());

        PDNDEntityNotFound thrown = assertThrows(
                PDNDEntityNotFound.class,
                () -> {
                    tracingBatchService.terminateTracingBatch(batchId, IN_PROGRESS, 1L );
                }
        );
        assertEquals("Batch entity not founded with ".concat(batchId.toString()).concat(" id"), thrown.getMessage());
    }

    @Test
    void terminateTracingBatch() {
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(new TracingBatchEntity()));
        Mockito.when(repository.saveAndFlush(Mockito.any())).thenReturn(new TracingBatchEntity());
        Mockito.when(mapper.toDto(Mockito.any())).thenReturn(new TracingBatchDto());
        assertNotNull(tracingBatchService.terminateTracingBatch(1L, ENDED, 1L ));
    }
}
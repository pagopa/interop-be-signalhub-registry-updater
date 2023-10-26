package it.pagopa.interop.signalhub.updater.mapper;

import it.pagopa.interop.signalhub.updater.entity.TracingBatchEntity;
import it.pagopa.interop.signalhub.updater.model.TracingBatchDto;
import it.pagopa.interop.signalhub.updater.model.TracingBatchStateEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class TracingBatchMapperImplTest {
    private Long batchId;
    private Long lastEventId;
    private String state;
    private TracingBatchMapperImpl tracingBatchMapper;

    @BeforeEach
    void preTest(){
        this.setUp();
    }

    @Test
    void toDtoTest() {
        TracingBatchEntity tracingBatchEntity = getTracingBatchEntity();
        TracingBatchDto tracingBatchDto = tracingBatchMapper.toDto(tracingBatchEntity);
        assertNotNull(tracingBatchDto);
        assertEquals(batchId, tracingBatchDto.getBatchId());
        assertEquals(lastEventId, tracingBatchDto.getLastEventId());
        assertEquals(state, tracingBatchDto.getState().name());

        tracingBatchEntity.setState(null);
        tracingBatchDto = tracingBatchMapper.toDto(tracingBatchEntity);
        assertNotNull(tracingBatchDto);
        assertNull(tracingBatchDto.getState());
    }

    @Test
    void toDtoNullCaseTest() {
        TracingBatchEntity tracingBatchEntity = null;
        TracingBatchDto tracingBatchDto = tracingBatchMapper.toDto(tracingBatchEntity);
        assertNull(tracingBatchDto);
    }

    private TracingBatchEntity getTracingBatchEntity() {
        TracingBatchEntity tracingBatchEntity = new TracingBatchEntity();
        tracingBatchEntity.setBatchId(batchId);
        tracingBatchEntity.setLastEventId(lastEventId);
        tracingBatchEntity.setState(state);
        return tracingBatchEntity;
    }

    private void setUp() {
        this.batchId = 0L;
        this.lastEventId = 100L;
        this.state = TracingBatchStateEnum.ENDED.name();
        this.tracingBatchMapper = new TracingBatchMapperImpl();
    }
}
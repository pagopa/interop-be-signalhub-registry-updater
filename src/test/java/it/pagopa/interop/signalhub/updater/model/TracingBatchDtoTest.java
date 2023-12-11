package it.pagopa.interop.signalhub.updater.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.time.Instant;


class TracingBatchDtoTest {
    private Long batchId;
    private Long lastEventId;
    private TracingBatchStateEnum state;
    private Timestamp dateStarted;
    private Timestamp dateEnded;


    @BeforeEach
    void preTest(){
        this.setUp();
    }

    @Test
    void setGetTest() {
        TracingBatchDto tracingBatchDto = new TracingBatchDto();
        Assertions.assertNotNull(tracingBatchDto);

        tracingBatchDto.setBatchId(batchId);
        Assertions.assertEquals(batchId, tracingBatchDto.getBatchId());

        tracingBatchDto.setLastEventId(lastEventId);
        Assertions.assertEquals(lastEventId, tracingBatchDto.getLastEventId());

        tracingBatchDto.setState(state);
        Assertions.assertEquals(state, tracingBatchDto.getState());

        tracingBatchDto.setDateStarted(dateStarted);
        Assertions.assertEquals(dateStarted, tracingBatchDto.getDateStarted());

        tracingBatchDto.setDateEnded(dateEnded);
        Assertions.assertEquals(dateEnded, tracingBatchDto.getDateEnded());
    }

    @Test
    void toStringTest() {
        TracingBatchDto tracingBatchDto = getTracingBatchDto();
        Assertions.assertNotNull(tracingBatchDto);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tracingBatchDto.getClass().getSimpleName());
        stringBuilder.append("(");
        stringBuilder.append("batchId=");
        stringBuilder.append(tracingBatchDto.getBatchId());
        stringBuilder.append(", ");
        stringBuilder.append("lastEventId=");
        stringBuilder.append(tracingBatchDto.getLastEventId());
        stringBuilder.append(", ");
        stringBuilder.append("state=");
        stringBuilder.append(tracingBatchDto.getState().toString());
        stringBuilder.append(", ");
        stringBuilder.append("dateStarted=");
        stringBuilder.append(tracingBatchDto.getDateStarted());
        stringBuilder.append(", ");
        stringBuilder.append("dateEnded=");
        stringBuilder.append(tracingBatchDto.getDateEnded());
        stringBuilder.append(")");

        String toTest = stringBuilder.toString();
        Assertions.assertEquals(toTest, tracingBatchDto.toString());
    }

    private TracingBatchDto getTracingBatchDto() {
        TracingBatchDto tracingBatchDto = new TracingBatchDto();
        tracingBatchDto.setBatchId(batchId);
        tracingBatchDto.setLastEventId(lastEventId);
        tracingBatchDto.setState(state);
        tracingBatchDto.setDateEnded(dateEnded);
        return tracingBatchDto;
    }

    private void setUp() {
        this.batchId = 0L;
        this.lastEventId = 1L;
        this.state = TracingBatchStateEnum.ENDED;
        this.dateStarted = Timestamp.from(Instant.parse("2023-10-20T18:15:00.000Z"));
        this.dateEnded = Timestamp.from(Instant.parse("2023-10-23T09:00:00.000Z"));
    }
}
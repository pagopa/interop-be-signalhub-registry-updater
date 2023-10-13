package it.pagopa.interop.signalhub.updater.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.Instant;


@Getter
@Setter
@ToString
public class TracingBatchDto {
    private Long batchId;

    private Long lastEventId;

    private TracingBatchStateEnum state;

    private Timestamp dateStarted;

    private Timestamp dateEnded;

}

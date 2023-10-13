package it.pagopa.interop.signalhub.updater.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Getter
@Setter
@Table("TRACING_BATCH")
@ToString
public class TracingBatchEntity {
    public static final String COLUMN_BATCH_ID = "batch_id";
    public static final String COLUMN_LAST_EVENT_ID = "last_event_id";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_DATE_STARTED = "tmst_started";
    public static final String COLUMN_DATE_ENDED = "tmst_ended";

    @Id
    @Column(COLUMN_BATCH_ID)
    private Long batchId;

    @Column(COLUMN_LAST_EVENT_ID)
    private Long lastEventId;

    @Column(COLUMN_STATE)
    private String state;

    @Column(COLUMN_DATE_STARTED)
    private Timestamp tmstInsert;

    @Column(COLUMN_DATE_ENDED)
    private Timestamp tmstEnded;

}

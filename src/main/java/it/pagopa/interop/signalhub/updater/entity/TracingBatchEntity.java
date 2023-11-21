package it.pagopa.interop.signalhub.updater.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Table(name = "TRACING_BATCH")
@Entity
@ToString
public class TracingBatchEntity {
    public static final String COLUMN_BATCH_ID = "batch_id";
    public static final String COLUMN_LAST_EVENT_ID = "last_event_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_DATE_CREATED = "tmst_created";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_BATCH_ID)
    private Long batchId;

    @Column(name = COLUMN_LAST_EVENT_ID)
    private Long lastEventId;

    @Column(name = COLUMN_TYPE)
    private String type;

    @Column(name = COLUMN_STATE)
    private String state;

    @Column(name = COLUMN_DATE_CREATED)
    private Timestamp tmstCreated;

    @PrePersist
    public void prePersist(){
        this.tmstCreated = Timestamp.from(Instant.now());
    }
}
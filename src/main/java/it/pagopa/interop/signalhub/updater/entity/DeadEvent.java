package it.pagopa.interop.signalhub.updater.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.Instant;


@Getter
@Setter
@ToString
@Entity
@Table(name = "DEAD_EVENT")
public class DeadEvent {
    public static final String COLUMN_EVENT_TMP_ID = "event_tmp_id";
    public static final String COLUMN_TMST_INSERT = "tmst_insert";
    public static final String COLUMN_ERROR_REASON = "error_reason";
    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_EVENT_TYPE = "event_type";
    public static final String COLUMN_OBJECT_TYPE = "object_type";
    public static final String COLUMN_ESERVICE_ID = "eservice_id";
    public static final String COLUMN_AGREEMENT_ID = "agreement_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_EVENT_TMP_ID)
    private Long eventTmpId;
    @Column(name = COLUMN_TMST_INSERT)
    private Timestamp tmstInsert;
    @Column(name = COLUMN_ERROR_REASON)
    private String errorReason;
    @Column(name = COLUMN_EVENT_ID)
    private Long eventId;
    @Column(name = COLUMN_EVENT_TYPE)
    private String eventType;
    @Column(name = COLUMN_OBJECT_TYPE)
    private String objectType;
    @Column(name = COLUMN_ESERVICE_ID)
    private String eserviceId;
    @Column(name = COLUMN_AGREEMENT_ID)
    private String agreementId;
    @PrePersist
    public void prePersist(){
        this.setTmstInsert(Timestamp.from(Instant.now()));
    }
}
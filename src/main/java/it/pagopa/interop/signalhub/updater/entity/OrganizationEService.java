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
@Table(name = "ESERVICE")
@IdClass(OrganizationKey.class)
public class OrganizationEService {
    public static final String COLUMN_ESERVICE_ID = "eservice_id";
    public static final String COLUMN_PRODUCER_ID = "producer_id";
    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_DATE_INSERT = "tmst_insert";
    public static final String COLUMN_DATE_UPDATE = "tmst_last_edit";

    @Id
    @Column(name = COLUMN_ESERVICE_ID)
    private String eserviceId;

    @Column(name = COLUMN_EVENT_ID)
    private Long eventId;

    @Id
    @Column(name = COLUMN_PRODUCER_ID)
    private String producerId;

    @Column(name = COLUMN_STATE)
    private String state;

    @Column(name = COLUMN_DATE_INSERT)
    private Timestamp tmstInsert;

    @Column(name = COLUMN_DATE_UPDATE)
    private Timestamp tmstLastEdit;

    @PrePersist
    public void prePersist(){
        this.setTmstInsert(Timestamp.from(Instant.now()));
    }

    @PreUpdate
    public void preUpdate(){
        this.setTmstLastEdit(Timestamp.from(Instant.now()));
    }

}

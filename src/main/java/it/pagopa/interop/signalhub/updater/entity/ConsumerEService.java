package it.pagopa.interop.signalhub.updater.entity;


import jakarta.persistence.IdClass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.query.Update;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Table("CONSUMER_ESERVICE")
@ToString
@IdClass(ConsumerKey.class)
public class ConsumerEService {
    public static final String COLUMN_ESERVICE_ID = "eservice_id";
    public static final String COLUMN_CONSUMER_ID = "consumer_id";
    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_DATE_INSERT = "tmst_insert";
    public static final String COLUMN_DATE_UPDATE = "tmst_last_edit";

    @Column(COLUMN_ESERVICE_ID)
    private String eserviceId;

    @Column(COLUMN_CONSUMER_ID)
    private String consumerId;

    @Column(COLUMN_STATE)
    private String state;

    @Column(COLUMN_EVENT_ID)
    private Long eventId;

    @Column(COLUMN_DATE_INSERT)
    private Timestamp tmstInsert;

    @Column(COLUMN_DATE_UPDATE)
    private Timestamp tmstLastEdit;

}

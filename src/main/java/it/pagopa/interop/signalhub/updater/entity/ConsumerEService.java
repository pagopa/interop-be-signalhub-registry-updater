package it.pagopa.interop.signalhub.updater.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@Table
@ToString
public class ConsumerEService {
    @Id
    @Column("eservice_id")
    private String eserviceId;
    @Column("consumer_id")
    private String consumerId;
    @Column("state")
    private String state;
    @Column("tmst_insert")
    private Instant dateInsert;
    @Column("tmst_last_edit")
    private Instant dateLastEdit;

}

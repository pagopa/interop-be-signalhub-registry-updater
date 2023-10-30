package it.pagopa.interop.signalhub.updater.cache.model;


import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("eservices")
public class OrganizationEServiceCache implements Serializable {
    private String eserviceId;
    private String producerId;
    private String descriptorId;
    private String state;
    private Timestamp tmstInsert;
    private Timestamp tmstLastEdit;
}

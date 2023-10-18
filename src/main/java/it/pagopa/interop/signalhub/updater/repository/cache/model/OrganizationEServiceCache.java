package it.pagopa.interop.signalhub.updater.repository.cache.model;


import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.sql.Timestamp;


@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("eservices")
public class OrganizationEServiceCache {
    private String eserviceId;
    private String producerId;
    private String state;
    private Timestamp tmstInsert;
    private Timestamp tmstLastEdit;
}

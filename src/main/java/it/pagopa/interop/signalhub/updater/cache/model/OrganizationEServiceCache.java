package it.pagopa.interop.signalhub.updater.cache.model;


import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;


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
    private String descriptorId;
    private Timestamp tmstInsert;
    private Timestamp tmstLastEdit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationEServiceCache that = (OrganizationEServiceCache) o;
        return Objects.equals(eserviceId, that.eserviceId) && Objects.equals(producerId, that.producerId) && Objects.equals(descriptorId, that.descriptorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eserviceId, producerId, descriptorId);
    }

}

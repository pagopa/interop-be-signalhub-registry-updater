package it.pagopa.interop.signalhub.updater.repository.cache.model;


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
@RedisHash("consumer_eservice")
public class ConsumerEServiceCache implements Serializable {
    private String eserviceId;
    private String consumerId;
    private String descriptorId;
    private String state;
    private Timestamp tmstInsert;
    private Timestamp tmstLastEdit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumerEServiceCache that = (ConsumerEServiceCache) o;
        return Objects.equals(eserviceId, that.eserviceId) && Objects.equals(consumerId, that.consumerId) && Objects.equals(descriptorId, that.descriptorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eserviceId, consumerId, descriptorId);
    }

}

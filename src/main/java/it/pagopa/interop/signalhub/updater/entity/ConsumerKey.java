package it.pagopa.interop.signalhub.updater.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class ConsumerKey implements Serializable {
    private String eserviceId;
    private String consumerId;
    private String descriptorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumerKey that = (ConsumerKey) o;
        return Objects.equals(eserviceId, that.eserviceId) && Objects.equals(consumerId, that.consumerId) && Objects.equals(descriptorId, that.descriptorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eserviceId, consumerId, descriptorId);
    }
}

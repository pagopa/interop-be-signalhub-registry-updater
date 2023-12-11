package it.pagopa.interop.signalhub.updater.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class OrganizationKey implements Serializable {
    private String eserviceId;
    private String producerId;
    private String descriptorId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationKey that = (OrganizationKey) o;
        return Objects.equals(eserviceId, that.eserviceId) && Objects.equals(producerId, that.producerId) && Objects.equals(descriptorId, that.descriptorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eserviceId, producerId, descriptorId);
    }
}
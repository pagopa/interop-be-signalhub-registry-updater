package it.pagopa.interop.signalhub.updater.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrganizationKey implements Serializable {
    private String eserviceId;
    private String producerId;
    private String descriptorId;
}
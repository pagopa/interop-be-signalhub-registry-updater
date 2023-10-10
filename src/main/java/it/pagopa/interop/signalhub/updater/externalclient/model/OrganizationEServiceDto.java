package it.pagopa.interop.signalhub.updater.externalclient.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrganizationEServiceDto {
    private String eserviceId;
    private String producerId;
    private String state;
}

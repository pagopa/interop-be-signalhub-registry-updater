package it.pagopa.interop.signalhub.updater.externalClient.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ConsumerEServiceDto {
    private String eserviceId;
    private String consumerId;
    private String state;
}

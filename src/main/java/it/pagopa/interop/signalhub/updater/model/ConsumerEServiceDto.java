package it.pagopa.interop.signalhub.updater.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;


@Getter
@Setter
@ToString
public class ConsumerEServiceDto {
    private Long eventId;
    private String eserviceId;
    private String producerId;
    private String consumerId;
    private String agreementId;
    private String descriptorId;
    private String state;


    public boolean isNull(){
        return StringUtils.isBlank(eserviceId) ||  StringUtils.isBlank(consumerId) ||  StringUtils.isBlank(state);
    }
}
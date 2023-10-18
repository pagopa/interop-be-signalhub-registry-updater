package it.pagopa.interop.signalhub.updater.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString
public class OrganizationEServiceDto {
    private Long eventId;
    private String eserviceId;
    private String producerId;
    private String state;

    public boolean isNull(){
        return StringUtils.isBlank(eserviceId) ||  StringUtils.isBlank(producerId) ||  StringUtils.isBlank(state);
    }

}

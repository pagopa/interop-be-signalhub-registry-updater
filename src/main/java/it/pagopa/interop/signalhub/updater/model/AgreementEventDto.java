package it.pagopa.interop.signalhub.updater.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class AgreementEventDto extends EventDto {
    private String agreementId;


    @Override
    public int hashCode() {
        return Objects.hashCode(agreementId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AgreementEventDto other){
            return this.agreementId.equals(other.agreementId)
                    && super.getObjectType().equals(other.getObjectType());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AgreementEventDto { ");
        sb.append("eventId: ").append(getEventId());
        sb.append(", agreementId: ").append(agreementId);
        sb.append(" }");
        return sb.toString();
    }
}
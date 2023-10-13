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
            return this.agreementId.equals(other.agreementId);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AgreementEventDto {\n");
        sb.append("    eventId: ").append(getEventId()).append("\n");
        sb.append("    agreementId: ").append(agreementId).append("\n");
        sb.append("}");
        return sb.toString();
    }
}

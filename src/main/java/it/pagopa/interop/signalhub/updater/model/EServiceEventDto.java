package it.pagopa.interop.signalhub.updater.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Objects;


@Getter
@Setter
public class EServiceEventDto extends EventDto {
    private String eServiceId;


    @Override
    public int hashCode() {
        return Objects.hashCode(eServiceId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EServiceEventDto other){
            return this.eServiceId.equals(other.eServiceId) && super.getDescriptorId().equals(other.getDescriptorId())
                    && super.getObjectType().equals(other.getObjectType());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EServiceEventDto {\n");
        sb.append("    eventId: ").append(getEventId()).append("\n");
        sb.append("    eServiceId: ").append(eServiceId).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
package it.pagopa.interop.signalhub.updater.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EventDto {
    private Long eventId;
    private String objectType;
    private String eventType;
    private String descriptorId;


    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract String toString();
}
package it.pagopa.interop.signalhub.updater.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class EventsDto {
    private List<EventDto> events;
    private Long lastEventId;
}

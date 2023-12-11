package it.pagopa.interop.signalhub.updater.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class EventsDto {
    private List<EventDto> events = new ArrayList<>();
    private Long lastEventId;
}

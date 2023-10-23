package it.pagopa.interop.signalhub.updater.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class EventsDtoTest {
    private List<EventDto> events;
    private Long lastEventId;

    @BeforeEach
    void preTest(){
        this.setUp();
    }


    @Test
    void setGetTest() {
        EventsDto eventsDto = new EventsDto();
        Assertions.assertNotNull(eventsDto);

        eventsDto.setEvents(events);
        Assertions.assertEquals(events, eventsDto.getEvents());

        eventsDto.setLastEventId(lastEventId);
        Assertions.assertEquals(lastEventId, eventsDto.getLastEventId());
    }

    private void setUp() {
        this.events = new ArrayList<>();
        this.lastEventId = 0L;
    }
}

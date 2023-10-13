package it.pagopa.interop.signalhub.updater.utility;

import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Event;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Events;
import it.pagopa.interop.signalhub.updater.model.EServiceEventDTO;
import it.pagopa.interop.signalhub.updater.model.EventDto;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;

import static it.pagopa.interop.signalhub.updater.utility.Const.AGREEMENT_EVENT;
import static it.pagopa.interop.signalhub.updater.utility.Const.ESERVICE_EVENT;

@Slf4j
public class Predicates {

    private Predicates(){}


    public static Predicate<Event> isAgreementOrEServiceEvent() { return event ->
            (event.getObjectType().equals(ESERVICE_EVENT) || event.getObjectType().equals(AGREEMENT_EVENT));
    }

    public static Predicate<Events> isEventsNotEmpty() {
        return events -> (!events.getEvents().isEmpty());
    }


    public static Predicate<EventDto> isEServiceEvent() {
        return event -> {
            log.info("Verifico object type: ESERVICE ");
            return (event instanceof EServiceEventDTO);
        };
    }


}

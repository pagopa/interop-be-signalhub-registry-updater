package it.pagopa.interop.signalhub.updater.controller;

import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Event;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Events;
import reactor.core.publisher.Flux;
import java.util.List;
import java.util.function.Predicate;


public class AutoUpdaterUtils {
    public static final String AGREEMENT_EVENT = "AGREEMENT";
    public static final String ESERVICE_EVENT = "ESERVICE";
    public static final String AGREEMENT_KEY_ID = "agreementId";
    public static final String ESERVICE_KEY_ID = "eServiceId";


    private AutoUpdaterUtils() { }

    public static Predicate<Events> isEventsEmpty() { return events -> (events.getEvents().isEmpty());}
    public static Predicate<Event> isAgreementEvent() { return event -> (event.getObjectType().equals(AGREEMENT_EVENT));}
    public static Predicate<Event> isEServiceEvent() { return event -> (event.getObjectType().equals(ESERVICE_EVENT));}
    public static Predicate<Event> isNotAgreementAndEServiceEvent() { return event ->
            !(event.getObjectType().equals(ESERVICE_EVENT) && event.getObjectType().equals(AGREEMENT_EVENT));
    }
    public static Flux<Event> monoToFlux(List<Event> eventList) {
        return Flux.fromIterable(eventList);
    }
}
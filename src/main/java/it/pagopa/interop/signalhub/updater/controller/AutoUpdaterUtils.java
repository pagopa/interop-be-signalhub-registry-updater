package it.pagopa.interop.signalhub.updater.controller;

import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Event;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Events;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import java.util.List;
import java.util.function.Predicate;


@Slf4j

public class AutoUpdaterUtils {
    public static final String AGREEMENT_EVENT = "AGREEMENT";
    public static final String ESERVICE_EVENT = "ESERVICE";
    public static final String AGREEMENT_KEY_ID = "agreementId";
    public static final String ESERVICE_KEY_ID = "eServiceId";


    private AutoUpdaterUtils() { }

    public static Predicate<Events> isEventsNotEmpty() { return events -> (!events.getEvents().isEmpty());}

    public static Predicate<Event> isEServiceEvent() { return event -> {
        log.info("Verifico object type: ESERVICE == {}", event.getObjectType());
        return (event.getObjectType().equals(ESERVICE_EVENT));
        };
    }

    public static Predicate<Event> isAgreementOrEServiceEvent() { return event ->
            (event.getObjectType().equals(ESERVICE_EVENT) || event.getObjectType().equals(AGREEMENT_EVENT));
    }

}
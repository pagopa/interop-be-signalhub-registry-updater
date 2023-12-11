package it.pagopa.interop.signalhub.updater.utility;

import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Event;
import it.pagopa.interop.signalhub.updater.model.EServiceEventDto;
import it.pagopa.interop.signalhub.updater.model.EventDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.function.Predicate;

import static it.pagopa.interop.signalhub.updater.utility.Const.*;

@Slf4j
public class Predicates {

    private Predicates(){}


    public static Predicate<Event> isAgreementOrEServiceEvent() { return event ->
            (event.getObjectType().equals(ESERVICE_EVENT) || event.getObjectType().equals(AGREEMENT_EVENT));
    }

    public static Predicate<EventDto> isEServiceEvent() {
        return event -> {
            log.info("Verifico object type: {} ", (event instanceof EServiceEventDto) ? "ESERVICE" : "AGREEMENT");
            return (event instanceof EServiceEventDto);
        };
    }


    public static boolean isCorrectApplicationType(Environment envs) {
        if (envs.containsProperty(APPLICATION_TYPE_ARG)){
            String applicationType = envs.getProperty(APPLICATION_TYPE_ARG);
            return StringUtils.equals(ESERVICE_EVENT, applicationType) || StringUtils.equals(AGREEMENT_EVENT, applicationType);
        }
        return false;
    }

}

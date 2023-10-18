package it.pagopa.interop.signalhub.updater.service;

import it.pagopa.interop.signalhub.updater.entity.DeadEvent;
import it.pagopa.interop.signalhub.updater.model.EventDto;

public interface DeadEventService {
    DeadEvent saveDeadEvent(EventDto eventDto);
}
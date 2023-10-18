package it.pagopa.interop.signalhub.updater.mapper;

import it.pagopa.interop.signalhub.updater.entity.DeadEvent;
import it.pagopa.interop.signalhub.updater.model.EventDto;

public interface DeadEventMapper {
    DeadEvent toDeadEvent(EventDto eventDto);
}

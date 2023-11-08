package it.pagopa.interop.signalhub.updater.service;

import it.pagopa.interop.signalhub.updater.model.TracingBatchDto;
import it.pagopa.interop.signalhub.updater.model.TracingBatchStateEnum;


public interface TracingBatchService {
    Long getLastEventIdByTracingBatch();
    TracingBatchDto terminateTracingBatch(TracingBatchStateEnum stateEnum, Long eventId);
    Integer countBatchInErrorWithLastEventId(Long lastEventId);
}

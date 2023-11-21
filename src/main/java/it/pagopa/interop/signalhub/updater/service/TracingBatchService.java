package it.pagopa.interop.signalhub.updater.service;

import it.pagopa.interop.signalhub.updater.model.TracingBatchDto;
import it.pagopa.interop.signalhub.updater.model.TracingBatchStateEnum;


public interface TracingBatchService {
    Long getLastEventIdByTracingBatchAndType(String type);
    TracingBatchDto terminateTracingBatch(TracingBatchStateEnum stateEnum, Long eventId, String type);
    Integer countBatchInErrorWithLastEventIdAndType(Long lastEventId, String type);
}

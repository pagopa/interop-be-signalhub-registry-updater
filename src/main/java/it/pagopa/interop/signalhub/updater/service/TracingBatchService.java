package it.pagopa.interop.signalhub.updater.service;

import it.pagopa.interop.signalhub.updater.model.TracingBatchDto;
import it.pagopa.interop.signalhub.updater.model.TracingBatchStateEnum;

public interface TracingBatchService {

    TracingBatchDto checkAndCreateTracingBatch();

    TracingBatchDto terminateTracingBatch(Long batchId, TracingBatchStateEnum stateEnum, Long lastEventId);

    Integer countBatchInErrorWithLastEventId(Long lastEventId);


}

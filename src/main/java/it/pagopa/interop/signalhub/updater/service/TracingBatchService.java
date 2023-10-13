package it.pagopa.interop.signalhub.updater.service;

import it.pagopa.interop.signalhub.updater.model.TracingBatchDto;
import it.pagopa.interop.signalhub.updater.model.TracingBatchStateEnum;
import reactor.core.publisher.Mono;

public interface TracingBatchService {

    Mono<Boolean> alreadyExistBatchInProgress();

    Mono<Integer> countBatchInErrorWithLastEventId(Long lastEventId);

    Mono<Long> getLastEventId();

    Mono<TracingBatchDto> createTracingBatch();

    Mono<TracingBatchDto> terminateTracingBatch(Long batchId, TracingBatchStateEnum stateEnum, Long lastEventId);

}

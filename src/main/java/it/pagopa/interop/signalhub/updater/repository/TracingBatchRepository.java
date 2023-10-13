package it.pagopa.interop.signalhub.updater.repository;

import it.pagopa.interop.signalhub.updater.entity.TracingBatchEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TracingBatchRepository extends R2dbcRepository<TracingBatchEntity, Long> {


    @Query("SELECT * FROM TRACING_BATCH WHERE state = :state order by last_event_id desc limit 1")
    Mono<TracingBatchEntity> findByStateProgressAndLastEventIdMax(String state);

    @Query("select * from TRACING_BATCH tb where last_event_id =  (select MAX(last_event_id) from TRACING_BATCH) order by tmst_started desc")
    Flux<TracingBatchEntity> findByStateAndLastEventIdMax();

    @Query("SELECT * FROM TRACING_BATCH WHERE state = :state AND last_event_id = :lastEventId")
    Flux<TracingBatchEntity> findAllStateEndedWithErrorAndLastEventId(String state, Long lastEventId);

}

package it.pagopa.interop.signalhub.updater.repository;

import it.pagopa.interop.signalhub.updater.entity.TracingBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TracingBatchRepository extends JpaRepository<TracingBatchEntity, Long> {


    @Query("select trace from TracingBatchEntity trace where trace.state = :state order by trace.lastEventId desc limit 1")
    Optional<TracingBatchEntity> findByStateProgressAndLastEventIdMax(String state);

    @Query("select trace from TracingBatchEntity trace where trace.lastEventId = (select MAX(t.lastEventId) from TracingBatchEntity t) order by trace.tmstInsert desc" )
    List<TracingBatchEntity> findByStateAndLastEventIdMax();

    @Query("SELECT trace FROM TracingBatchEntity trace WHERE trace.state = :state AND trace.lastEventId = :lastEventId")
    List<TracingBatchEntity> findAllStateEndedWithErrorAndLastEventId(String state, Long lastEventId);

}

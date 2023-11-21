package it.pagopa.interop.signalhub.updater.repository;

import it.pagopa.interop.signalhub.updater.entity.TracingBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TracingBatchRepository extends JpaRepository<TracingBatchEntity, Long> {
    @Query("select trace from TracingBatchEntity trace where trace.lastEventId = (select MAX(t.lastEventId) from TracingBatchEntity t where t.type = :type) order by trace.tmstCreated desc" )
    List<TracingBatchEntity> findByStateAndLastEventIdMaxAndType(String type);

    @Query("SELECT trace FROM TracingBatchEntity trace WHERE trace.state = :state AND trace.lastEventId = :lastEventId AND trace.type = :type")
    List<TracingBatchEntity> findAllStateEndedWithErrorAndLastEventIdAndType(String state, Long lastEventId, String type);
}
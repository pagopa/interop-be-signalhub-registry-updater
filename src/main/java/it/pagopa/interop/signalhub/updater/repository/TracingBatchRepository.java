package it.pagopa.interop.signalhub.updater.repository;

import it.pagopa.interop.signalhub.updater.entity.TracingBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TracingBatchRepository extends JpaRepository<TracingBatchEntity, Long> {
    @Query("select trace from TracingBatchEntity trace where trace.lastEventId = (select MAX(t.lastEventId) from TracingBatchEntity t where t.type = :type) order by trace.tmstCreated desc" )
    List<TracingBatchEntity> findLatestByType(String type);

    @Query("SELECT trace FROM TracingBatchEntity trace WHERE trace.state = it.pagopa.interop.signalhub.updater.model.TracingBatchStateEnum.ENDED_WITH_ERROR AND trace.lastEventId = :lastEventId AND trace.type = :type")
    List<TracingBatchEntity> findAllByStateEndedWithErrorAndLastEventIdAndType(Long lastEventId, String type);
}
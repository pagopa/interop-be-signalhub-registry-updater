package it.pagopa.interop.signalhub.updater.repository;


import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import it.pagopa.interop.signalhub.updater.entity.OrganizationKey;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;


@Repository
public interface OrganizationEserviceRepository extends R2dbcRepository<OrganizationEService, OrganizationKey> {
    @Query("SELECT * FROM ESERVICE WHERE eservice_id = :eserviceId AND producer_id = :producerId")
    Mono<OrganizationEService> findByEserviceIdAndProducerId(String eserviceId, String producerId);


    @Query("UPDATE ESERVICE SET tmst_last_edit= :lastEdit, state = :state, event_id = :eventId WHERE eservice_id = :eserviceId AND producer_id = :producerId AND event_id < :eventId")
    Mono<OrganizationEService> updateByEserviceIdAndProducerIdAndEventId(
            String eserviceId, String producerId, String state, Long eventId, Timestamp lastEdit
    );
}

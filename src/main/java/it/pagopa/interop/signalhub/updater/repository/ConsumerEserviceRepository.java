package it.pagopa.interop.signalhub.updater.repository;


import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import it.pagopa.interop.signalhub.updater.entity.ConsumerKey;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;


@Repository
public interface ConsumerEserviceRepository extends R2dbcRepository<ConsumerEService, ConsumerKey> {


    @Query("UPDATE CONSUMER_ESERVICE SET tmst_last_edit = :lastEdit, state = :state, event_id = :eventId WHERE eservice_id = :eserviceId AND consumer_id = :consumerId AND event_id < :eventId")
    Mono<ConsumerEService> updateByEserviceIdAndConsumerIdAndEventId(
            String eserviceId, String consumerId, String state, Long eventId, Timestamp lastEdit
    );

}

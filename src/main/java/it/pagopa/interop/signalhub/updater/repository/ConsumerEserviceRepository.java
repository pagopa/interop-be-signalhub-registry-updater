package it.pagopa.interop.signalhub.updater.repository;


import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface ConsumerEserviceRepository {
    Mono<ConsumerEService> saveAndFlush(String eserviceId, String consumerId, String state);
}

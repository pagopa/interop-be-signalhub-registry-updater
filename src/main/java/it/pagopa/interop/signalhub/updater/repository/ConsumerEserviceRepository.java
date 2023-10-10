package it.pagopa.interop.signalhub.updater.repository;


import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import reactor.core.publisher.Mono;


public interface ConsumerEserviceRepository {
    Mono<ConsumerEService> saveAndFlush(String eserviceId, String consumerId, String state);
}

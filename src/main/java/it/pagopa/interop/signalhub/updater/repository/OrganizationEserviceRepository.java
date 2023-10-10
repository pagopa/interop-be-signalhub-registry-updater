package it.pagopa.interop.signalhub.updater.repository;


import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import reactor.core.publisher.Mono;


public interface OrganizationEserviceRepository {
    Mono<OrganizationEService> saveAndFlush(String eserviceId, String producerId, String state);
}

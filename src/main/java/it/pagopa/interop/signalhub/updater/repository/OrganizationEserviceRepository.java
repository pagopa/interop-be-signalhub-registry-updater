package it.pagopa.interop.signalhub.updater.repository;


import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface OrganizationEserviceRepository {
    Mono<OrganizationEService> saveAndFlush(String eserviceId, String producerId, String state);
}

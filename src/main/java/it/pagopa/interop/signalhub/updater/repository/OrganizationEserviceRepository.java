package it.pagopa.interop.signalhub.updater.repository;


import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface OrganizationEserviceRepository extends R2dbcRepository<OrganizationEService, String> {


    Flux<OrganizationEService> findAll();
}

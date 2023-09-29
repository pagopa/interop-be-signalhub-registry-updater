package it.pagopa.interop.signalhub.updater.repository;


import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface ConsumerEserviceRepository extends R2dbcRepository<ConsumerEService, String> {


    Flux<ConsumerEService> findAll();
}

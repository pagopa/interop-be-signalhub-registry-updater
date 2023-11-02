package it.pagopa.interop.signalhub.updater.repository;


import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ConsumerEserviceRepository extends JpaRepository<ConsumerEService, String> {


    @Query("select consumer from ConsumerEService consumer where consumer.agreementId = :agreementId")
    Optional<ConsumerEService> findByEserviceIdAndConsumerIdAndDescriptorId(String agreementId);


}
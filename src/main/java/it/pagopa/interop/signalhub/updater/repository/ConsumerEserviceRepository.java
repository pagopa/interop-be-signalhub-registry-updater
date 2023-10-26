package it.pagopa.interop.signalhub.updater.repository;


import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import it.pagopa.interop.signalhub.updater.entity.ConsumerKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ConsumerEserviceRepository extends JpaRepository<ConsumerEService, ConsumerKey> {
    @Query("select consumer from ConsumerEService consumer where consumer.eserviceId = :eserviceId AND consumer.consumerId = :consumerId  AND consumer.descriptorId = :descriptorId")
    Optional<ConsumerEService> findByEserviceIdAndConsumerIdAndDescriptorId(String eserviceId, String consumerId, String descriptorId);
}
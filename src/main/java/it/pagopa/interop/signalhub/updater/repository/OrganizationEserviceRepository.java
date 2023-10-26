package it.pagopa.interop.signalhub.updater.repository;


import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import it.pagopa.interop.signalhub.updater.entity.OrganizationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OrganizationEserviceRepository extends JpaRepository<OrganizationEService, OrganizationKey> {
    @Query("SELECT eservice FROM OrganizationEService eservice WHERE eservice.eserviceId = :eServiceId AND eservice.producerId = :producerId AND eservice.descriptorId = :descriptorId")
    Optional<OrganizationEService> findByEserviceIdAndProducerIdAndDescriptorId(String eServiceId, String producerId, String descriptorId);
}
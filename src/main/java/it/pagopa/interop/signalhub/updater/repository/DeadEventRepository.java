package it.pagopa.interop.signalhub.updater.repository;

import it.pagopa.interop.signalhub.updater.entity.DeadEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeadEventRepository extends JpaRepository<DeadEvent, Long> {
    DeadEvent saveAndFlush(DeadEvent deadEvent);
}

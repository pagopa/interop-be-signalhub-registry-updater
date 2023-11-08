package it.pagopa.interop.signalhub.updater.service;


import it.pagopa.interop.signalhub.updater.model.EServiceEventDTO;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;

public interface OrganizationService {

    OrganizationEServiceDto updateOrganizationEService(EServiceEventDTO eServiceEventDTO);

    OrganizationEServiceDto checkAndUpdate(String eserviceId, String producerId, Long eventId);

}
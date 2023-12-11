package it.pagopa.interop.signalhub.updater.service;


import it.pagopa.interop.signalhub.updater.model.EServiceEventDto;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;

public interface OrganizationService {
    OrganizationEServiceDto updateOrganizationEService(EServiceEventDto eServiceEventDTO);
    OrganizationEServiceDto checkAndUpdate(String eserviceId, String producerId, String descriptorId, Long eventId);
}
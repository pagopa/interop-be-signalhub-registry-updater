package it.pagopa.interop.signalhub.updater.service;

import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.model.EServiceDescriptorDto;
import it.pagopa.interop.signalhub.updater.model.EventsDto;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;

public interface InteropService {
    EventsDto getAgreementsAndEServices(Long lastEventId);
    ConsumerEServiceDto getConsumerEService(String agreementId, Long eventId);
    OrganizationEServiceDto getEService(String eserviceId, Long eventId);
    OrganizationEServiceDto getEServiceDescriptor(OrganizationEServiceDto organizationEServiceDto);
}
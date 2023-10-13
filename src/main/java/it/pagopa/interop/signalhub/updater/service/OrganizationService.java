package it.pagopa.interop.signalhub.updater.service;


import it.pagopa.interop.signalhub.updater.model.EServiceEventDTO;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import reactor.core.publisher.Mono;

public interface OrganizationService {

    Mono<OrganizationEServiceDto> updateOrganizationEService(EServiceEventDTO eServiceEventDTO);

    Mono<OrganizationEServiceDto> checkAndUpdate(String eserviceId, String producerId);

}
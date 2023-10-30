package it.pagopa.interop.signalhub.updater.mapper;

import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.EServiceDescriptor;
import it.pagopa.interop.signalhub.updater.model.EServiceDescriptorDto;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;

public interface EServiceDescriptorMapper {
    EServiceDescriptorDto fromEServiceDescriptorToEServiceDescriptorDto(EServiceDescriptor eServiceDescriptor);
    OrganizationEServiceDto fromEServiceDescriptorDtoToOrganizationEServiceDto(EServiceDescriptorDto eServiceDescriptorDto, OrganizationEServiceDto organizationEServiceDto);
}

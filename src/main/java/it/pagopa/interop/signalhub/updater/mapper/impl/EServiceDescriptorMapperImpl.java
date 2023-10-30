package it.pagopa.interop.signalhub.updater.mapper.impl;

import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.EServiceDescriptor;
import it.pagopa.interop.signalhub.updater.mapper.EServiceDescriptorMapper;
import it.pagopa.interop.signalhub.updater.model.EServiceDescriptorDto;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import org.springframework.stereotype.Component;

@Component
public class EServiceDescriptorMapperImpl implements EServiceDescriptorMapper {
    public EServiceDescriptorMapperImpl() {}

    @Override
    public EServiceDescriptorDto fromEServiceDescriptorToEServiceDescriptorDto(EServiceDescriptor eServiceDescriptor) {
        if (eServiceDescriptor == null) {
            return null;
        }
        EServiceDescriptorDto eServiceDescriptorDto = new EServiceDescriptorDto();
        eServiceDescriptorDto.setState(eServiceDescriptor.getState().toString());
        return eServiceDescriptorDto;
    }

    @Override
    public OrganizationEServiceDto fromEServiceDescriptorDtoToOrganizationEServiceDto(EServiceDescriptorDto eServiceDescriptorDto, OrganizationEServiceDto organizationEServiceDto) {
        if (eServiceDescriptorDto == null && organizationEServiceDto == null) {
            return null;
        }
        organizationEServiceDto.setState(eServiceDescriptorDto.getState());
        return organizationEServiceDto;
    }
}

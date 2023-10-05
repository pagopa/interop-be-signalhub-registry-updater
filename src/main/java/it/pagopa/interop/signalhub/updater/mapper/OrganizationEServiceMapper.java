package it.pagopa.interop.signalhub.updater.mapper;

import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import it.pagopa.interop.signalhub.updater.externalClient.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.EService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrganizationEServiceMapper {
    @Mappings ({
            @Mapping(target = "eserviceId", source = "id"),
            @Mapping(target = "producerId", source = "producer.id" ),
            @Mapping(target = "state", source = "state" ),
    })
    OrganizationEServiceDto fromEServiceToOrganizationEServiceDto(EService eService);
    OrganizationEService fromOrganizationEServiceDtoToOrganizationEService(OrganizationEServiceDto organizationEServiceDto);
    OrganizationEServiceDto fromOrganizationEServiceToOrganizationEServiceDto(OrganizationEService organizationEService);
}

package it.pagopa.interop.signalhub.updater.mapper;

import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.EService;
import it.pagopa.interop.signalhub.updater.repository.cache.model.OrganizationEServiceCache;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrganizationEServiceMapper {
    @Mappings ({
            @Mapping(target = "eserviceId", source = "eService.id"),
            @Mapping(target = "producerId", source = "eService.producer.id" ),
            @Mapping(target = "state", source = "eService.state" ),
            @Mapping(target = "eventId", source = "eventId" ),
    })
    OrganizationEServiceDto fromEServiceToOrganizationEServiceDto(EService eService, Long eventId);


    OrganizationEServiceDto toDtoFromEntity(OrganizationEService entity);

    OrganizationEServiceCache toCacheFromEntity(OrganizationEService entity);


    @Mappings ({
            @Mapping(target = "eserviceId", source = "eserviceId"),
            @Mapping(target = "producerId", source = "producerId" ),
            @Mapping(target = "state", source = "state" ),
    })
    OrganizationEService toEntityFromProps(String eserviceId, String producerId, String state);

}

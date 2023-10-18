package it.pagopa.interop.signalhub.updater.mapper;

import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Agreement;
import it.pagopa.interop.signalhub.updater.repository.cache.model.ConsumerEServiceCache;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ConsumerEServiceMapper {
    ConsumerEServiceDto toConsumerEServiceDtoFromAgreement(Agreement agreement, Long eventId);

    ConsumerEServiceDto toDtoFromEntity(ConsumerEService entity);

    @Mappings({
            @Mapping(target = "eserviceId", source = "eserviceId"),
            @Mapping(target = "consumerId", source = "consumerId" ),
            @Mapping(target = "state", source = "state" ),
    })
    ConsumerEService toEntityFromProps(String eserviceId, String consumerId, String state);

    ConsumerEServiceCache toCacheFromEntity(ConsumerEService eService);

}

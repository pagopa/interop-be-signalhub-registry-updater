package it.pagopa.interop.signalhub.updater.mapper;

import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import it.pagopa.interop.signalhub.updater.externalClient.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Agreement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsumerEServiceMapper {
    ConsumerEServiceDto fromAgreementToConsumerEServiceDto(Agreement agreement);
    ConsumerEService fromConsumerEServiceDtoToConsumerEService(ConsumerEServiceDto consumerEServiceDto);
    ConsumerEServiceDto fromConsumerEServiceToConsumerEServiceDto(ConsumerEService consumerEService);
}

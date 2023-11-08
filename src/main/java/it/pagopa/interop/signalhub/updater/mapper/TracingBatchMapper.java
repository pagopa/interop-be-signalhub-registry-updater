package it.pagopa.interop.signalhub.updater.mapper;

import it.pagopa.interop.signalhub.updater.entity.TracingBatchEntity;
import it.pagopa.interop.signalhub.updater.model.TracingBatchDto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface TracingBatchMapper {

    TracingBatchDto toDto(TracingBatchEntity entity);


}

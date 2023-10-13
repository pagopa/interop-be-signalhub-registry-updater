package it.pagopa.interop.signalhub.updater.service.impl;


import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import it.pagopa.interop.signalhub.updater.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.AgreementEventDto;
import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.repository.ConsumerEserviceRepository;
import it.pagopa.interop.signalhub.updater.service.ConsumerService;
import it.pagopa.interop.signalhub.updater.service.InteropService;
import it.pagopa.interop.signalhub.updater.service.OrganizationService;
import it.pagopa.interop.signalhub.updater.utility.Functions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Instant;


@Slf4j
@Service
@AllArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {
    private final InteropService interopService;
    private final OrganizationService organizationService;
    private final ConsumerEserviceRepository consumerEserviceRepository;
    private final ConsumerEServiceMapper mapper;

    @Override
    public Mono<ConsumerEServiceDto> updateConsumer(AgreementEventDto agreementEventDto) {
        return this.interopService.getConsumerEservice(agreementEventDto.getAgreementId(), agreementEventDto.getEventId())
                .flatMap(this::checkAndUpdateEservice)
                .map(this::getInitialConsumerEService)
                .flatMap(this::save)
                .map(mapper::toDtoFromEntity);
    }

    private Mono<ConsumerEServiceDto> checkAndUpdateEservice(ConsumerEServiceDto dto){
        if (!dto.getState().equals("ACTIVE")) return Mono.just(dto);

        return this.organizationService.checkAndUpdate(dto.getEserviceId(), dto.getProducerId())
                .thenReturn(dto);
    }

    private Mono<ConsumerEService> save(ConsumerEService entity){
        return this.consumerEserviceRepository.save(entity)
                .onErrorResume(DuplicateKeyException.class, ex -> this.update(entity, entity.getState()));
    }


    private Mono<ConsumerEService> update(ConsumerEService entity, String state){
        return Mono.just(getEditConsumerEService(entity, state))
                .flatMap(toUpdated -> this.consumerEserviceRepository
                        .updateByEserviceIdAndConsumerIdAndEventId(toUpdated.getEserviceId(),
                                                                    toUpdated.getConsumerId(),
                                                                    toUpdated.getState(),
                                                                    toUpdated.getEventId(),
                                                                    toUpdated.getTmstLastEdit())
                );
    }

    private ConsumerEService getEditConsumerEService(ConsumerEService eService, String state){
        eService.setTmstLastEdit(Timestamp.from(Instant.now()));
        eService.setState(state);
        return eService;
    }

    private ConsumerEService getInitialConsumerEService(ConsumerEServiceDto consumerdto){
        ConsumerEService entity = mapper.toEntityFromProps(consumerdto.getEserviceId(), consumerdto.getConsumerId(), consumerdto.getState());
        entity.setTmstInsert(Timestamp.from(Instant.now()));
        entity.setTmstLastEdit(Timestamp.from(Instant.now()));
        entity.setEventId(consumerdto.getEventId());
        return entity;
    }

}

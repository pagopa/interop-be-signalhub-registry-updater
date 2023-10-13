package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import it.pagopa.interop.signalhub.updater.mapper.OrganizationEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.model.EServiceEventDTO;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.repository.OrganizationEserviceRepository;
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
public class OrganizationServiceImpl implements OrganizationService {
    private final InteropService interopService;
    private final OrganizationEserviceRepository repository;
    private final OrganizationEServiceMapper mapper;


    @Override
    public Mono<OrganizationEServiceDto> updateOrganizationEService(EServiceEventDTO eServiceEventDTO) {
        return this.interopService.getEservice(eServiceEventDTO.getEServiceId(),eServiceEventDTO.getEventId())
                .map(this::getInitialEService)
                .flatMap(this::save)
                .map(mapper::toDtoFromEntity)
                .onErrorResume(WebClientException.class, Functions.catchWebClientException());
    }

    @Override
    public Mono<OrganizationEServiceDto> checkAndUpdate(String eserviceId, String producerId) {
        return this.repository.findByEserviceIdAndProducerId(eserviceId, producerId)
                .map(mapper::toDtoFromEntity)
                .switchIfEmpty(Mono.defer(() -> {
                    EServiceEventDTO dto = new EServiceEventDTO();
                    dto.setEServiceId(eserviceId);
                    dto.setEventId(0L);
                    return updateOrganizationEService(dto);
                }));
    }

    private Mono<OrganizationEService> save(OrganizationEService entity){
        return this.repository.save(entity)
                .onErrorResume(DuplicateKeyException.class, ex -> this.update(entity, entity.getState()));
    }


    private Mono<OrganizationEService> update(OrganizationEService entity, String state){
        return Mono.just(getEditEService(entity, state))
                .flatMap(toUpdated -> this.repository
                        .updateByEserviceIdAndProducerIdAndEventId(toUpdated.getEserviceId(),
                                toUpdated.getProducerId(),
                                toUpdated.getState(),
                                toUpdated.getEventId(),
                                toUpdated.getTmstLastEdit())
                );
    }

    private OrganizationEService getEditEService(OrganizationEService entity, String state){
        entity.setTmstLastEdit(Timestamp.from(Instant.now()));
        entity.setState(state);
        return entity;
    }

    private OrganizationEService getInitialEService(OrganizationEServiceDto dto){
        OrganizationEService entity = mapper.toEntityFromProps(dto.getEserviceId(), dto.getProducerId(), dto.getState());
        entity.setTmstInsert(Timestamp.from(Instant.now()));
        entity.setEventId(dto.getEventId());
        return entity;
    }
}

package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.config.RegistryUpdaterProps;
import it.pagopa.interop.signalhub.updater.entity.TracingBatchEntity;
import it.pagopa.interop.signalhub.updater.mapper.TracingBatchMapper;
import it.pagopa.interop.signalhub.updater.model.TracingBatchDto;
import it.pagopa.interop.signalhub.updater.model.TracingBatchStateEnum;
import it.pagopa.interop.signalhub.updater.repository.TracingBatchRepository;
import it.pagopa.interop.signalhub.updater.service.TracingBatchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TracingBatchServiceImpl implements TracingBatchService {
    private final TracingBatchRepository repository;
    private final TracingBatchMapper mapper;
    private final RegistryUpdaterProps props;

    @Override
    public Mono<Boolean> alreadyExistBatchInProgress() {
        return repository.findByStateProgressAndLastEventIdMax(TracingBatchStateEnum.IN_PROGRESS.name())
                .map(entity -> Boolean.TRUE)
                .switchIfEmpty(Mono.just(Boolean.FALSE))
                .doOnNext(result -> log.info("There is {} a progressing batch", (result.equals(Boolean.TRUE) ? "" : "not")));
    }

    @Override
    public Mono<Integer> countBatchInErrorWithLastEventId(Long lastEventId) {
        return repository.findAllStateEndedWithErrorAndLastEventId(TracingBatchStateEnum.ENDED_WITH_ERROR.name(), lastEventId)
                .collectList()
                .map(List::size);

    }

    @Override
    public Mono<Long> getLastEventId() {
        return repository.findByStateAndLastEventIdMax()
                .collectList()
                .map(list -> {
                    if (list.isEmpty()) return 0L;
                    if (list.get(0).getState().equals(TracingBatchStateEnum.ENDED.name())){
                        return list.get(0).getLastEventId();
                    }
                    if (list.size() >= props.getAttemptEvent()){
                        return list.get(list.size()-1).getLastEventId()+1;
                    }
                    return list.get(0).getLastEventId();
                });
    }

    @Override
    public Mono<TracingBatchDto> createTracingBatch() {
        TracingBatchEntity initEntity = new TracingBatchEntity();
        initEntity.setState(TracingBatchStateEnum.IN_PROGRESS.name());
        initEntity.setTmstInsert(Timestamp.from(Instant.now()));
        return this.repository.save(initEntity).map(mapper::toDto);
    }

    @Override
    public Mono<TracingBatchDto> terminateTracingBatch(Long batchId, TracingBatchStateEnum stateEnum, Long lastEventId) {
        return this.repository.findById(batchId)
                .map(entity -> {
                    entity.setTmstEnded(Timestamp.from(Instant.now()));
                    entity.setLastEventId(lastEventId);
                    entity.setState(stateEnum.name());
                    return entity;
                })
                .flatMap(repository::save)
                .map(mapper::toDto);
    }
}

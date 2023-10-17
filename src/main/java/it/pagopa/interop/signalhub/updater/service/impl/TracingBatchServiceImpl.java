package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.config.RegistryUpdaterProps;
import it.pagopa.interop.signalhub.updater.entity.TracingBatchEntity;
import it.pagopa.interop.signalhub.updater.exception.PDNDBatchAlreadyExistException;
import it.pagopa.interop.signalhub.updater.exception.PDNDEntityNotFound;
import it.pagopa.interop.signalhub.updater.mapper.TracingBatchMapper;
import it.pagopa.interop.signalhub.updater.model.TracingBatchDto;
import it.pagopa.interop.signalhub.updater.model.TracingBatchStateEnum;
import it.pagopa.interop.signalhub.updater.repository.TracingBatchRepository;
import it.pagopa.interop.signalhub.updater.service.TracingBatchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public TracingBatchDto checkAndCreateTracingBatch() {
        boolean alreadyInProgress = alreadyExistBatchInProgress();
        log.info("{} a progress batch", (alreadyInProgress) ? "Have" : "Haven't");
        if (alreadyInProgress) throw new PDNDBatchAlreadyExistException();

        Long lastEventId = getLastEventId();
        log.info("Last event id traced is {}", lastEventId);
        if (lastEventId == null) lastEventId = 0L;

        TracingBatchEntity instantBatch = this.createTracingBatch();
        TracingBatchDto dto = mapper.toDto(instantBatch);
        dto.setLastEventId(lastEventId);
        return dto;
    }


    private boolean alreadyExistBatchInProgress() {
        log.info("Check if exist progress batch");
        return repository.findByStateProgressAndLastEventIdMax(TracingBatchStateEnum.IN_PROGRESS.name()).isPresent();
    }


    public Integer countBatchInErrorWithLastEventId(Long lastEventId) {
        log.info("Retrieve a number of batch in error with event Id {}", lastEventId);
        List<TracingBatchEntity> list = repository.findAllStateEndedWithErrorAndLastEventId(TracingBatchStateEnum.ENDED_WITH_ERROR.name(), lastEventId);
        if (list == null) return  0;
        log.info("{} batch in error with event id {}", list.size(), lastEventId);
        return list.size();
    }

    private Long getLastEventId() {
        List<TracingBatchEntity> list =  repository.findByStateAndLastEventIdMax();
        if (list.isEmpty()) return 0L;
        if (list.get(0).getState().equals(TracingBatchStateEnum.ENDED.name())){
            return list.get(0).getLastEventId();
        }
        if (list.size() >= props.getAttemptEvent()){
            return list.get(list.size()-1).getLastEventId()+1;
        }
        return list.get(0).getLastEventId();
    }

    private TracingBatchEntity createTracingBatch() {
        TracingBatchEntity initEntity = new TracingBatchEntity();
        initEntity.setState(TracingBatchStateEnum.IN_PROGRESS.name());
        log.debug("Create a tracing batch entity {}", initEntity);
        return this.repository.saveAndFlush(initEntity);
    }

    @Override
    public TracingBatchDto terminateTracingBatch(Long batchId, TracingBatchStateEnum stateEnum, Long lastEventId) {
        TracingBatchEntity finded = this.repository.findById(batchId)
                .orElseThrow(() -> new PDNDEntityNotFound("Batch entity not founded with ".concat(batchId.toString()).concat(" id")));

        finded.setTmstEnded(Timestamp.from(Instant.now()));
        finded.setLastEventId(lastEventId);
        finded.setState(stateEnum.name());
        finded = this.repository.saveAndFlush(finded);
        return mapper.toDto(finded);
    }


}

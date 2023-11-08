package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.config.RegistryUpdaterProps;
import it.pagopa.interop.signalhub.updater.entity.TracingBatchEntity;
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

    public Integer countBatchInErrorWithLastEventId(Long lastEventId) {
        log.info("Retrieve a number of batch in error with event Id {}", lastEventId);
        List<TracingBatchEntity> list = repository.findAllStateEndedWithErrorAndLastEventId(TracingBatchStateEnum.ENDED_WITH_ERROR.name(), lastEventId);
        if (list == null || list.isEmpty()) return  0;
        log.info("{} batch in error with event id {}", list.size(), lastEventId);
        return list.size();
    }

    public Long getLastEventIdByTracingBatch() {
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

    @Override
    public TracingBatchDto terminateTracingBatch(TracingBatchStateEnum stateEnum, Long eventId) {
        TracingBatchEntity tracingBatchEntity = new TracingBatchEntity();
        tracingBatchEntity.setTmstCreated(Timestamp.from(Instant.now()));
        tracingBatchEntity.setLastEventId(eventId-1);
        tracingBatchEntity.setState(stateEnum.name());
        tracingBatchEntity = this.repository.saveAndFlush(tracingBatchEntity);
        log.debug("Create a tracing batch entity {}", tracingBatchEntity);
        return mapper.toDto(tracingBatchEntity);
    }
}
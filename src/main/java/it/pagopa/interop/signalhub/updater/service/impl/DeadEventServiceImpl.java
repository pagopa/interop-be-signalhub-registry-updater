package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.config.RegistryUpdaterProps;
import it.pagopa.interop.signalhub.updater.entity.DeadEvent;
import it.pagopa.interop.signalhub.updater.mapper.impl.DeadEventMapperImpl;
import it.pagopa.interop.signalhub.updater.model.EventDto;
import it.pagopa.interop.signalhub.updater.repository.DeadEventRepository;
import it.pagopa.interop.signalhub.updater.service.DeadEventService;
import it.pagopa.interop.signalhub.updater.service.TracingBatchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class DeadEventServiceImpl implements DeadEventService {
    private TracingBatchService tracingBatchService;
    private RegistryUpdaterProps registryUpdaterProps;
    private DeadEventRepository deadEventRepository;
    private DeadEventMapperImpl deadEventMapperImpl;


    @Override
    public DeadEvent saveDeadEvent(EventDto eventDto) {
        Integer occurrence = tracingBatchService.countBatchInErrorWithLastEventId(eventDto.getEventId()-1);
        if((occurrence+1) >= registryUpdaterProps.getAttemptEvent()) {
            deadEventRepository.saveAndFlush(deadEventMapperImpl.toDeadEvent(eventDto));
        }
        return null;
    }
}
package it.pagopa.interop.signalhub.updater.controller;

import it.pagopa.interop.signalhub.updater.exception.PDNDBatchAlreadyExistException;
import it.pagopa.interop.signalhub.updater.exception.PDNDClientException;
import it.pagopa.interop.signalhub.updater.exception.PDNDNoEventsException;
import it.pagopa.interop.signalhub.updater.model.*;
import it.pagopa.interop.signalhub.updater.service.ConsumerService;
import it.pagopa.interop.signalhub.updater.service.InteropService;
import it.pagopa.interop.signalhub.updater.service.OrganizationService;
import it.pagopa.interop.signalhub.updater.service.TracingBatchService;
import it.pagopa.interop.signalhub.updater.utility.Predicates;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Component
@AllArgsConstructor
public class AutoUpdaterController {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final InteropService interopService;
    private final OrganizationService organizationService;
    private final ConsumerService consumerService;
    private final TracingBatchService tracingBatchService;



    @Scheduled(fixedRate = 40000)
    private void scheduleUpdater() {
        log.info("ScheduleUpdater Started: {}", dateTimeFormatter.format(LocalDateTime.now()));
        try {
            TracingBatchDto instanceTracingBatch = this.tracingBatchService.checkAndCreateTracingBatch();
            Long lastEventId = updateRecursiveFlow(instanceTracingBatch.getLastEventId(), instanceTracingBatch.getBatchId());
            tracingBatchService.terminateTracingBatch(instanceTracingBatch.getBatchId(), TracingBatchStateEnum.ENDED, lastEventId);
        } catch (PDNDBatchAlreadyExistException ex) {
            log.info(ex.getMessage());
        }
    }

    private Long updateRecursiveFlow(Long lastEventId, Long batchId) {
        try {
            EventsDto events = this.interopService.getAgreementsAndEServices(lastEventId);
            updateEvents(events.getEvents());
            return updateRecursiveFlow(events.getLastEventId(), batchId);
        } catch (PDNDNoEventsException ex) {
            return lastEventId;
        } catch (PDNDClientException ex) {
            tracingBatchService.terminateTracingBatch(batchId, TracingBatchStateEnum.ENDED_WITH_ERROR, ex.getEventId()-1);
            throw ex;
        }
    }

    private void updateEvents(List<EventDto> events){
        for (EventDto event : events){
            if (Predicates.isEServiceEvent().test(event)){
                organizationService.updateOrganizationEService((EServiceEventDTO) event);
            } else {
                consumerService.updateConsumer((AgreementEventDto) event);
            }
        }
    }

}
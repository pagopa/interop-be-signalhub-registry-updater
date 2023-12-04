package it.pagopa.interop.signalhub.updater.controller;

import it.pagopa.interop.signalhub.updater.exception.PDNDEventException;
import it.pagopa.interop.signalhub.updater.exception.PDNDConnectionResetException;
import it.pagopa.interop.signalhub.updater.exception.PDNDNoEventsException;
import it.pagopa.interop.signalhub.updater.model.*;
import it.pagopa.interop.signalhub.updater.service.*;
import it.pagopa.interop.signalhub.updater.utility.Predicates;
import it.pagopa.interop.signalhub.updater.utility.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;


@Slf4j
@Component
@AllArgsConstructor
public class AutoUpdaterController {
    private final InteropService interopService;
    private final OrganizationService organizationService;
    private final ConsumerService consumerService;
    private final TracingBatchService tracingBatchService;
    private final DeadEventService deadEventService;


    public void scheduleUpdater(String applicationType) {
        log.info("ScheduleUpdater of {} started at {}", applicationType, Utils.getFormatHour(Instant.now()));
        Long lastEventId = this.tracingBatchService.getLastEventIdByTracingBatchAndType(applicationType);
        lastEventId = updateRecursiveFlow(lastEventId, applicationType);
        tracingBatchService.terminateTracingBatch(TracingBatchStateEnum.ENDED, lastEventId+1, applicationType);
    }

    private Long updateRecursiveFlow(Long lastEventId, String type) {
        try {
            EventsDto events = this.interopService.getEventsByType(lastEventId, type);
            updateEvents(events.getEvents(), type);
            return updateRecursiveFlow(events.getLastEventId(), type);
        } catch (PDNDConnectionResetException ex) {
            tracingBatchService.terminateTracingBatch(TracingBatchStateEnum.ENDED, ex.getEventId(), type);
            throw ex;
        } catch (PDNDNoEventsException ex) {
            return lastEventId;
        } catch (PDNDEventException ex) {
            tracingBatchService.terminateTracingBatch(TracingBatchStateEnum.ENDED_WITH_ERROR, ex.getEventId(), type);
            throw ex;
        }
    }

    private void updateEvents(List<EventDto> events, String type){
        for (EventDto event : events) {
            try {
                MDC.put("traceId", event.getEventId().toString());
                if (Predicates.isEServiceEvent().test(event)) {
                    organizationService.updateOrganizationEService((EServiceEventDto) event);
                } else {
                    consumerService.updateConsumer((AgreementEventDto) event);
                }
            }
            catch (PDNDEventException ex) {
                deadEventService.saveDeadEvent(event, type);
                throw ex;
            } finally {
                MDC.clear();
            }
        }
    }
}
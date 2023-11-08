package it.pagopa.interop.signalhub.updater.controller;

import it.pagopa.interop.signalhub.updater.exception.PDNDEventException;
import it.pagopa.interop.signalhub.updater.exception.PDNDConnectionResetException;
import it.pagopa.interop.signalhub.updater.exception.PDNDNoEventsException;
import it.pagopa.interop.signalhub.updater.model.*;
import it.pagopa.interop.signalhub.updater.service.*;
import it.pagopa.interop.signalhub.updater.utility.Predicates;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final DeadEventService deadEventService;


    public void scheduleUpdater() {
        log.info("ScheduleUpdater Started: {}", dateTimeFormatter.format(LocalDateTime.now()));
        Long lastEventId = this.tracingBatchService.getLastEventIdByTracingBatch();
        lastEventId = updateRecursiveFlow(lastEventId);
        tracingBatchService.terminateTracingBatch(TracingBatchStateEnum.ENDED, lastEventId+1);
    }

    private Long updateRecursiveFlow(Long lastEventId) {
        try {
            EventsDto events = this.interopService.getAgreementsAndEServices(lastEventId);
            updateEvents(events.getEvents());
            return updateRecursiveFlow(events.getLastEventId());
        } catch (PDNDConnectionResetException ex) {
            tracingBatchService.terminateTracingBatch(TracingBatchStateEnum.ENDED, ex.getEventId());
            throw ex;
        } catch (PDNDNoEventsException ex) {
            return lastEventId;
        } catch (PDNDEventException ex) {
            tracingBatchService.terminateTracingBatch(TracingBatchStateEnum.ENDED_WITH_ERROR, ex.getEventId());
            throw ex;
        }
    }

    private void updateEvents(List<EventDto> events){
        for (EventDto event : events) {
            try {
                if (Predicates.isEServiceEvent().test(event)) {
                    organizationService.updateOrganizationEService((EServiceEventDto) event);
                } else {
                    consumerService.updateConsumer((AgreementEventDto) event);
                }
            }
            catch (PDNDEventException ex) {
                deadEventService.saveDeadEvent(event);
                throw ex;
            }
        }
    }
}
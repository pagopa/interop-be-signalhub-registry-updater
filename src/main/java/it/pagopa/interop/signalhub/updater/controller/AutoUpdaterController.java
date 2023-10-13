package it.pagopa.interop.signalhub.updater.controller;

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
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
        tracingBatchService.alreadyExistBatchInProgress()
                        .filter(result -> result.equals(Boolean.FALSE))
                        .flatMap(result -> tracingBatchService.getLastEventId())
                        .zipWhen(result -> tracingBatchService.createTracingBatch())
                        .flatMap(result -> updateRecursiveFlow(result.getT1(), result.getT2().getBatchId()))
                        .subscribe();
    }

    private Mono<Void> updateRecursiveFlow(Long lastEventId, Long batchId) {
        return Mono.just(lastEventId)
                .doOnNext(lastId -> log.info("Retrieve events from lastEventId = {}", lastId))
                .flatMap(lastId -> getAgreementsAndEservices(lastId, batchId))
                .flatMap(eventsDto -> Flux.fromIterable(eventsDto.getEvents())
                                    .flatMap(eventDto -> updateEventAndCatchError(eventDto, batchId))
                                    .collectList()
                                    .doOnNext(list -> log.info("Scan event ended, check if exists other page"))
                                    .flatMap(list -> updateRecursiveFlow(eventsDto.getLastEventId(), batchId))
                )
                .then();
    }

    private Mono<EventsDto> getAgreementsAndEservices(Long lastEventId, Long batchId){
        return this.interopService.getAgreementsAndEservices(lastEventId)
                .switchIfEmpty(Mono.defer(() ->
                        tracingBatchService.terminateTracingBatch(batchId, TracingBatchStateEnum.ENDED, lastEventId)
                                .flatMap(response -> Mono.empty())
                ));
    }

    private Mono<Void> updateEventAndCatchError(EventDto event, Long batchId) {
        return this.getCorrectFlowForUpdate(event)
                .onErrorResume(WebClientResponseException.class, ex ->
                    tracingBatchService.terminateTracingBatch(batchId, TracingBatchStateEnum.ENDED_WITH_ERROR, event.getEventId()-1)
                            .flatMap(response -> Mono.error(ex))
                );
    }

    private Mono<Void> getCorrectFlowForUpdate(EventDto event){
        if (Predicates.isEServiceEvent().test(event))
            return organizationService.updateOrganizationEService((EServiceEventDTO) event).then();
        return consumerService.updateConsumer((AgreementEventDto) event).then();
    }

}
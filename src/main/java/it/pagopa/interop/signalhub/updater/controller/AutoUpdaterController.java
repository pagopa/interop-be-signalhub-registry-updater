package it.pagopa.interop.signalhub.updater.controller;

import it.pagopa.interop.signalhub.updater.model.*;
import it.pagopa.interop.signalhub.updater.service.ConsumerService;
import it.pagopa.interop.signalhub.updater.service.InteropService;
import it.pagopa.interop.signalhub.updater.service.OrganizationService;
import it.pagopa.interop.signalhub.updater.utility.Predicates;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@Component
@AllArgsConstructor
public class AutoUpdaterController {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private InteropService interopService;
    private OrganizationService organizationService;
    private ConsumerService consumerService;



    @Scheduled(fixedRate = 100000)
    private void scheduleUpdater() {
        log.info("ScheduleUpdater Started: {}", dateTimeFormatter.format(LocalDateTime.now()));
        Long lastEventId = 0L;
        updateRecursiveFlow(lastEventId)
                .subscribe();
    }

    private Mono<Void> updateRecursiveFlow(Long lastEventId) {
        return Mono.just(lastEventId)
                .doOnNext(lastId -> log.info("Retrieve events from lastEventId = {}", lastId))
                .flatMap(interopService::getAgreementsAndEservices)
                .flatMap(eventsDto -> Flux.fromIterable(eventsDto.getEvents())
                                    .flatMap(this::getCorrectFlowForUpdate)
                                    .collectList()
                                    .doOnNext(list -> log.info("Scan event ended, check if exists other page"))
                                    .flatMap(list -> updateRecursiveFlow(eventsDto.getLastEventId()))
                )
                .then();
    }

    private Mono<Void> getCorrectFlowForUpdate(EventDto event){
        if (Predicates.isEServiceEvent().test(event))
            return organizationService.updateOrganizationEService((EServiceEventDTO) event).then();
        return consumerService.updateConsumer((AgreementEventDto) event).then();
    }

}
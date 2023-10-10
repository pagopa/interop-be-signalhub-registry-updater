package it.pagopa.interop.signalhub.updater.controller;

import it.pagopa.interop.signalhub.updater.externalclient.InteroperabilityClient;
import it.pagopa.interop.signalhub.updater.externalclient.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.externalclient.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Event;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Events;
import it.pagopa.interop.signalhub.updater.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.updater.mapper.OrganizationEServiceMapper;
import it.pagopa.interop.signalhub.updater.service.UpdaterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;


@Slf4j
@Component
@AllArgsConstructor
public class AutoUpdaterController {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private UpdaterService updaterService;
    private InteroperabilityClient interoperabilityClient;
    private ConsumerEServiceMapper consumerEServiceMapper;
    private OrganizationEServiceMapper organizationEServiceMapper;



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
                .delayElement(Duration.ofMillis(1000))
                .flatMap(interoperabilityClient::getEventsFromId)
                .doOnNext(events -> log.info("Total events retrieved = {}", events.getEvents().size()))
                .filter(AutoUpdaterUtils.isEventsNotEmpty())
                .flatMap(item -> getAgreementOrEserviceEvents(item)
                        .flatMap(this::getCorrectFlowForUpdate)
                        .collectList()
                        .doOnNext(list -> log.info("Scan event ended, check if exists other page"))
                        .flatMap(list -> updateRecursiveFlow(item.getLastEventId()))
                )
                .then();
    }

    private Mono<Void> getCorrectFlowForUpdate(Event event){
        return Mono.just(event)
                .filter(AutoUpdaterUtils.isEServiceEvent())
                .doOnNext(filtered -> log.info("Eservice Event {}", filtered))
                .flatMap(this::updateEServiceEvent)
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Agreement  Event {}", event);
                    return this.updateAgreementEvent(event);
                }))
                .then();
    }

    private Flux<Event> getAgreementOrEserviceEvents(Events events) {
        return Flux.fromIterable(events.getEvents())
                .filter(AutoUpdaterUtils.isAgreementOrEServiceEvent())
                .log(log.getName(), Level.INFO, SignalType.ON_COMPLETE);
    }

    private Mono<Event> updateEServiceEvent(Event event) {

        return Mono.just(event.getObjectId().get(AutoUpdaterUtils.ESERVICE_KEY_ID))
                .doOnNext(eserviceId -> log.info("Retrieve and update eservice with {} id", eserviceId))
                .flatMap(this::retrievingEService)
                .flatMap(updaterService::updateOrganizationEservice)
                .switchIfEmpty(Mono.just(new OrganizationEServiceDto()))
                .thenReturn(event);


    }

    private Mono<Event> updateAgreementEvent(Event event) {
        return Mono.just(event.getObjectId().get(AutoUpdaterUtils.AGREEMENT_KEY_ID))
                .doOnNext(agreementId -> log.info("Retrieve and update agreement with {} id", agreementId))
                .flatMap(this::retrievingAgreement)
                .flatMap(updaterService::updateConsumer)
                .switchIfEmpty(Mono.just(new ConsumerEServiceDto()))
                .thenReturn(event);
    }

    private Mono<ConsumerEServiceDto> retrievingAgreement(String agreementId) {
        return interoperabilityClient.getAgreement(agreementId)
                .doOnNext(response -> log.info("Retrieved agreement detail {}", response.getId()))
                .map(agreement -> consumerEServiceMapper.fromAgreementToConsumerEServiceDto(agreement))
                .doOnError(exception -> log.error("ErrorReason: {}", exception.getMessage()))
                .onErrorResume(ex -> Mono.just(new ConsumerEServiceDto()));
    }

    private Mono<OrganizationEServiceDto> retrievingEService(String eserviceId) {
        return interoperabilityClient.getEService(eserviceId)
                .doOnNext(response -> log.info("Retrieved eservice detail {}", response.getId()))
                .map(eService -> organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService))
                .doOnError(exception -> log.error("ErrorReason: {}", exception.getMessage()))
                .onErrorResume(ex -> Mono.just(new OrganizationEServiceDto()));
    }
}
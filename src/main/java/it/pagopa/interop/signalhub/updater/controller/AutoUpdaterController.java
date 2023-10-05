package it.pagopa.interop.signalhub.updater.controller;

import it.pagopa.interop.signalhub.updater.externalClient.InteroperabilityClient;
import it.pagopa.interop.signalhub.updater.externalClient.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.externalClient.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Event;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Events;
import it.pagopa.interop.signalhub.updater.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.updater.mapper.OrganizationEServiceMapper;
import it.pagopa.interop.signalhub.updater.service.UpdaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@Component
public class AutoUpdaterController {
    @Autowired
    private UpdaterService updaterService;
    @Autowired
    private InteroperabilityClient interoperabilityClient;
    @Autowired
    private ConsumerEServiceMapper consumerEServiceMapper;
    @Autowired
    private OrganizationEServiceMapper organizationEServiceMapper;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


    @Scheduled(fixedRate = 10000)
    private void scheduleUpdater() {
        log.info("ScheduleUpdater Started: {}", dateTimeFormatter.format(LocalDateTime.now()));
        Long lastEventId = 0L;
        updateRecursiveFlow(lastEventId)
                .subscribe();
    }

    private Mono<Events> updateRecursiveFlow(Long lastEventId) {
        log.info("UpdateRecursiveFlow received input: lastEventId = {}", lastEventId);
        return interoperabilityClient.getEventsFromId(lastEventId)
                .filter(event -> AutoUpdaterUtils.isEventsEmpty().test(event))
                .flatMap(events -> {
                    log.info("getEvents response: events = {}", events);
                    return checkEventType(events);
                })
                .flatMap(events -> {
                    log.info("updateRecursiveFlow call: events = {}", events);
                    return updateRecursiveFlow(events.getLastEventId());
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Mono empty");
                    return Mono.empty();
                }));
    }

    private Mono<Events> checkEventType(Events events) {
        log.info("CheckEventType received input: events = {}", events);
        return AutoUpdaterUtils.monoToFlux(events.getEvents())
                .filter(AutoUpdaterUtils.isNotAgreementAndEServiceEvent())
                .flatMap(this::updateEvents)
                .collectList()
                .thenReturn(events);
    }

    private Mono<Void> updateEvents(Event event) {
        log.info("UpdateEvents received input: event = {}", event);
        return Mono.just(event)
                .filter(AutoUpdaterUtils.isEServiceEvent())
                .flatMap(this::updateEServiceEvents)
                .switchIfEmpty(this.updateAgreementEvents(event));
    }

    private Mono<Void> updateEServiceEvents(Event event) {
        log.info("UpdateEServiceEvents received input: event = {}", event);
        return retrievingEService(event.getObjectId().get(AutoUpdaterUtils.ESERVICE_KEY_ID))
                .flatMap(updaterService::updateOrganizationEservice);
    }

    private Mono<Void> updateAgreementEvents(Event event) {
        log.info("UpdateAgreementEvents received input: event = {}", event);
        return retrievingAgreement(event.getObjectId().get(AutoUpdaterUtils.AGREEMENT_KEY_ID))
                .flatMap(updaterService::updateConsumer);
    }

    private Mono<ConsumerEServiceDto> retrievingAgreement(String agreementId) {
        log.info("RetrievingAgreement received input: agreementId = {}", agreementId);
        return interoperabilityClient.getAgreement(agreementId)
                .map(agreement -> consumerEServiceMapper.fromAgreementToConsumerEServiceDto(agreement))
                .doOnError(exception -> log.error("ErrorReason: {}", exception.getMessage()));
    }

    private Mono<OrganizationEServiceDto> retrievingEService(String eserviceId) {
        log.info("RetrievingEService received input: eserviceId = {}", eserviceId);
        return interoperabilityClient.getEService(eserviceId)
                .map(eService -> organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService))
                .doOnError(exception -> log.error("ErrorReason: {}", exception.getMessage()));
    }
}
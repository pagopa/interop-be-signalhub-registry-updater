package it.pagopa.interop.signalhub.updater.service;

import it.pagopa.interop.signalhub.updater.externalClient.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.externalClient.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.repository.ConsumerEserviceRepository;
import it.pagopa.interop.signalhub.updater.repository.OrganizationEserviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class UpdaterService {
    @Autowired
    private ConsumerEserviceRepository consumerEserviceRepository;
    @Autowired
    private OrganizationEserviceRepository organizationEserviceRepository;


    @Transactional
    public Mono<Void> updateConsumer(ConsumerEServiceDto consumerEServiceDto) {
        return this.consumerEserviceRepository.saveAndFlush(consumerEServiceDto.getEserviceId(), consumerEServiceDto.getConsumerId(), consumerEServiceDto.getState())
                .then();
    }

    @Transactional
    public Mono<Void> updateOrganizationEservice(OrganizationEServiceDto organizationEServiceDto) {
        return this.organizationEserviceRepository.saveAndFlush(organizationEServiceDto.getEserviceId(), organizationEServiceDto.getProducerId(), organizationEServiceDto.getState())
                .then();
    }
}
package it.pagopa.interop.signalhub.updater.service;

import it.pagopa.interop.signalhub.updater.externalclient.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.externalclient.model.OrganizationEServiceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class UpdaterService {
    /*
    @Autowired
    private ConsumerEserviceRepository consumerEserviceRepository;
    @Autowired
    private OrganizationEserviceRepository organizationEserviceRepository;
*/

   // @Transactional
    public Mono<ConsumerEServiceDto> updateConsumer(ConsumerEServiceDto consumerEServiceDto) {
        log.info("Update consumer {}", consumerEServiceDto);
        return Mono.just(new ConsumerEServiceDto());
        /*
        return this.consumerEserviceRepository.saveAndFlush(consumerEServiceDto.getEserviceId(), consumerEServiceDto.getConsumerId(), consumerEServiceDto.getState())
                .then();

         */
    }

    //@Transactional
    public Mono<OrganizationEServiceDto> updateOrganizationEservice(OrganizationEServiceDto organizationEServiceDto) {
        log.info("Update organization eservice {}", organizationEServiceDto);
        return Mono.just(new OrganizationEServiceDto());
        /*
        return this.organizationEserviceRepository.saveAndFlush(organizationEServiceDto.getEserviceId(), organizationEServiceDto.getProducerId(), organizationEServiceDto.getState())
                .then();

         */
    }
}
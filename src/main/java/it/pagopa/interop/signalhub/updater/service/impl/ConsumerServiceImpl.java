package it.pagopa.interop.signalhub.updater.service.impl;


import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import it.pagopa.interop.signalhub.updater.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.AgreementEventDto;
import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.repository.ConsumerEserviceRepository;
import it.pagopa.interop.signalhub.updater.repository.cache.model.ConsumerEServiceCache;
import it.pagopa.interop.signalhub.updater.repository.cache.repository.ConsumerEServiceCacheRepository;
import it.pagopa.interop.signalhub.updater.service.ConsumerService;
import it.pagopa.interop.signalhub.updater.service.InteropService;
import it.pagopa.interop.signalhub.updater.service.OrganizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;



@Slf4j
@Service
@AllArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {
    private final InteropService interopService;
    private final OrganizationService organizationService;
    private final ConsumerEserviceRepository consumerEserviceRepository;
    private final ConsumerEServiceMapper mapper;
    private final ConsumerEServiceCacheRepository consumerEServiceCacheRepository;


    @Override
    public ConsumerEServiceDto updateConsumer(AgreementEventDto agreementEventDto) {
        log.info("[{} - {}] Retrieving detail agreement...", agreementEventDto.getEventId(), agreementEventDto.getAgreementId());
        ConsumerEServiceDto detailAgreement = this.interopService.getConsumerEService(agreementEventDto.getAgreementId(), agreementEventDto.getEventId());
        log.info("[{} - {}] Detail agreement retrieved with state {}", agreementEventDto.getEventId(), agreementEventDto.getAgreementId(), detailAgreement.getState());

        ConsumerEService entity = this.consumerEserviceRepository.findByEserviceIdAndConsumerId(detailAgreement.getEserviceId(), detailAgreement.getConsumerId())
                .orElse(getInitialConsumerEService(detailAgreement));
        log.info("[{} - {}] Entity {} exist into DB",
                agreementEventDto.getEventId(),
                agreementEventDto.getAgreementId(),
                entity.getTmstInsert() ==  null ? "not" : "");

        if (detailAgreement.getState().equals("ACTIVE")) checkAndCreateOrganization(detailAgreement, agreementEventDto.getEventId());

        entity.setState(detailAgreement.getState());
        entity = this.consumerEserviceRepository.saveAndFlush(entity);
        log.info("[{} - {}] Entity saved",
                agreementEventDto.getEventId(),
                agreementEventDto.getAgreementId());
        consumerEServiceCacheRepository.updateConsumerEService(mapper.toCacheFromEntity(entity));
        return mapper.toDtoFromEntity(entity);
    }

    private void checkAndCreateOrganization(ConsumerEServiceDto detailConsumer, Long eventId){
        this.organizationService.checkAndUpdate(detailConsumer.getEserviceId(), detailConsumer.getProducerId(), eventId);
    }


    private ConsumerEService getInitialConsumerEService(ConsumerEServiceDto consumerdto){
        ConsumerEService entity = mapper.toEntityFromProps(consumerdto.getEserviceId(), consumerdto.getConsumerId(), consumerdto.getState());
        entity.setEventId(consumerdto.getEventId());
        return entity;
    }

}

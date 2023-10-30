package it.pagopa.interop.signalhub.updater.service.impl;


import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import it.pagopa.interop.signalhub.updater.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.AgreementEventDto;
import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.repository.ConsumerEserviceRepository;
import it.pagopa.interop.signalhub.updater.cache.repository.ConsumerEServiceCacheRepository;
import it.pagopa.interop.signalhub.updater.service.ConsumerService;
import it.pagopa.interop.signalhub.updater.service.InteropService;
import it.pagopa.interop.signalhub.updater.service.OrganizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;



@Slf4j
@Service
@AllArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {
    private final InteropService interopService;
    private final OrganizationService organizationService;
    private final ConsumerEserviceRepository consumerEserviceRepository;
    private final ConsumerEServiceMapper consumerEServiceMapper;
    private final ConsumerEServiceCacheRepository consumerEServiceCacheRepository;


    @Override
    public ConsumerEServiceDto updateConsumer(AgreementEventDto agreementEventDto) {
        log.info("[{} - {}] Retrieving detail agreement...", agreementEventDto.getEventId(), agreementEventDto.getAgreementId());
        ConsumerEServiceDto detailAgreement = this.interopService.getConsumerEService(agreementEventDto.getAgreementId(), agreementEventDto.getEventId());
        log.info("[{} - {}] Detail agreement retrieved with state {}", agreementEventDto.getEventId(), agreementEventDto.getAgreementId(), detailAgreement.getState());

        ConsumerEService entity = this.consumerEserviceRepository.findByEserviceIdAndConsumerIdAndDescriptorId(detailAgreement.getEserviceId(), detailAgreement.getConsumerId(), detailAgreement.getDescriptorId())
                .orElse(getInitialConsumerEService(detailAgreement));
        log.info("[{} - {} - {}] Entity {} exist into DB",
                agreementEventDto.getEventId(),
                entity.getAgreementId(),
                entity.getDescriptorId(),
                entity.getTmstInsert() ==  null ? "not" : "");

        if (detailAgreement.getState().equals("ACTIVE")) checkAndCreateOrganization(detailAgreement, agreementEventDto.getEventId());

        String entityState= entity.getState();
        entity.setState(detailAgreement.getState());
        entity = this.consumerEserviceRepository.saveAndFlush(entity);
        log.info("[{} - {} - {}] Entity saved",
                agreementEventDto.getEventId(),
                entity.getAgreementId(),
                entity.getDescriptorId());
        if(!StringUtils.equalsIgnoreCase(entityState, detailAgreement.getState())) {
            consumerEServiceCacheRepository.updateConsumerEService(consumerEServiceMapper.toCacheFromEntity(entity));
        }
        return consumerEServiceMapper.toDtoFromEntity(entity);
    }

    private void checkAndCreateOrganization(ConsumerEServiceDto detailConsumer, Long eventId){
        this.organizationService.checkAndUpdate(detailConsumer.getEserviceId(), detailConsumer.getProducerId(), detailConsumer.getDescriptorId(), eventId);
    }


    private ConsumerEService getInitialConsumerEService(ConsumerEServiceDto consumerdto){
        ConsumerEService entity = consumerEServiceMapper.toEntityFromProps(consumerdto.getEserviceId(),
                consumerdto.getConsumerId(),
                consumerdto.getAgreementId(),
                consumerdto.getDescriptorId(),
                consumerdto.getState());
        entity.setEventId(consumerdto.getEventId());
        return entity;
    }

}

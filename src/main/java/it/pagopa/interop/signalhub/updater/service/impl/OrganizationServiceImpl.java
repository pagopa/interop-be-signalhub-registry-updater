package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import it.pagopa.interop.signalhub.updater.mapper.OrganizationEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.EServiceEventDto;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.repository.OrganizationEServiceRepository;
import it.pagopa.interop.signalhub.updater.cache.repository.OrganizationEServiceCacheRepository;
import it.pagopa.interop.signalhub.updater.service.InteropService;
import it.pagopa.interop.signalhub.updater.service.OrganizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;



@Slf4j
@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final InteropService interopService;
    private final OrganizationEServiceRepository repository;
    private final OrganizationEServiceMapper organizationEServiceMapper;
    private final OrganizationEServiceCacheRepository organizationEServiceCache;


    @Override
    public OrganizationEServiceDto updateOrganizationEService(EServiceEventDto eServiceEventDTO) {
        log.info("[{} - {} - {}] Retrieving producerID eservice...", eServiceEventDTO.getEventId(), eServiceEventDTO.getEServiceId(), eServiceEventDTO.getDescriptorId());
        //Only for retrieving Producer ID
        OrganizationEServiceDto detailEservice = this.interopService.getEService(eServiceEventDTO.getEServiceId(), eServiceEventDTO.getEventId());
        detailEservice.setDescriptorId(eServiceEventDTO.getDescriptorId());

        log.info("[{} - {} - {}] Detail eservice retrieved with producerID {}", detailEservice.getEventId(), detailEservice.getEserviceId(), detailEservice.getDescriptorId(), detailEservice.getProducerId());

        log.info("[{} - {} - {}] Retrieving state of eservice...", detailEservice.getEventId(), detailEservice.getEserviceId(), detailEservice.getDescriptorId());
        //Only setting eservices state
        detailEservice = this.interopService.getEServiceDescriptor(detailEservice);

        log.info("[{} - {} - {}] Detail eservice retrieved with state {}", detailEservice.getEventId(), detailEservice.getEserviceId(), detailEservice.getDescriptorId(), detailEservice.getState());

        OrganizationEService entity = this.repository.findByEserviceIdAndProducerIdAndDescriptorId(detailEservice.getEserviceId(), detailEservice.getProducerId(), detailEservice.getDescriptorId())
                .orElse(getInitialEService(detailEservice));

        log.info("[{} - {} - {}] Entity {} exist into DB",
                entity.getEventId(),
                entity.getEserviceId(),
                entity.getDescriptorId(),
                entity.getTmstInsert() ==  null ? "not" : "");

        if(!StringUtils.equalsIgnoreCase(entity.getState(), detailEservice.getState())) {
            organizationEServiceCache.updateOrganizationEService(organizationEServiceMapper.toCacheFromEntity(entity));
        }

        entity.setEventId(eServiceEventDTO.getEventId());
        entity.setState(detailEservice.getState());
        entity = this.repository.saveAndFlush(entity);

        log.info("[{} - {} - {}] Entity saved",
                entity.getEventId(), entity.getEserviceId(), entity.getDescriptorId());

        return organizationEServiceMapper.toDtoFromEntity(entity);
    }

    @Override
    public OrganizationEServiceDto checkAndUpdate(String eserviceId, String producerId, String descriptorId, Long eventId) {
        log.info("[{} - {} - {}] Check and Update organization eservice", eserviceId, producerId, descriptorId);
        OrganizationEService entity = this.repository.findByEserviceIdAndProducerIdAndDescriptorId(eserviceId, producerId, descriptorId)
                .orElse(null);
        if (entity != null) {
            log.info("[{} - {} - {}] Eservice already exist with state {}", eserviceId, producerId, descriptorId, entity.getState());
            return organizationEServiceMapper.toDtoFromEntity(entity);
        }

        EServiceEventDto eventDTO = new EServiceEventDto();
        eventDTO.setEventId(eventId);
        eventDTO.setEServiceId(eserviceId);
        eventDTO.setDescriptorId(descriptorId);
        log.info("[{} - {} - {}] Eservice doesn't exist", eventDTO.getEServiceId(), producerId, eventDTO.getDescriptorId());
        return updateOrganizationEService(eventDTO);
    }


    private OrganizationEService getInitialEService(OrganizationEServiceDto dto){
        OrganizationEService entity = organizationEServiceMapper.toEntityFromDto(dto);
        entity.setEventId(dto.getEventId());
        return entity;
    }
}

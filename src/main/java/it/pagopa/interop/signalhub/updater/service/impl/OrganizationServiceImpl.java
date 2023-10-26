package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import it.pagopa.interop.signalhub.updater.mapper.EServiceDescriptorMapper;
import it.pagopa.interop.signalhub.updater.mapper.OrganizationEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.EServiceDescriptorDto;
import it.pagopa.interop.signalhub.updater.model.EServiceEventDto;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.repository.OrganizationEserviceRepository;
import it.pagopa.interop.signalhub.updater.repository.cache.repository.OrganizationEServiceCacheRepository;
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
    private final OrganizationEserviceRepository repository;
    private final OrganizationEServiceMapper organizationEServiceMapper;
    private final EServiceDescriptorMapper eServiceDescriptorMapper;
    private final OrganizationEServiceCacheRepository organizationEServiceCache;


    @Override
    public OrganizationEServiceDto updateOrganizationEService(EServiceEventDto eServiceEventDTO) {
        log.info("[{} - {}] Retrieving detail eservice...", eServiceEventDTO.getEventId(), eServiceEventDTO.getEServiceId());
        OrganizationEServiceDto detailEservice = this.interopService.getEService(eServiceEventDTO.getEServiceId(), eServiceEventDTO.getEventId());
        log.info("[{} - {}] Detail eservice retrieved with state {}", eServiceEventDTO.getEventId(), eServiceEventDTO.getEServiceId(), detailEservice.getState());

        EServiceDescriptorDto eServiceDescriptorDto = this.interopService.getEServiceDescriptor(eServiceEventDTO.getEServiceId(), eServiceEventDTO.getEventId(), eServiceEventDTO.getDescriptorId());
        detailEservice = eServiceDescriptorMapper.fromEServiceDescriptorDtoToOrganizationEServiceDto(eServiceDescriptorDto, detailEservice);

        OrganizationEService entity = this.repository.findByEserviceIdAndProducerIdAndDescriptorId(detailEservice.getEserviceId(), detailEservice.getProducerId(), eServiceEventDTO.getDescriptorId())
                .orElse(getInitialEService(detailEservice));

        log.info("[{} - {}] Entity {} exist into DB",
                eServiceEventDTO.getEventId(),
                eServiceEventDTO.getEServiceId(),
                entity.getTmstInsert() ==  null ? "not" : "");


        String entityState= entity.getState();
        entity.setState(detailEservice.getState());
        entity = this.repository.saveAndFlush(entity);
        log.info("[{} - {}] Entity saved",
                eServiceEventDTO.getEventId(),
                eServiceEventDTO.getEServiceId());
        if(!StringUtils.equalsIgnoreCase(entityState, detailEservice.getState())) {
            organizationEServiceCache.updateOrganizationEService(organizationEServiceMapper.toCacheFromEntity(entity));
        }
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
        log.info("[{} - {} - {}] Eservice doesn't exist", eserviceId, producerId, descriptorId);
        return updateOrganizationEService(eventDTO);
    }


    private OrganizationEService getInitialEService(OrganizationEServiceDto dto){
        OrganizationEService entity = organizationEServiceMapper.toEntityFromProps(dto.getEserviceId(), dto.getProducerId(), dto.getState());
        entity.setEventId(dto.getEventId());
        return entity;
    }
}

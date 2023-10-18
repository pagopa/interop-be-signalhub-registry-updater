package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import it.pagopa.interop.signalhub.updater.mapper.OrganizationEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.EServiceEventDto;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.repository.OrganizationEserviceRepository;
import it.pagopa.interop.signalhub.updater.repository.cache.model.OrganizationEServiceCache;
import it.pagopa.interop.signalhub.updater.repository.cache.repository.OrganizationEServiceCacheRepository;
import it.pagopa.interop.signalhub.updater.service.InteropService;
import it.pagopa.interop.signalhub.updater.service.OrganizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;



@Slf4j
@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final InteropService interopService;
    private final OrganizationEserviceRepository repository;
    private final OrganizationEServiceMapper mapper;
    private final OrganizationEServiceCacheRepository organizationEServiceCache;


    @CachePut
    @Override
    public OrganizationEServiceDto updateOrganizationEService(EServiceEventDto eServiceEventDTO) {
        log.info("[{} - {}] Retrieving detail eservice...", eServiceEventDTO.getEventId(), eServiceEventDTO.getEServiceId());
        OrganizationEServiceDto detailEservice = this.interopService.getEService(eServiceEventDTO.getEServiceId(), eServiceEventDTO.getEventId());
        log.info("[{} - {}] Detail eservice retrieved with state {}", eServiceEventDTO.getEventId(), eServiceEventDTO.getEServiceId(), detailEservice.getState());

        OrganizationEService entity = this.repository.findByEserviceIdAndProducerId(detailEservice.getEserviceId(), detailEservice.getProducerId())
                .orElse(getInitialEService(detailEservice));

        log.info("[{} - {}] Entity {} exist into DB",
                eServiceEventDTO.getEventId(),
                eServiceEventDTO.getEServiceId(),
                entity.getTmstInsert() ==  null ? "not" : "");


        entity.setState(detailEservice.getState());
        entity = this.repository.saveAndFlush(entity);
        log.info("[{} - {}] Entity saved",
                eServiceEventDTO.getEventId(),
                eServiceEventDTO.getEServiceId());
        organizationEServiceCache.updateEService(mapper.toCacheFromEntity(entity));
        return mapper.toDtoFromEntity(entity);
    }

    @Override
    public OrganizationEServiceDto checkAndUpdate(String eserviceId, String producerId, Long eventId) {
        log.info("[{} - {}] Check and Update organization eservice", eserviceId, producerId);
        OrganizationEService entity = this.repository.findByEserviceIdAndProducerId(eserviceId, producerId)
                .orElse(null);
        if (entity != null) {
            log.info("[{} - {}] Eservice already exist with state {}", eserviceId, producerId, entity.getState());
            return mapper.toDtoFromEntity(entity);
        }

        EServiceEventDto eventDTO = new EServiceEventDto();
        eventDTO.setEventId(eventId);
        eventDTO.setEServiceId(eserviceId);
        log.info("[{} - {}] Eservice doesn't exist", eserviceId, producerId);
        return updateOrganizationEService(eventDTO);
    }


    private OrganizationEService getInitialEService(OrganizationEServiceDto dto){
        OrganizationEService entity = mapper.toEntityFromProps(dto.getEserviceId(), dto.getProducerId(), dto.getState());
        entity.setEventId(dto.getEventId());
        return entity;
    }
}

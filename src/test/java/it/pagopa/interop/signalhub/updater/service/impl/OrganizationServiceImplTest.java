package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.cache.model.OrganizationEServiceCache;
import it.pagopa.interop.signalhub.updater.cache.repository.OrganizationEServiceCacheRepository;
import it.pagopa.interop.signalhub.updater.config.DataBuilder;
import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import it.pagopa.interop.signalhub.updater.mapper.OrganizationEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.EServiceDescriptorDto;
import it.pagopa.interop.signalhub.updater.model.EServiceEventDto;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.repository.OrganizationEServiceRepository;
import it.pagopa.interop.signalhub.updater.service.InteropService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {
    @InjectMocks
    private OrganizationServiceImpl organizationService;
    @Mock
    private InteropService interopService;
    @Mock
    private OrganizationEServiceRepository repository;
    @Mock
    private OrganizationEServiceMapper mapper;
    @Mock
    private OrganizationEServiceCacheRepository organizationEServiceCache;
    private EServiceEventDto eServiceEventDto;
    private OrganizationEServiceDto organizationEServiceDto;
    private EServiceDescriptorDto eServiceDescriptorDto;
    private OrganizationEService organizationEService;


    @BeforeEach
    void inizialize(){
        eServiceEventDto= new EServiceEventDto();
        eServiceEventDto.setEServiceId("123");
        eServiceEventDto.setObjectType("test");
        eServiceEventDto.setEventId(1L);
        eServiceEventDto.setEventType("test");

        organizationEServiceDto= new OrganizationEServiceDto();
        organizationEServiceDto.setEserviceId("123");
        organizationEServiceDto.setProducerId("123");
        organizationEServiceDto.setState("ACTIVE");
        organizationEServiceDto.setEventId(1L);

        eServiceDescriptorDto = new EServiceDescriptorDto();
        eServiceDescriptorDto.setState(organizationEServiceDto.getState());

        organizationEService= new OrganizationEService();
        organizationEService.setEserviceId("123");
        organizationEService.setProducerId("123");
        organizationEService.setState("test");
    }

    @Test
    void updateOrganizationEService() {
        Mockito.when(interopService.getEService(Mockito.any(), Mockito.any())).thenReturn(organizationEServiceDto);
        Mockito.when(interopService.getEServiceDescriptor(Mockito.any())).thenReturn(organizationEServiceDto);
        Mockito.when(repository.findByEserviceIdAndProducerIdAndDescriptorId(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(mapper.toEntityFromDto(Mockito.any())).thenReturn(organizationEService);
        Mockito.when(repository.saveAndFlush(Mockito.any())).thenReturn(organizationEService);
        Mockito.when(mapper.toCacheFromEntity(Mockito.any())).thenReturn(new OrganizationEServiceCache());
        Mockito.when(mapper.toDtoFromEntity(Mockito.any())).thenReturn(new OrganizationEServiceDto());

        assertNotNull(organizationService.updateOrganizationEService(eServiceEventDto));
    }

    @Test
    void checkAndUpdate() {
        Mockito.when(repository.findByEserviceIdAndProducerIdAndDescriptorId(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Optional.of(organizationEService));
        Mockito.when(mapper.toDtoFromEntity(Mockito.any())).thenReturn(new OrganizationEServiceDto());
        assertNotNull(organizationService.checkAndUpdate("123", "123", "123", 1L));
    }

    @Test
    void checkAndUpdateWhenOrganizationIsNotPresentToDb() {
        Mockito.when(repository.findByEserviceIdAndProducerIdAndDescriptorId(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(interopService.getEService(Mockito.any(), Mockito.any()))
                .thenReturn(organizationEServiceDto);
        Mockito.when(interopService.getEServiceDescriptor(Mockito.any()))
                .thenReturn(DataBuilder.getEservice());
        Mockito.when(repository.findByEserviceIdAndProducerIdAndDescriptorId(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(mapper.toEntityFromDto(Mockito.any()))
                .thenReturn(organizationEService);
        Mockito.when(repository.saveAndFlush(Mockito.any()))
                .thenReturn(organizationEService);
        Mockito.when(mapper.toCacheFromEntity(Mockito.any()))
                .thenReturn(new OrganizationEServiceCache());
        Mockito.when(mapper.toDtoFromEntity(Mockito.any()))
                .thenReturn(new OrganizationEServiceDto());

        assertNotNull(organizationService.checkAndUpdate("123", "123", "123", 1L));
    }

    @Test
    void checkAndUpdateButEserviceNotExist() {
        Mockito.when(repository.findByEserviceIdAndProducerIdAndDescriptorId(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(interopService.getEService(Mockito.any(), Mockito.any()))
                .thenReturn(DataBuilder.getEservice());
        Mockito.when(interopService.getEServiceDescriptor(Mockito.any()))
                .thenReturn(DataBuilder.getEservice());
        Mockito.when(repository.findByEserviceIdAndProducerIdAndDescriptorId(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(mapper.toEntityFromDto(Mockito.any()))
                .thenReturn(organizationEService);
        Mockito.when(repository.saveAndFlush(Mockito.any()))
                .thenReturn(organizationEService);
        Mockito.when(mapper.toCacheFromEntity(Mockito.any()))
                .thenReturn(new OrganizationEServiceCache());
        Mockito.when(mapper.toDtoFromEntity(Mockito.any()))
                .thenReturn(new OrganizationEServiceDto());

        assertNotNull(organizationService.checkAndUpdate("123", "123", "123", 1L));
    }
}
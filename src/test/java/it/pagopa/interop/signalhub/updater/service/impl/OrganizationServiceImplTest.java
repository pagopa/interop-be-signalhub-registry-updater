package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.config.DataBuilder;
import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.EServiceDescriptorState;
import it.pagopa.interop.signalhub.updater.mapper.OrganizationEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.repository.OrganizationEserviceRepository;
import it.pagopa.interop.signalhub.updater.cache.repository.OrganizationEServiceCacheRepository;
import it.pagopa.interop.signalhub.updater.service.InteropService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {
    @Spy
    @InjectMocks
    private OrganizationServiceImpl organizationService;
    @Mock
    private InteropService interopService;
    @Mock
    private OrganizationEserviceRepository repository;
    @Mock
    private OrganizationEServiceMapper mapper;
    @Mock
    private OrganizationEServiceCacheRepository organizationEServiceCache;



    @Test
    void whenEserviceAlreadyExistThenCheckExistOnCacheAndEditState(){
        OrganizationEServiceDto detailFromInterop = DataBuilder.getEservice();
        detailFromInterop.setState(EServiceDescriptorState.SUSPENDED.getValue());

        //Mock retrieve detail from interop
        Mockito.when(interopService.getEService(Mockito.any(), Mockito.any()))
                .thenReturn(detailFromInterop);

        //Mock retrieve descriptor ID from interop
        Mockito.when(interopService.getEServiceDescriptor(Mockito.any()))
                .thenReturn(detailFromInterop);

        OrganizationEService entity = DataBuilder.getEserviceEntity();
        entity.setState(EServiceDescriptorState.ARCHIVED.getValue());

        //Mock return eservice from DB
        Mockito.when(repository.findByEserviceIdAndProducerIdAndDescriptorId(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(entity));

        Mockito.when(mapper.toEntityFromDto(Mockito.any())).thenReturn(entity);
        //Mock update cache because the state is different
        Mockito.doNothing().when(organizationEServiceCache).updateOrganizationEService(Mockito.any());


        OrganizationEService entitySaved = DataBuilder.getEserviceEntity();
        entitySaved.setState(detailFromInterop.getState());
        //Mock save and flush
        Mockito.when(repository.saveAndFlush(Mockito.any()))
                .thenReturn(entitySaved);

        Mockito.when(mapper.toDtoFromEntity(Mockito.any())).thenReturn(detailFromInterop);


        OrganizationEServiceDto response = organizationService.updateOrganizationEService(DataBuilder.getEserviceEventDTO());


        //Verify that call organization service because state changed
        Mockito.verify(
                organizationEServiceCache,
                Mockito.timeout(2000).times(1)
        ).updateOrganizationEService(Mockito.any());


        assertNotNull(response);
        assertEquals(detailFromInterop.getState(), response.getState());

    }


    /** CHECK AND UPDATE ESERVICE CASE TEST **/

    @Test
    void whenOrganizationExistThenCheckAndUpdateReturnEserviceInDB(){
        Mockito.when(repository.findByEserviceIdAndProducerIdAndDescriptorId(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(DataBuilder.getEserviceEntity()));

        OrganizationEServiceDto mockResponse = DataBuilder.getEservice();
        Mockito.when(mapper.toDtoFromEntity(Mockito.any())).thenReturn(DataBuilder.getEservice());

        OrganizationEServiceDto eServiceDto = this.organizationService.checkAndUpdate(mockResponse.getEserviceId(), mockResponse.getProducerId(), mockResponse.getDescriptorId(), 2L);

        assertNotNull(eServiceDto);
    }


    @Test
    void whenOrganizationNotExistThenCheckAndUpdateCallUpdateOrganizationEservice(){
        Mockito.when(repository.findByEserviceIdAndProducerIdAndDescriptorId(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());

        OrganizationEServiceDto mockResponse = DataBuilder.getEservice();
        Mockito.doReturn(DataBuilder.getEservice()).when(organizationService).updateOrganizationEService(Mockito.any());


        OrganizationEServiceDto eServiceDto = this.organizationService.checkAndUpdate(mockResponse.getEserviceId(), mockResponse.getProducerId(), mockResponse.getDescriptorId(), 2L);

        assertNotNull(eServiceDto);

        Mockito.verify(organizationService, Mockito.timeout(1000).times(1)).updateOrganizationEService(Mockito.any());
    }

}
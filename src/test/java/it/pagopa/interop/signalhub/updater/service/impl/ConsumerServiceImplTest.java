package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.config.DataBuilder;
import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.AgreementState;
import it.pagopa.interop.signalhub.updater.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.repository.ConsumerEServiceRepository;
import it.pagopa.interop.signalhub.updater.cache.repository.ConsumerEServiceCacheRepository;
import it.pagopa.interop.signalhub.updater.service.InteropService;
import it.pagopa.interop.signalhub.updater.service.OrganizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ConsumerServiceImplTest {
    @InjectMocks
    private ConsumerServiceImpl consumerService;
    @Mock
    private InteropService interopService;
    @Mock
    private OrganizationService organizationService;
    @Mock
    private ConsumerEServiceRepository consumerEserviceRepository;
    @Mock
    private ConsumerEServiceMapper mapper;
    @Mock
    private ConsumerEServiceCacheRepository consumerEServiceCacheRepository;


    @Test
    void whenConsumerStateIsActiveAndNoEditThenCallOrganizationServiceAndSaveOnlyDB(){
        ConsumerEServiceDto detailFromInterop = DataBuilder.getConsumerDto();
        detailFromInterop.setState(AgreementState.ACTIVE.getValue());

        //Mock retrieve detail from interop
        Mockito.when(interopService.getConsumerEService(Mockito.any(), Mockito.any()))
                .thenReturn(detailFromInterop);

        Mockito.when(mapper.toEntityFromProps(Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any())).thenReturn(DataBuilder.getConsumerEntity());

        //Mock return empty from DB
        Mockito.when(consumerEserviceRepository.findByEserviceIdAndConsumerIdAndDescriptorId(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());

        //Mock check and update organization service because state is active
        Mockito.when(organizationService.checkAndUpdate(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new OrganizationEServiceDto());

        //Mock save and flush
        Mockito.when(consumerEserviceRepository.saveAndFlush(Mockito.any())).thenReturn(DataBuilder.getConsumerEntity());

        Mockito.when(mapper.toDtoFromEntity(Mockito.any())).thenReturn(detailFromInterop);

        ConsumerEServiceDto response = consumerService.updateConsumer(DataBuilder.getAgreementEventDTO());


        //Verify that call organization service because state is active
        Mockito.verify(
                organizationService,
                Mockito.timeout(2000).times(1)
        ).checkAndUpdate(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());


        //Verify that not call organization service because state not changed
        Mockito.verify(
                consumerEServiceCacheRepository,
                Mockito.timeout(2000).times(0)
        ).updateConsumerEService(Mockito.any());


        assertNotNull(response);
        assertEquals(detailFromInterop.getState(), response.getState());

    }



    @Test
    void whenConsumerStateIsPENDINGAndIsEditThenCheckExistOnCacheAndSaveOnlyDB(){
        ConsumerEServiceDto detailFromInterop = DataBuilder.getConsumerDto();
        detailFromInterop.setState(AgreementState.SUSPENDED.getValue());

        //Mock retrieve detail from interop
        Mockito.when(interopService.getConsumerEService(Mockito.any(), Mockito.any()))
                .thenReturn(detailFromInterop);

        Mockito.when(mapper.toEntityFromProps(Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any())).thenReturn(DataBuilder.getConsumerEntity());


        ConsumerEService entity = DataBuilder.getConsumerEntity();
        entity.setState(AgreementState.ACTIVE.getValue());

        //Mock return empty from DB
        Mockito.when(consumerEserviceRepository.findByEserviceIdAndConsumerIdAndDescriptorId(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(entity));

        //Mock check and update organization service because state is active
        Mockito.doNothing().when(consumerEServiceCacheRepository).updateConsumerEService(Mockito.any());

        //Mock save and flush
        ConsumerEService saved = DataBuilder.getConsumerEntity();
        saved.setState(detailFromInterop.getState());
        Mockito.when(consumerEserviceRepository.saveAndFlush(Mockito.any())).thenReturn(saved);

        Mockito.when(mapper.toDtoFromEntity(Mockito.any())).thenReturn(detailFromInterop);

        ConsumerEServiceDto response = consumerService.updateConsumer(DataBuilder.getAgreementEventDTO());


        //Verify that call organization service because state is active
        Mockito.verify(
                organizationService,
                Mockito.timeout(2000).times(0)
        ).checkAndUpdate(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());


        //Verify that not call organization service because state not changed
        Mockito.verify(
                consumerEServiceCacheRepository,
                Mockito.timeout(2000).times(1)
        ).updateConsumerEService(Mockito.any());


        assertNotNull(response);
        assertEquals(detailFromInterop.getState(), response.getState());

    }

}
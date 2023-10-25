package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import it.pagopa.interop.signalhub.updater.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.updater.model.AgreementEventDto;
import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.repository.ConsumerEserviceRepository;
import it.pagopa.interop.signalhub.updater.repository.cache.model.ConsumerEServiceCache;
import it.pagopa.interop.signalhub.updater.repository.cache.repository.ConsumerEServiceCacheRepository;
import it.pagopa.interop.signalhub.updater.service.InteropService;
import it.pagopa.interop.signalhub.updater.service.OrganizationService;
import org.junit.jupiter.api.BeforeEach;
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
    private ConsumerEserviceRepository consumerEserviceRepository;
    @Mock
    private ConsumerEServiceMapper mapper;
    @Mock
    private ConsumerEServiceCacheRepository consumerEServiceCacheRepository;

    private AgreementEventDto agreementEventDto;
    private ConsumerEServiceDto consumerEServiceDto;
    private ConsumerEService consumerEService;

    @BeforeEach
    void inizialize(){
        agreementEventDto= new AgreementEventDto();
        agreementEventDto.setAgreementId("123");
        agreementEventDto.setEventId(1l);
        agreementEventDto.setEventType("test");
        agreementEventDto.setObjectType("test");

        consumerEServiceDto= new ConsumerEServiceDto();
        consumerEServiceDto.setConsumerId("123");
        consumerEServiceDto.setEserviceId("123");
        consumerEServiceDto.setState("ACTIVE");
        consumerEServiceDto.setEventId(1l);

        consumerEService= new ConsumerEService();
        consumerEService.setConsumerId("123");
        consumerEService.setEserviceId("123");
        consumerEService.setState("TEST");
    }

    @Test
    void updateConsumer() {
        Mockito.when(interopService.getConsumerEService(Mockito.any(), Mockito.any())).thenReturn(consumerEServiceDto);
        Mockito.when(consumerEserviceRepository.findByEserviceIdAndConsumerId(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(mapper.toEntityFromProps(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(consumerEService);
        Mockito.when(organizationService.checkAndUpdate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new OrganizationEServiceDto());
        Mockito.when(consumerEserviceRepository.saveAndFlush(Mockito.any())).thenReturn(consumerEService);
        Mockito.when(mapper.toCacheFromEntity(Mockito.any())).thenReturn(new ConsumerEServiceCache());
        Mockito.when(mapper.toDtoFromEntity(Mockito.any())).thenReturn(new ConsumerEServiceDto());
        assertNotNull(consumerService.updateConsumer(agreementEventDto));
    }
}
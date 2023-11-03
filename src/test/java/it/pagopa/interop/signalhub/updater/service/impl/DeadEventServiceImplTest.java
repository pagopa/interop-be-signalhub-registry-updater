package it.pagopa.interop.signalhub.updater.service.impl;

import it.pagopa.interop.signalhub.updater.config.RegistryUpdaterProps;
import it.pagopa.interop.signalhub.updater.entity.DeadEvent;
import it.pagopa.interop.signalhub.updater.mapper.impl.DeadEventMapperImpl;
import it.pagopa.interop.signalhub.updater.model.EServiceEventDto;
import it.pagopa.interop.signalhub.updater.model.EventDto;
import it.pagopa.interop.signalhub.updater.repository.DeadEventRepository;
import it.pagopa.interop.signalhub.updater.service.TracingBatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class DeadEventServiceImplTest {
    @InjectMocks
    private DeadEventServiceImpl deadEventService;
    @Mock
    private TracingBatchService tracingBatchService;
    @Mock
    private RegistryUpdaterProps registryUpdaterProps;
    @Mock
    private DeadEventRepository deadEventRepository;
    @Mock
    private DeadEventMapperImpl deadEventMapperImpl;


    @Test
    void whenOccuranceUnderAttemptPropsThenSaveOnDB() {
        EventDto eventDto= new EServiceEventDto();
        eventDto.setEventId(1L);
        Mockito.when(tracingBatchService.countBatchInErrorWithLastEventId(Mockito.any()))
                .thenReturn(1);
        Mockito.when(registryUpdaterProps.getAttemptEvent()).thenReturn(5);

        assertNull(deadEventService.saveDeadEvent(eventDto));

        Mockito.verify(deadEventRepository, Mockito.timeout(1000).times(0))
                .saveAndFlush(Mockito.any());
    }

    @Test
    void whenOccuranceOverAttemptPropsThenSaveOnDB(){
        EventDto eventDto= new EServiceEventDto();
        eventDto.setEventId(1L);

        Mockito.when(tracingBatchService.countBatchInErrorWithLastEventId(Mockito.any()))
                .thenReturn(4);

        Mockito.when(registryUpdaterProps.getAttemptEvent()).thenReturn(2);

        Mockito.when(deadEventRepository.saveAndFlush(Mockito.any())).thenReturn(new DeadEvent());
        Mockito.when(deadEventMapperImpl.toDeadEvent(Mockito.any())).thenReturn(new DeadEvent());

        assertNull(deadEventService.saveDeadEvent(eventDto));

        Mockito.verify(deadEventRepository, Mockito.timeout(1000).times(1))
                .saveAndFlush(Mockito.any());
    }
}
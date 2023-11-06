package it.pagopa.interop.signalhub.updater.cache.repository;

import it.pagopa.interop.signalhub.updater.cache.model.ConsumerEServiceCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ConsumerEServiceCacheRepositoryTest {
    @InjectMocks
    private ConsumerEServiceCacheRepository consumerEServiceCacheRepository;
    @Mock
    private RedisTemplate<String, ConsumerEServiceCache> redisTemplate;


    @Test
    void updateConsumerEServiceTest() {
        ListOperations list = mock(ListOperations.class);
        ConsumerEServiceCache consumerEServiceCache = new ConsumerEServiceCache();
        consumerEServiceCache.setEserviceId("09662b6b-5cbf-4bb8-b353-e23d7af6cb5e");
        consumerEServiceCache.setConsumerId("84871fd4-2fd7-46ab-9d22-f6b452f4b3c5");

        when(redisTemplate.opsForList())
                .thenReturn(list);

        when(list.indexOf(any(), any()))
                .thenReturn(10L);
        Assertions.assertDoesNotThrow(() -> consumerEServiceCacheRepository.updateConsumerEService(consumerEServiceCache));
  }

    @Test
    void updateConsumerEServiceTestWhenConsumerIsNotInCacheTest() {
        ListOperations list = mock(ListOperations.class);
        ConsumerEServiceCache consumerEServiceCache = new ConsumerEServiceCache();
        consumerEServiceCache.setEserviceId("09662b6b-5cbf-4bb8-b353-e23d7af6cb5e");
        consumerEServiceCache.setConsumerId("84871fd4-2fd7-46ab-9d22-f6b452f4b3c5");

        when(redisTemplate.opsForList())
                .thenReturn(list);

        when(list.indexOf(any(), any()))
                .thenReturn(null);

        Assertions.assertDoesNotThrow(() -> consumerEServiceCacheRepository.updateConsumerEService(consumerEServiceCache));
    }
}
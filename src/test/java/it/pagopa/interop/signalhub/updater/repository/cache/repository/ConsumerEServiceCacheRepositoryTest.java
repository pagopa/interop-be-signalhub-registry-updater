package it.pagopa.interop.signalhub.updater.repository.cache.repository;

import it.pagopa.interop.signalhub.updater.repository.cache.model.ConsumerEServiceCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConsumerEServiceCacheRepositoryTest {

    @InjectMocks
    private ConsumerEServiceCacheRepository consumerEServiceCacheRepository;

    @Mock
    private RedisTemplate<String, ConsumerEServiceCache> redisTemplate;

    @Mock
    private ValueOperations<String, ConsumerEServiceCache> valueOperations;

    private ConsumerEServiceCache consumerEServiceCache;

    @BeforeEach
    void inizialize(){
        consumerEServiceCache= new ConsumerEServiceCache();
        consumerEServiceCache.setEserviceId("123");
        consumerEServiceCache.setConsumerId("123");
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void updateConsumerEService() {
        Mockito.when(valueOperations.get(Mockito.any())).thenReturn(consumerEServiceCache);

    }

    @Test
    void findById() {
    }
}
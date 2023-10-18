package it.pagopa.interop.signalhub.updater.repository.cache.repository;


import it.pagopa.interop.signalhub.updater.repository.cache.model.ConsumerEServiceCache;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;



@Repository
public class ConsumerEServiceCacheRepository {
    private RedisTemplate<String, ConsumerEServiceCache> redisTemplate;
    private HashOperations hashOperations;

    public ConsumerEServiceCacheRepository(RedisTemplate<String, ConsumerEServiceCache> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    public void updateConsumerEService(ConsumerEServiceCache item){
        hashOperations.put("consumer_eservice", item.getEserviceId().concat("-").concat(item.getConsumerId()),item);
    }



}

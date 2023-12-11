package it.pagopa.interop.signalhub.updater.cache.repository;


import it.pagopa.interop.signalhub.updater.cache.model.ConsumerEServiceCache;
import it.pagopa.interop.signalhub.updater.utility.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class ConsumerEServiceCacheRepository {
    @Autowired
    @Qualifier("RedisTemplateConsumer")
    private RedisTemplate<String, ConsumerEServiceCache>  redisTemplate;


    public void updateConsumerEService(ConsumerEServiceCache item){
        Long index = this.findByEservice(item);
        if(index != null) {
            redisTemplate.opsForList().set(Utils.getCacheKey(item.getEserviceId(), item.getConsumerId()), index, item);
            log.info("Redis update OrganizationEService: {} ", item.getEserviceId());
        }
    }

    private Long findByEservice(ConsumerEServiceCache consumerEServiceCache) {
        return redisTemplate.opsForList().indexOf(Utils.getCacheKey(consumerEServiceCache.getEserviceId(), consumerEServiceCache.getConsumerId()), consumerEServiceCache);
    }
}
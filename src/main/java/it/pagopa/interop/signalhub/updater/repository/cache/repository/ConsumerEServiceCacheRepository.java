package it.pagopa.interop.signalhub.updater.repository.cache.repository;


import it.pagopa.interop.signalhub.updater.repository.cache.model.ConsumerEServiceCache;
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

    public void updateOrganizationEService(ConsumerEServiceCache item){
        Long index = this.findByEservice(item);
        if(index != null) {
            redisTemplate.opsForList().set(item.getEserviceId().concat("-").concat(item.getConsumerId()), index, item);
            log.info("Redis update OrganizationEService: {} ", item.getEserviceId());
        }
    }

    private Long findByEservice(ConsumerEServiceCache consumerEServiceCache) {
        return redisTemplate.opsForList().indexOf(consumerEServiceCache.getEserviceId().concat("-").concat(consumerEServiceCache.getConsumerId()), consumerEServiceCache);
    }


}

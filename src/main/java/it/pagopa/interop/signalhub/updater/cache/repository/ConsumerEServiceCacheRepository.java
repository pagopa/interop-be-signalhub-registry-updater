package it.pagopa.interop.signalhub.updater.cache.repository;


import it.pagopa.interop.signalhub.updater.cache.model.ConsumerEServiceCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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
        if(ObjectUtils.isNotEmpty(findById(item.getConsumerId(), item.getEserviceId()))) {
            redisTemplate.opsForValue().set(item.getEserviceId().concat("-").concat(item.getConsumerId()), item);
            log.info("Redis update ConsumerEService: {} ", item.getEserviceId().concat("-").concat(item.getConsumerId()));
        }
    }

    public ConsumerEServiceCache findById(String consumer, String eservice) {
        return redisTemplate.opsForValue().get(eservice.concat("-").concat(consumer));
    }



}

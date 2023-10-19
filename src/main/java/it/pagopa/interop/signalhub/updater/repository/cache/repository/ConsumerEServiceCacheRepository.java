package it.pagopa.interop.signalhub.updater.repository.cache.repository;


import it.pagopa.interop.signalhub.updater.repository.cache.model.ConsumerEServiceCache;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;



@Repository
public class ConsumerEServiceCacheRepository {
    @Autowired
    @Qualifier("RedisTemplateConsumer")
    private RedisTemplate<String,ConsumerEServiceCache>  redisTemplate;



    public void updateConsumerEService(ConsumerEServiceCache item){

        redisTemplate.opsForValue().set(item.getEserviceId().concat("-").concat(item.getConsumerId()), item);

    }



}

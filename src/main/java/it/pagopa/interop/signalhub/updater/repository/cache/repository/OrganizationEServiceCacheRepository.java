package it.pagopa.interop.signalhub.updater.repository.cache.repository;


import it.pagopa.interop.signalhub.updater.repository.cache.model.ConsumerEServiceCache;
import it.pagopa.interop.signalhub.updater.repository.cache.model.OrganizationEServiceCache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class OrganizationEServiceCacheRepository {
    @Autowired
    private RedisTemplate redisTemplate;



    public void updateOrganizationEService(OrganizationEServiceCache item){

        redisTemplate.opsForValue().set(item.getEserviceId(), item);

    }
}

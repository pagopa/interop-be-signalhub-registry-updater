package it.pagopa.interop.signalhub.updater.repository.cache.repository;


import it.pagopa.interop.signalhub.updater.repository.cache.model.OrganizationEServiceCache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
@AllArgsConstructor
public class OrganizationEServiceCacheRepository {

    private RedisTemplate<String, OrganizationEServiceCache> redisTemplate;
    private HashOperations hashOperations;

    public OrganizationEServiceCacheRepository(RedisTemplate<String, OrganizationEServiceCache> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    public void updateEService(OrganizationEServiceCache item){
        hashOperations.put("eservices", item.getEserviceId(),item);
        log.info("update :", item.getEserviceId());
    }
}

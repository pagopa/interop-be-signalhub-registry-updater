package it.pagopa.interop.signalhub.updater.cache.repository;


import it.pagopa.interop.signalhub.updater.cache.model.OrganizationEServiceCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class OrganizationEServiceCacheRepository {
    @Qualifier("RedisTemplateOrganization")
    @Autowired
    private RedisTemplate<String, OrganizationEServiceCache>  redisTemplate;


    public void updateOrganizationEService(OrganizationEServiceCache item){
        Long index = this.findByEservice(item);
        if(index != null) {
            redisTemplate.opsForList().set(item.getEserviceId(), index, item);
            log.info("Redis update OrganizationEService: {} ", item.getEserviceId());
        }
    }

    private Long findByEservice(OrganizationEServiceCache eServiceCache) {
        return redisTemplate.opsForList().indexOf(eServiceCache.getEserviceId(), eServiceCache);
    }

}

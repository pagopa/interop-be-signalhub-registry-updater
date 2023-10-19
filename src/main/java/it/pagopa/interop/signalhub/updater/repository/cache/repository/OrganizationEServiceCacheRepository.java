package it.pagopa.interop.signalhub.updater.repository.cache.repository;


import it.pagopa.interop.signalhub.updater.repository.cache.model.OrganizationEServiceCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class OrganizationEServiceCacheRepository {
    @Qualifier("RedisTemplateOrganization")
    @Autowired
    private RedisTemplate<String, OrganizationEServiceCache>  redisTemplate;



    public void updateOrganizationEService(OrganizationEServiceCache item){

        redisTemplate.opsForValue().set(item.getEserviceId(), item);

    }
}

package it.pagopa.interop.signalhub.updater.repository.cache;

import it.pagopa.interop.signalhub.updater.repository.cache.model.EServiceCache;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;


@Repository
@AllArgsConstructor
public class OrganizationCacheRepository {
    private final ReactiveRedisOperations<String, EServiceCache> reactiveRedisOperations;

    private Flux<EServiceCache> findAll(){
        return this.reactiveRedisOperations.opsForList().range("eservices", 0, -1);
    }

    public Mono<EServiceCache> findById(String organization, String eservice) {
        return this.findAll()
                .filter(correctEservice(organization, eservice))
                .collectList()
                .flatMap(finded -> {
                    if (finded.isEmpty()) return Mono.empty();
                    return Mono.just(finded.get(finded.size()-1));
                });
    }


    public Mono<EServiceCache> save(EServiceCache eservice){
        return this.reactiveRedisOperations.opsForList().rightPush("eservices", eservice).thenReturn(eservice);
    }

    private Predicate<EServiceCache> correctEservice(String organizationId, String eserviceId){
        return eservice -> eservice.getEserviceId().equals(eserviceId) &&
                eservice.getOrganizationId().equals(organizationId);
    }


}

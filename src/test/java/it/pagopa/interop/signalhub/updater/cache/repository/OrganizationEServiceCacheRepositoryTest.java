package it.pagopa.interop.signalhub.updater.cache.repository;

import it.pagopa.interop.signalhub.updater.cache.model.OrganizationEServiceCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrganizationEServiceCacheRepositoryTest {
    @InjectMocks
    private OrganizationEServiceCacheRepository organizationEServiceCacheRepository;
    @Mock
    private RedisTemplate<String, OrganizationEServiceCache> redisTemplate;


    @Test
    void updateOrganizationEServiceTest() {
        ListOperations list = mock(ListOperations.class);
        OrganizationEServiceCache organizationEServiceCache = new OrganizationEServiceCache();
        organizationEServiceCache.setEserviceId("09662b6b-5cbf-4bb8-b353-e23d7af6cb5e");
        organizationEServiceCache.setProducerId("84871fd4-2fd7-46ab-9d22-f6b452f4b3c5");

        when(redisTemplate.opsForList())
                .thenReturn(list);

        when(list.indexOf(any(), any()))
                .thenReturn(10L);

        Assertions.assertDoesNotThrow(() -> organizationEServiceCacheRepository.updateOrganizationEService(organizationEServiceCache));}

    @Test
    void updateOrganizationEServiceWhenOrganizationIsNotInCacheTest() {
        ListOperations list = mock(ListOperations.class);
        OrganizationEServiceCache organizationEServiceCache = new OrganizationEServiceCache();
        organizationEServiceCache.setEserviceId("09662b6b-5cbf-4bb8-b353-e23d7af6cb5e");
        organizationEServiceCache.setProducerId("84871fd4-2fd7-46ab-9d22-f6b452f4b3c5");

        when(redisTemplate.opsForList())
                .thenReturn(list);

        when(list.indexOf(any(), any()))
                .thenReturn(null);

        Assertions.assertDoesNotThrow(() -> organizationEServiceCacheRepository.updateOrganizationEService(organizationEServiceCache));
    }
}
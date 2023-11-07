package it.pagopa.interop.signalhub.updater.cache.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.time.Instant;


class OrganizationEServiceCacheTest {
    private String eserviceId;
    private String producerId;
    private String descriptorId;
    private String state;
    private Timestamp tmstInsert;
    private Timestamp tmstLastEdit;


    @BeforeEach
    void preTest(){
        this.setUp();
    }

    @Test
    void equalsTest() {
        OrganizationEServiceCache organizationEServiceCacheA = getOrganizationEServiceCache();
        OrganizationEServiceCache organizationEServiceCacheB = getOrganizationEServiceCache();
        Assertions.assertTrue(organizationEServiceCacheA.equals(organizationEServiceCacheB) && organizationEServiceCacheB.equals(organizationEServiceCacheA));

        String toFail = "";
        Assertions.assertNotEquals(organizationEServiceCacheA, toFail);
    }

    @Test
    void hashCodeTest() {
        OrganizationEServiceCache organizationEServiceCacheA = getOrganizationEServiceCache();
        OrganizationEServiceCache organizationEServiceCacheB = getOrganizationEServiceCache();
        Assertions.assertTrue(organizationEServiceCacheA.equals(organizationEServiceCacheB) && organizationEServiceCacheB.equals(organizationEServiceCacheA));
        Assertions.assertEquals(organizationEServiceCacheA.hashCode(), organizationEServiceCacheB.hashCode());
    }

    private OrganizationEServiceCache getOrganizationEServiceCache() {
        OrganizationEServiceCache organizationEServiceCache = new OrganizationEServiceCache();
        organizationEServiceCache.setEserviceId(eserviceId);
        organizationEServiceCache.setProducerId(producerId);
        organizationEServiceCache.setDescriptorId(descriptorId);
        organizationEServiceCache.setState(state);
        organizationEServiceCache.setTmstInsert(tmstInsert);
        organizationEServiceCache.setTmstLastEdit(tmstLastEdit);

        return organizationEServiceCache;
    }

    private void setUp() {
        this.eserviceId = "01928-37465";
        this.producerId = "09876-54321";
        this.descriptorId = "09876-332244";
        this.state = "ESERVICE";
        this.tmstInsert = Timestamp.from(Instant.parse("2023-10-20T18:15:00.000Z"));
        this.tmstLastEdit = Timestamp.from(Instant.parse("2023-10-22T08:40:00.000Z"));
    }
}

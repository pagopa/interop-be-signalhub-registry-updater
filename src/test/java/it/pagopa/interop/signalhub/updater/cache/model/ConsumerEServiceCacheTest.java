package it.pagopa.interop.signalhub.updater.cache.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.time.Instant;


class ConsumerEServiceCacheTest {
    private String eserviceId;
    private String consumerId;
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
        ConsumerEServiceCache consumerEServiceCacheA = getConsumerEServiceCache();
        ConsumerEServiceCache consumerEServiceCacheB = getConsumerEServiceCache();
        Assertions.assertTrue(consumerEServiceCacheA.equals(consumerEServiceCacheB) && consumerEServiceCacheB.equals(consumerEServiceCacheA));

        String toFail = "";
        Assertions.assertNotEquals(consumerEServiceCacheA, toFail);
    }

    @Test
    void hashCodeTest() {
        ConsumerEServiceCache consumerEServiceCacheA = getConsumerEServiceCache();
        ConsumerEServiceCache consumerEServiceCacheB = getConsumerEServiceCache();
        Assertions.assertTrue(consumerEServiceCacheA.equals(consumerEServiceCacheB) && consumerEServiceCacheB.equals(consumerEServiceCacheA));
        Assertions.assertEquals(consumerEServiceCacheA.hashCode(), consumerEServiceCacheB.hashCode());
    }

    private ConsumerEServiceCache getConsumerEServiceCache() {
        ConsumerEServiceCache consumerEServiceCache = new ConsumerEServiceCache();
        consumerEServiceCache.setEserviceId(eserviceId);
        consumerEServiceCache.setConsumerId(consumerId);
        consumerEServiceCache.setDescriptorId(descriptorId);
        consumerEServiceCache.setState(state);
        consumerEServiceCache.setTmstInsert(tmstInsert);
        consumerEServiceCache.setTmstLastEdit(tmstLastEdit);
        return consumerEServiceCache;
    }

    private void setUp() {
        this.eserviceId = "01928-37465";
        this.consumerId = "12345-67890";
        this.descriptorId = "98214-75100";
        this.state = "AGREEMENT";
        this.tmstInsert = Timestamp.from(Instant.parse("2023-10-20T18:15:00.000Z"));
        this.tmstLastEdit = Timestamp.from(Instant.parse("2023-10-22T08:40:00.000Z"));
    }
}

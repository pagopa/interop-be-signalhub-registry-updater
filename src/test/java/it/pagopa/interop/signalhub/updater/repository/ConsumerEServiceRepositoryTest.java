package it.pagopa.interop.signalhub.updater.repository;

import it.pagopa.interop.signalhub.updater.config.BaseTest;
import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;


class ConsumerEServiceRepositoryTest extends BaseTest.WithJpa {
    private static final String correctEservice = "1234-eservice-correct";
    private static final String correctConsumer = "1234-consumer-correct";
    private static final String correctDescriptor = "1234-descriptor-correct";

    @Autowired
    private ConsumerEServiceRepository repository;

    @BeforeEach
    void setUp(){
        repository.saveAndFlush(getEntity());
    }

    @Test
    void whenFindConsumerWithBadlyParamThenReturnNull(){
        Optional<ConsumerEService> entity =
                repository.findByEserviceIdAndConsumerIdAndDescriptorId(
                        correctEservice.replace("-", ""),
                        correctConsumer.replace("-", ""),
                        correctDescriptor.replace("-", ""));

        Assertions.assertNotNull(entity);
        Assertions.assertFalse(entity.isPresent());
    }


    @Test
    void whenFindConsumerWithCorrectParamThenReturnEntity(){
        Optional<ConsumerEService> entity =
                repository.findByEserviceIdAndConsumerIdAndDescriptorId(correctEservice, correctConsumer, correctDescriptor);

        Assertions.assertNotNull(entity);
        Assertions.assertTrue(entity.isPresent());
        Assertions.assertNotNull(entity.get());
    }


    private ConsumerEService getEntity(){
        ConsumerEService entity = new ConsumerEService();
        entity.setEserviceId(correctEservice);
        entity.setConsumerId(correctConsumer);
        entity.setDescriptorId(correctDescriptor);
        entity.setState("ACTIVE");
        entity.setTmstInsert(Timestamp.from(Instant.now()));
        return entity;
    }
}
package it.pagopa.interop.signalhub.updater.repository;

import it.pagopa.interop.signalhub.updater.config.BaseTest;
import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;


class OrganizationEServiceRepositoryTest extends BaseTest.WithJpa {
    private static final String correctEservice = "1234-eservice-correct";
    private static final String correctProducer = "1234-producer-correct";
    private static final String correctDescriptorId = "1234-descriptor-correct";

    @Autowired
    private OrganizationEServiceRepository organizationEServiceRepository;

    @BeforeEach
    void setUp(){
        organizationEServiceRepository.saveAndFlush(getEntity());
    }

    @Test
    void whenFindOrganizationWithBadlyParamThenReturnNull(){
        Optional<OrganizationEService> entity =
                organizationEServiceRepository.findByEserviceIdAndProducerIdAndDescriptorId(
                        correctEservice.replace("-", ""),
                        correctProducer.replace("-", ""),
                        correctDescriptorId.replace("-", ""));

        Assertions.assertNotNull(entity);
        Assertions.assertFalse(entity.isPresent());
    }


    @Test
    void whenFindOrganizationWithCorrectParamThenReturnEntity(){
        Optional<OrganizationEService> entity =
                organizationEServiceRepository.findByEserviceIdAndProducerIdAndDescriptorId(correctEservice, correctProducer, correctDescriptorId);

        Assertions.assertNotNull(entity);
        Assertions.assertTrue(entity.isPresent());
        Assertions.assertNotNull(entity.get());
    }


    private OrganizationEService getEntity(){
        OrganizationEService entity = new OrganizationEService();
        entity.setEserviceId(correctEservice);
        entity.setProducerId(correctProducer);
        entity.setDescriptorId(correctDescriptorId);
        entity.setState("ACTIVE");
        entity.setTmstInsert(Timestamp.from(Instant.now()));
        return entity;
    }
}
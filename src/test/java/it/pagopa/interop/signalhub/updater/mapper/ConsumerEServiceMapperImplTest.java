package it.pagopa.interop.signalhub.updater.mapper;

import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Agreement;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.AgreementState;
import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.cache.model.ConsumerEServiceCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;


class ConsumerEServiceMapperImplTest {
    private Long eventId;
    private String eserviceId;
    private String consumerId;
    private String agreementId;
    private String descriptorId;
    private AgreementState agreementState;
    private String consumerState;
    private String producerId;
    private Timestamp tmstInsert;
    private Timestamp tmstLastEdit;
    private ConsumerEServiceMapper consumerEServiceMapper;


    @BeforeEach
    void preTest(){
        this.setUp();
    }

    @Test
    void toConsumerEServiceDtoFromAgreementTest() {
        Agreement agreement = getAgreement();
        ConsumerEServiceDto consumerEServiceDto = consumerEServiceMapper.toConsumerEServiceDtoFromAgreement(agreement, agreementId,  eventId);
        assertNotNull(consumerEServiceDto);
        assertEquals(eventId, consumerEServiceDto.getEventId());
        assertEquals(eserviceId, consumerEServiceDto.getEserviceId());
        assertEquals(consumerId, consumerEServiceDto.getConsumerId());
        assertEquals(agreementId, consumerEServiceDto.getAgreementId());
        assertEquals(producerId, consumerEServiceDto.getProducerId());
        assertEquals(descriptorId, consumerEServiceDto.getDescriptorId());
        assertEquals(consumerState, consumerEServiceDto.getState());
    }

    @Test
    void toConsumerEServiceDtoFromAgreementNullCaseTest() {
        Agreement agreement = null;
        ConsumerEServiceDto consumerEServiceDto = consumerEServiceMapper.toConsumerEServiceDtoFromAgreement(agreement, agreementId, null);
        assertNull(consumerEServiceDto);

        consumerEServiceDto = consumerEServiceMapper.toConsumerEServiceDtoFromAgreement(agreement, agreementId, eventId);
        assertNotNull(consumerEServiceDto);

        agreement = getAgreement();
        consumerEServiceDto = consumerEServiceMapper.toConsumerEServiceDtoFromAgreement(agreement, agreementId, null);
        assertNotNull(consumerEServiceDto);

        agreement = new Agreement();
        consumerEServiceDto = consumerEServiceMapper.toConsumerEServiceDtoFromAgreement(agreement, agreementId, eventId);
        assertNotNull(consumerEServiceDto);
    }


    @Test
    void toDtoFromEntityTest() {
        ConsumerEService consumerEService = getConsumerEService();
        ConsumerEServiceDto consumerEServiceDto = consumerEServiceMapper.toDtoFromEntity(consumerEService);
        assertNotNull(consumerEServiceDto);
        assertEquals(eventId, consumerEServiceDto.getEventId());
        assertEquals(eserviceId, consumerEServiceDto.getEserviceId());
        assertEquals(consumerId, consumerEServiceDto.getConsumerId());
        assertEquals(agreementId, consumerEServiceDto.getAgreementId());
        assertEquals(descriptorId, consumerEServiceDto.getDescriptorId());
        assertEquals(consumerState, consumerEServiceDto.getState());
    }

    @Test
    void toDtoFromEntityNullCaseTest() {
        ConsumerEService consumerEService = null;
        ConsumerEServiceDto consumerEServiceDto = consumerEServiceMapper.toDtoFromEntity(consumerEService);
        assertNull(consumerEServiceDto);
    }

    @Test
    void toEntityFromPropsTest() {
        ConsumerEService consumerEService = consumerEServiceMapper.toEntityFromProps(eserviceId,
                consumerId,
                agreementId,
                descriptorId,
                consumerState);
        assertNotNull(consumerEService);
        assertEquals(eserviceId, consumerEService.getEserviceId());
        assertEquals(consumerId, consumerEService.getConsumerId());
        assertEquals(agreementId, consumerEService.getAgreementId());
        assertEquals(descriptorId, consumerEService.getDescriptorId());
        assertEquals(consumerState, consumerEService.getState());
    }

    @Test
    void toEntityFromPropsNullCaseTest() {
        ConsumerEService consumerEService = consumerEServiceMapper.toEntityFromProps(null, null, null, null, null);
        assertNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(eserviceId, null, null, null, null);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(eserviceId, consumerId, null, null, null);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(eserviceId, consumerId, agreementId, null, null);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(eserviceId, consumerId, agreementId, descriptorId, null);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, consumerId, agreementId, descriptorId, consumerState);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, null, agreementId, descriptorId, consumerState);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, null, null, descriptorId, consumerState);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, null, null, null, consumerState);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, consumerId, null, null, null);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, consumerId, agreementId, null, null);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, consumerId, agreementId, descriptorId, null);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, consumerId, agreementId, descriptorId, null);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, null, agreementId, null, null);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, null, agreementId, descriptorId, null);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, null, agreementId, descriptorId, null);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, null, null, descriptorId, null);
        assertNotNull(consumerEService);
    }

    @Test
    void toCacheFromEntityTest() {
        ConsumerEService consumerEService = getConsumerEService();
        ConsumerEServiceCache consumerEServiceCache = consumerEServiceMapper.toCacheFromEntity(consumerEService);
        assertNotNull(consumerEServiceCache);
        assertEquals(eserviceId, consumerEServiceCache.getEserviceId());
        assertEquals(consumerId, consumerEServiceCache.getConsumerId());
        assertEquals(descriptorId, consumerEServiceCache.getDescriptorId());
        assertEquals(consumerState, consumerEServiceCache.getState());
        assertEquals(tmstInsert, consumerEServiceCache.getTmstInsert());
        assertEquals(tmstLastEdit, consumerEServiceCache.getTmstLastEdit());
    }

    @Test
    void toCacheFromEntityNullCaseTest() {
        ConsumerEService consumerEService = null;
        ConsumerEServiceCache consumerEServiceCache = consumerEServiceMapper.toCacheFromEntity(consumerEService);
        assertNull(consumerEServiceCache);
    }

    private Agreement getAgreement() {
        Agreement agreement = new Agreement();
        String eserviceId = "0f14d0ab9605-4a62-a9e4-5ed26688389b".replace("-", "");
        UUID uuid = new UUID(new BigInteger(eserviceId.substring(0, 16), 16).longValue(),
                new BigInteger(eserviceId.substring(16), 16).longValue());
        agreement.setEserviceId(uuid);

        String consumerId = "4a620f14d0ab-9605-a9e4-5ed26688389b".replace("-", "");
        uuid = new UUID(new BigInteger(consumerId.substring(0, 16), 16).longValue(),
                new BigInteger(consumerId.substring(16), 16).longValue());
        agreement.setConsumerId(uuid);

        String producerId = "5ed26688389b-4a62-9605-0f14d0ab9605".replace("-", "");
        uuid = new UUID(new BigInteger(producerId.substring(0, 16), 16).longValue(),
                new BigInteger(producerId.substring(16), 16).longValue());
        agreement.setProducerId(uuid);

        String descriptorId = "8a631cab-797b-482d-bf7d-615bf1004368".replace("-", "");
        uuid = new UUID(new BigInteger(descriptorId.substring(0, 16), 16).longValue(),
                new BigInteger(descriptorId.substring(16), 16).longValue());
        agreement.setDescriptorId(uuid);
        agreement.setState(agreementState);

        return agreement;
    }

    private ConsumerEService getConsumerEService() {
        ConsumerEService consumerEService = new ConsumerEService();
        consumerEService.setEventId(eventId);
        consumerEService.setEserviceId(eserviceId);
        consumerEService.setConsumerId(consumerId);
        consumerEService.setAgreementId(agreementId);
        consumerEService.setDescriptorId(descriptorId);
        consumerEService.setState(consumerState);
        consumerEService.setTmstInsert(tmstInsert);
        consumerEService.setTmstLastEdit(tmstLastEdit);
        return consumerEService;
    }

    private void setUp() {
        this.eventId = 0L;
        this.eserviceId = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
        this.consumerId = "4a620f14-d0ab-9605-a9e4-5ed26688389b";
        this.producerId = "5ed26688-389b-4a62-9605-0f14d0ab9605";
        this.agreementId = "01920f14-d0ab-9605-a9e4-374650192842";
        this.descriptorId = "8a631cab-797b-482d-bf7d-615bf1004368";
        this.agreementState = AgreementState.ACTIVE;
        this.consumerState = AgreementState.ACTIVE.toString();
        this.tmstInsert = Timestamp.from(Instant.parse("2023-10-20T18:15:00.000Z"));
        this.tmstLastEdit = Timestamp.from(Instant.parse("2023-10-22T08:40:00.000Z"));
        this.consumerEServiceMapper = Mappers.getMapper(ConsumerEServiceMapper.class);
    }
}
package it.pagopa.interop.signalhub.updater.mapper;

import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Agreement;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.AgreementState;
import it.pagopa.interop.signalhub.updater.model.ConsumerEServiceDto;
import it.pagopa.interop.signalhub.updater.repository.cache.model.ConsumerEServiceCache;
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
        ConsumerEServiceDto consumerEServiceDto = consumerEServiceMapper.toConsumerEServiceDtoFromAgreement(agreement, this.eventId);
        assertNotNull(consumerEServiceDto);
        assertEquals(consumerEServiceDto.getEventId(), this.eventId);
        assertEquals(consumerEServiceDto.getEserviceId(), this.eserviceId);
        assertEquals(consumerEServiceDto.getConsumerId(), this.consumerId);
        assertEquals(consumerEServiceDto.getProducerId(), this.producerId);
        assertEquals(consumerEServiceDto.getState(), this.consumerState);
    }

    @Test
    void toConsumerEServiceDtoFromAgreementNullCaseTest() {
        Agreement agreement = null;
        ConsumerEServiceDto consumerEServiceDto = consumerEServiceMapper.toConsumerEServiceDtoFromAgreement(agreement, null);
        assertNull(consumerEServiceDto);

        consumerEServiceDto = consumerEServiceMapper.toConsumerEServiceDtoFromAgreement(agreement, this.eventId);
        assertNotNull(consumerEServiceDto);

        agreement = getAgreement();
        consumerEServiceDto = consumerEServiceMapper.toConsumerEServiceDtoFromAgreement(agreement, null);
        assertNotNull(consumerEServiceDto);

        agreement = new Agreement();
        consumerEServiceDto = consumerEServiceMapper.toConsumerEServiceDtoFromAgreement(agreement, this.eventId);
        assertNotNull(consumerEServiceDto);
    }


    @Test
    void toDtoFromEntityTest() {
        ConsumerEService consumerEService = getConsumerEService();
        ConsumerEServiceDto consumerEServiceDto = consumerEServiceMapper.toDtoFromEntity(consumerEService);
        assertNotNull(consumerEServiceDto);
        assertEquals(consumerEServiceDto.getEventId(), this.eventId);
        assertEquals(consumerEServiceDto.getEserviceId(), this.eserviceId);
        assertEquals(consumerEServiceDto.getConsumerId(), this.consumerId);
        assertEquals(consumerEServiceDto.getState(), this.consumerState);
    }

    @Test
    void toDtoFromEntityNullCaseTest() {
        ConsumerEService consumerEService = null;
        ConsumerEServiceDto consumerEServiceDto = consumerEServiceMapper.toDtoFromEntity(consumerEService);
        assertNull(consumerEServiceDto);
    }

    @Test
    void toEntityFromPropsTest() {
        ConsumerEService consumerEService = consumerEServiceMapper.toEntityFromProps(this.eserviceId, this.consumerId, this.consumerState);
        assertNotNull(consumerEService);
        assertEquals(consumerEService.getEserviceId(), this.eserviceId);
        assertEquals(consumerEService.getConsumerId(), this.consumerId);
        assertEquals(consumerEService.getState(), this.consumerState);
    }

    @Test
    void toEntityFromPropsNullCaseTest() {
        ConsumerEService consumerEService = consumerEServiceMapper.toEntityFromProps(null, null, null);
        assertNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(this.eserviceId, null, null);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(this.eserviceId, this.consumerId, null);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, this.consumerId, this.consumerState);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, null, this.consumerState);
        assertNotNull(consumerEService);

        consumerEService = consumerEServiceMapper.toEntityFromProps(null, this.consumerId, null);
        assertNotNull(consumerEService);
    }

    @Test
    void toCacheFromEntityTest() {
        ConsumerEService consumerEService = getConsumerEService();
        ConsumerEServiceCache consumerEServiceCache = consumerEServiceMapper.toCacheFromEntity(consumerEService);
        assertNotNull(consumerEServiceCache);
        assertEquals(consumerEServiceCache.getEserviceId(), this.eserviceId);
        assertEquals(consumerEServiceCache.getConsumerId(), this.consumerId);
        assertEquals(consumerEServiceCache.getState(), this.consumerState);
        assertEquals(consumerEServiceCache.getTmstInsert(), this.tmstInsert);
        assertEquals(consumerEServiceCache.getTmstLastEdit(), this.tmstLastEdit);
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
        agreement.setState(this.agreementState);
        return agreement;
    }

    private ConsumerEService getConsumerEService() {
        ConsumerEService consumerEService = new ConsumerEService();
        consumerEService.setEventId(this.eventId);
        consumerEService.setEserviceId(this.eserviceId);
        consumerEService.setConsumerId(this.consumerId);
        consumerEService.setState(this.consumerState);
        consumerEService.setTmstInsert(this.tmstInsert);
        consumerEService.setTmstLastEdit(this.tmstLastEdit);
        return consumerEService;
    }

    private void setUp() {
        this.eventId = 0L;
        this.eserviceId = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
        this.consumerId = "4a620f14-d0ab-9605-a9e4-5ed26688389b";
        this.producerId = "5ed26688-389b-4a62-9605-0f14d0ab9605";
        this.agreementState = AgreementState.ACTIVE;
        this.consumerState = AgreementState.ACTIVE.toString();
        this.tmstInsert = Timestamp.from(Instant.parse("2023-10-20T18:15:00.000Z"));
        this.tmstLastEdit = Timestamp.from(Instant.parse("2023-10-22T08:40:00.000Z"));
        this.consumerEServiceMapper = Mappers.getMapper(ConsumerEServiceMapper.class);
    }
}
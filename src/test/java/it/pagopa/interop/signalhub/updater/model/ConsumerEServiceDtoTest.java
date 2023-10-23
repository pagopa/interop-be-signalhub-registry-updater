package it.pagopa.interop.signalhub.updater.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsumerEServiceDtoTest {
    private Long eventId;
    private String eserviceId;
    private String producerId;
    private String consumerId;
    private String state;

    @BeforeEach
    void preTest(){
        this.setUp();
    }

    @Test
    void setGetTest() {
        ConsumerEServiceDto consumerEServiceDto = new ConsumerEServiceDto();
        Assertions.assertNotNull(consumerEServiceDto);

        consumerEServiceDto.setEventId(eventId);
        Assertions.assertEquals(eventId, consumerEServiceDto.getEventId());

        consumerEServiceDto.setEserviceId(eserviceId);
        Assertions.assertEquals(eserviceId, consumerEServiceDto.getEserviceId());

        consumerEServiceDto.setProducerId(producerId);
        Assertions.assertEquals(producerId, consumerEServiceDto.getProducerId());

        consumerEServiceDto.setConsumerId(consumerId);
        Assertions.assertEquals(consumerId, consumerEServiceDto.getConsumerId());

        consumerEServiceDto.setState(state);
        Assertions.assertEquals(state, consumerEServiceDto.getState());
    }

    @Test
    void toStringTest() {
        ConsumerEServiceDto consumerEServiceDto = getConsumerEServiceDto();
        Assertions.assertNotNull(consumerEServiceDto);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(consumerEServiceDto.getClass().getSimpleName());
        stringBuilder.append("(");
        stringBuilder.append("eventId=");
        stringBuilder.append(consumerEServiceDto.getEventId());
        stringBuilder.append(", ");
        stringBuilder.append("eserviceId=");
        stringBuilder.append(consumerEServiceDto.getEserviceId());
        stringBuilder.append(", ");
        stringBuilder.append("producerId=");
        stringBuilder.append(consumerEServiceDto.getProducerId());
        stringBuilder.append(", ");
        stringBuilder.append("consumerId=");
        stringBuilder.append(consumerEServiceDto.getConsumerId());
        stringBuilder.append(", ");
        stringBuilder.append("state=");
        stringBuilder.append(consumerEServiceDto.getState());
        stringBuilder.append(")");

        String toTest = stringBuilder.toString();
        Assertions.assertEquals(toTest, consumerEServiceDto.toString());
    }

    @Test
    void isNullTest() {
        ConsumerEServiceDto consumerEServiceDto = new ConsumerEServiceDto();
        Assertions.assertTrue(consumerEServiceDto.isNull());

        consumerEServiceDto.setEserviceId(eserviceId);
        Assertions.assertTrue(consumerEServiceDto.isNull());

        consumerEServiceDto.setConsumerId(consumerId);
        Assertions.assertTrue(consumerEServiceDto.isNull());

        consumerEServiceDto.setState(state);
        Assertions.assertFalse(consumerEServiceDto.isNull());
    }

    private ConsumerEServiceDto getConsumerEServiceDto() {
        ConsumerEServiceDto consumerEServiceDto = new ConsumerEServiceDto();
        consumerEServiceDto.setEventId(eventId);
        consumerEServiceDto.setEserviceId(eserviceId);
        consumerEServiceDto.setProducerId(producerId);
        consumerEServiceDto.setConsumerId(consumerId);
        consumerEServiceDto.setState(state);
        return consumerEServiceDto;
    }

    private void setUp() {
        this.eventId = 0L;
        this.eserviceId = "01928-37465";
        this.producerId = "09876-54321";
        this.consumerId = "12345-67890";
        this.state = "AGREEMENT";
    }
}

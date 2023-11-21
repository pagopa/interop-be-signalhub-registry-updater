package it.pagopa.interop.signalhub.updater.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class AgreementEventDtoTest {
    private String agreementId;
    private Long eventId;
    private String descriptorId;
    private String eventType;
    private String objectType;

    @BeforeEach
    void preTest(){
        this.setUp();
    }

    @Test
    void setGetTest() {
        AgreementEventDto agreementEventDto = new AgreementEventDto();
        Assertions.assertNotNull(agreementEventDto);

        agreementEventDto = getAgreementEventDto(agreementId);
        Assertions.assertNotNull(agreementEventDto);
        Assertions.assertEquals(agreementId, agreementEventDto.getAgreementId());

        agreementEventDto.setEventId(eventId);
        Assertions.assertEquals(eventId, agreementEventDto.getEventId());

        agreementEventDto.setObjectType(objectType);
        Assertions.assertEquals(objectType, agreementEventDto.getObjectType());

        agreementEventDto.setEventType(eventType);
        Assertions.assertEquals(eventType, agreementEventDto.getEventType());
    }

    @Test
    void hashCodeTest() {
        AgreementEventDto agreementEventDtoA = getAgreementEventDto(agreementId);
        AgreementEventDto agreementEventDtoB = getAgreementEventDto(agreementId);
        Assertions.assertTrue(agreementEventDtoA.equals(agreementEventDtoB) && agreementEventDtoB.equals(agreementEventDtoA));
        Assertions.assertEquals(agreementEventDtoA.hashCode(), agreementEventDtoB.hashCode());
    }

    @Test
    void equalsTest() {
        AgreementEventDto agreementEventDtoA = getAgreementEventDto(agreementId);
        AgreementEventDto agreementEventDtoB = getAgreementEventDto(agreementId);
        Assertions.assertTrue(agreementEventDtoA.equals(agreementEventDtoB) && agreementEventDtoB.equals(agreementEventDtoA));

        String toFail = "";
        Assertions.assertNotEquals(agreementEventDtoA, toFail);
    }

    @Test
    void toStringTest() {
        AgreementEventDto agreementEventDto = getAgreementEventDto(agreementId);
        Assertions.assertNotNull(agreementEventDto);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class ");
        stringBuilder.append(agreementEventDto.getClass().getSimpleName());
        stringBuilder.append(" {\n");
        stringBuilder.append("    eventId: ");
        stringBuilder.append(agreementEventDto.getEventId());
        stringBuilder.append("\n");
        stringBuilder.append("    agreementId: ");
        stringBuilder.append(agreementEventDto.getAgreementId());
        stringBuilder.append("\n}");

        String toTest = stringBuilder.toString();
        Assertions.assertEquals(toTest, agreementEventDto.toString());
    }

    private AgreementEventDto getAgreementEventDto(String agreementId) {
        AgreementEventDto agreementEventDto = new AgreementEventDto();
        agreementEventDto.setAgreementId(agreementId);
        agreementEventDto.setDescriptorId("1122-33344");
        agreementEventDto.setObjectType("ESERVICE");
        agreementEventDto.setEventType("ADDED");
        return agreementEventDto;
    }

    private void setUp() {
        this.agreementId = "09876-54321";
        this.eventId = 0L;
        this.eventType = "ADDED";
        this.objectType = "AGREEMENT";
        this.descriptorId ="1122-33344";
    }
}
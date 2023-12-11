package it.pagopa.interop.signalhub.updater.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class EServiceEventDtoTest {
    private String eServiceId;
    private String descriptorId;
    private Long eventId;
    private String eventType;
    private String objectType;

    @BeforeEach
    void preTest(){
        this.setUp();
    }

    @Test
    void setGetTest() {
        EServiceEventDto eServiceEventDto = new EServiceEventDto();
        Assertions.assertNotNull(eServiceEventDto);

        eServiceEventDto = getEServiceEventDto(eServiceId);
        Assertions.assertNotNull(eServiceEventDto);
        Assertions.assertEquals(eServiceId, eServiceEventDto.getEServiceId());

        eServiceEventDto.setEventId(eventId);
        Assertions.assertEquals(eventId, eServiceEventDto.getEventId());

        eServiceEventDto.setObjectType(objectType);
        Assertions.assertEquals(objectType, eServiceEventDto.getObjectType());

        eServiceEventDto.setEventType(eventType);
        Assertions.assertEquals(eventType, eServiceEventDto.getEventType());
    }

    @Test
    void hashCodeTest() {
        EServiceEventDto eServiceEventDtoA = getEServiceEventDto(eServiceId);
        EServiceEventDto eServiceEventDtoB = getEServiceEventDto(eServiceId);
        Assertions.assertTrue(eServiceEventDtoA.equals(eServiceEventDtoB) && eServiceEventDtoB.equals(eServiceEventDtoA));
        Assertions.assertEquals(eServiceEventDtoA.hashCode(), eServiceEventDtoB.hashCode());
    }

    @Test
    void equalsTest() {
        EServiceEventDto eServiceEventDtoA = getEServiceEventDto(eServiceId);
        EServiceEventDto eServiceEventDtoB = getEServiceEventDto(eServiceId);
        Assertions.assertTrue(eServiceEventDtoA.equals(eServiceEventDtoB) && eServiceEventDtoB.equals(eServiceEventDtoA));

        String toFail = "";
        Assertions.assertNotEquals(eServiceEventDtoA, toFail);
    }

    @Test
    void toStringTest() {
        EServiceEventDto eServiceEventDto = getEServiceEventDto(eServiceId);
        Assertions.assertNotNull(eServiceEventDto);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class ");
        stringBuilder.append(eServiceEventDto.getClass().getSimpleName());
        stringBuilder.append(" { ");
        stringBuilder.append("eventId: ");
        stringBuilder.append(eServiceEventDto.getEventId());
        stringBuilder.append(", ");
        stringBuilder.append("eServiceId: ");
        stringBuilder.append(eServiceEventDto.getEServiceId());
        stringBuilder.append(" }");

        String toTest = stringBuilder.toString();
        Assertions.assertEquals(toTest, eServiceEventDto.toString());
    }

    private EServiceEventDto getEServiceEventDto(String eServiceId) {
        EServiceEventDto eServiceEventDto = new EServiceEventDto();
        eServiceEventDto.setEServiceId(eServiceId);
        eServiceEventDto.setDescriptorId(eServiceId.concat("123"));
        eServiceEventDto.setObjectType(this.objectType);
        return eServiceEventDto;
    }

    private void setUp() {
        this.eServiceId = "09876-54321";
        this.eventId = 0L;
        this.eventType = "ADDED";
        this.objectType = "ESERVICE";
        this.descriptorId = "122211-22222";
    }
}
package it.pagopa.interop.signalhub.updater.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class OrganizationEServiceDtoTest {
    private Long eventId;
    private String eserviceId;
    private String producerId;
    private String state;


    @BeforeEach
    void preTest(){
        this.setUp();
    }

    @Test
    void setGetTest() {
        OrganizationEServiceDto organizationEServiceDto = new OrganizationEServiceDto();
        Assertions.assertNotNull(organizationEServiceDto);

        organizationEServiceDto.setEventId(eventId);
        Assertions.assertEquals(eventId, organizationEServiceDto.getEventId());

        organizationEServiceDto.setEserviceId(eserviceId);
        Assertions.assertEquals(eserviceId, organizationEServiceDto.getEserviceId());

        organizationEServiceDto.setProducerId(producerId);
        Assertions.assertEquals(producerId, organizationEServiceDto.getProducerId());

        organizationEServiceDto.setState(state);
        Assertions.assertEquals(state, organizationEServiceDto.getState());
    }

    @Test
    void toStringTest() {
        OrganizationEServiceDto organizationEServiceDto = getOrganizationEServiceDto();
        Assertions.assertNotNull(organizationEServiceDto);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(organizationEServiceDto.getClass().getSimpleName());
        stringBuilder.append("(");
        stringBuilder.append("eventId=");
        stringBuilder.append(organizationEServiceDto.getEventId());
        stringBuilder.append(", ");
        stringBuilder.append("eserviceId=");
        stringBuilder.append(organizationEServiceDto.getEserviceId());
        stringBuilder.append(", ");
        stringBuilder.append("producerId=");
        stringBuilder.append(organizationEServiceDto.getProducerId());
        stringBuilder.append(", ");
        stringBuilder.append("state=");
        stringBuilder.append(organizationEServiceDto.getState());
        stringBuilder.append(")");

        String toTest = stringBuilder.toString();
        Assertions.assertEquals(toTest, organizationEServiceDto.toString());
    }

    @Test
    void isNullTest() {
        OrganizationEServiceDto organizationEServiceDto = new OrganizationEServiceDto();
        Assertions.assertTrue(organizationEServiceDto.isNull());

        organizationEServiceDto.setEserviceId(eserviceId);
        Assertions.assertTrue(organizationEServiceDto.isNull());

        organizationEServiceDto.setProducerId(producerId);
        Assertions.assertTrue(organizationEServiceDto.isNull());

        organizationEServiceDto.setState(state);
        Assertions.assertFalse(organizationEServiceDto.isNull());
    }

    private OrganizationEServiceDto getOrganizationEServiceDto() {
        OrganizationEServiceDto organizationEServiceDto = new OrganizationEServiceDto();
        organizationEServiceDto.setEventId(eventId);
        organizationEServiceDto.setEserviceId(eserviceId);
        organizationEServiceDto.setProducerId(producerId);
        organizationEServiceDto.setState(state);
        return organizationEServiceDto;
    }

    private void setUp() {
        this.eventId = 0L;
        this.eserviceId = "01928-37465";
        this.producerId = "09876-54321";
        this.state = "AGREEMENT";
    }
}
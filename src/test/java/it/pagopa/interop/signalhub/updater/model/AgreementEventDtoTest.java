package it.pagopa.interop.signalhub.updater.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgreementEventDtoTest {
    private String agreementId;

    @BeforeEach
    void preTest(){
        this.setUp();
    }

    @Test
    void equalsTest() {
        AgreementEventDto agreementEventDtoA = getAgreementEventDto(agreementId);
        AgreementEventDto agreementEventDtoB = getAgreementEventDto(agreementId);
        Assertions.assertTrue(agreementEventDtoA.equals(agreementEventDtoB) && agreementEventDtoB.equals(agreementEventDtoA));
    }

    private AgreementEventDto getAgreementEventDto(String agreementId) {
        AgreementEventDto agreementEventDto = new AgreementEventDto();
        agreementEventDto.setAgreementId(agreementId);
        return agreementEventDto;
    }

    private void setUp() {
        this.agreementId = "09876-54321";
    }
}

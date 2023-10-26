package it.pagopa.interop.signalhub.updater.model;

import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.EServiceDescriptorState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class EServiceDescriptorDtoTest {
    private String state;
    @BeforeEach
    void preTest(){
        this.setUp();
    }

    @Test
    void setGetTest() {
        EServiceDescriptorDto eServiceDescriptorDto = new EServiceDescriptorDto();
        Assertions.assertNotNull(eServiceDescriptorDto);

        eServiceDescriptorDto = getEServiceDescriptor();
        Assertions.assertNotNull(eServiceDescriptorDto);
        Assertions.assertEquals(state, eServiceDescriptorDto.getState());
    }

    @Test
    void toStringTest() {
        EServiceDescriptorDto eServiceDescriptorDto = getEServiceDescriptor();
        Assertions.assertNotNull(eServiceDescriptorDto);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(eServiceDescriptorDto.getClass().getSimpleName());
        stringBuilder.append("(");
        stringBuilder.append("state=");
        stringBuilder.append(eServiceDescriptorDto.getState());
        stringBuilder.append(")");

        String toTest = stringBuilder.toString();
        Assertions.assertEquals(toTest, eServiceDescriptorDto.toString());
    }

    private EServiceDescriptorDto getEServiceDescriptor() {
        EServiceDescriptorDto eServiceDescriptorDto = new EServiceDescriptorDto();
        eServiceDescriptorDto.setState(state);
        return eServiceDescriptorDto;
    }

    private void setUp() {
        this.state = EServiceDescriptorState.ARCHIVED.toString();
    }
}

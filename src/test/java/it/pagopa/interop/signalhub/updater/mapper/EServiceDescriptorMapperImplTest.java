package it.pagopa.interop.signalhub.updater.mapper;

import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.EServiceDescriptor;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.EServiceDescriptorState;
import it.pagopa.interop.signalhub.updater.mapper.impl.EServiceDescriptorMapperImpl;
import it.pagopa.interop.signalhub.updater.model.EServiceDescriptorDto;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class EServiceDescriptorMapperImplTest {
    private String state;
    private Long eventId;
    private String eserviceId;
    private String producerId;
    private EServiceDescriptorMapperImpl eServiceDescriptorMapper;


    @BeforeEach
    void preTest(){
        this.setUp();
    }


    @Test
    void fromEServiceDescriptorDtoToOrganizationEServiceDtoTest() {
        EServiceDescriptorDto eServiceDescriptorDto = getEServiceDescriptorDto();
        OrganizationEServiceDto organizationEServiceDto = eServiceDescriptorMapper.fromEServiceDescriptorDtoToOrganizationEServiceDto(eServiceDescriptorDto, getOrganizationEServiceDto());
        assertNotNull(organizationEServiceDto);
        assertEquals(eventId, organizationEServiceDto.getEventId());
        assertEquals(eserviceId, organizationEServiceDto.getEserviceId());
        assertEquals(producerId, organizationEServiceDto.getProducerId());
        assertEquals(state, organizationEServiceDto.getState());
    }

    @Test
    void fromEServiceDescriptorDtoToOrganizationEServiceDtoNullCaseTest() {
        EServiceDescriptorDto eServiceDescriptorDto = null;
        OrganizationEServiceDto inputOrganizationEServiceDto = null;
        OrganizationEServiceDto organizationEServiceDto = eServiceDescriptorMapper.fromEServiceDescriptorDtoToOrganizationEServiceDto(eServiceDescriptorDto, inputOrganizationEServiceDto);
        assertNull(organizationEServiceDto);
    }

    @Test
    void fromEServiceDescriptorToEServiceDescriptorDtoTest() {
        EServiceDescriptor eServiceDescriptor = getEServiceDescriptor();
        EServiceDescriptorDto eServiceDescriptorDto = eServiceDescriptorMapper.fromEServiceDescriptorToEServiceDescriptorDto(eServiceDescriptor);
        assertNotNull(eServiceDescriptorDto);
        assertEquals(state, eServiceDescriptorDto.getState());
    }

    @Test
    void fromEServiceDescriptorToEServiceDescriptorDtoNullCaseTest() {
        EServiceDescriptor eServiceDescriptor = null;
        EServiceDescriptorDto eServiceDescriptorDto = eServiceDescriptorMapper.fromEServiceDescriptorToEServiceDescriptorDto(eServiceDescriptor);
        assertNull(eServiceDescriptorDto);
    }

    private EServiceDescriptorDto getEServiceDescriptorDto() {
        EServiceDescriptorDto eServiceDescriptorDto = new EServiceDescriptorDto();
        eServiceDescriptorDto.setState(state);
        return eServiceDescriptorDto;
    }
    private EServiceDescriptor getEServiceDescriptor() {
        EServiceDescriptor eServiceDescriptor = new EServiceDescriptor();
        eServiceDescriptor.setState(EServiceDescriptorState.ARCHIVED);
        return eServiceDescriptor;
    }

    private OrganizationEServiceDto getOrganizationEServiceDto() {
        OrganizationEServiceDto organizationEServiceDto = new OrganizationEServiceDto();
        organizationEServiceDto.setEventId(eventId);
        organizationEServiceDto.setEserviceId(eserviceId);
        organizationEServiceDto.setProducerId(producerId);
        return organizationEServiceDto;
    }

    private void setUp() {
        this.state = EServiceDescriptorState.ARCHIVED.toString();
        this.eventId = 0L;
        this.eserviceId = "01928-37465";
        this.producerId = "09876-54321";
        this.eServiceDescriptorMapper = new EServiceDescriptorMapperImpl();
    }
}

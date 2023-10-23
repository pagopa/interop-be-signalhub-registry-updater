package it.pagopa.interop.signalhub.updater.mapper;

import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.*;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.repository.cache.model.OrganizationEServiceCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationEServiceMapperImplTest {
    private Long eventId;
    private String eserviceId;
    private String producerId;
    private String state;
    private Timestamp tmstInsert;
    private Timestamp tmstLastEdit;
    private OrganizationEServiceMapper organizationEServiceMapper;


    @BeforeEach
    void preTest(){
        this.setUp();
    }


    @Test
    void fromEServiceToOrganizationEServiceDtoTest() {
        EService eService = getEservice();
        OrganizationEServiceDto organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, this.eventId);
        assertNotNull(organizationEServiceDto);
        assertEquals(this.eserviceId, organizationEServiceDto.getEserviceId());
        assertEquals(this.producerId, organizationEServiceDto.getProducerId());
        assertEquals(this.state, organizationEServiceDto.getState());
        assertEquals(this.eventId, organizationEServiceDto.getEventId());

        eService.setProducer(null);
        organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, this.eventId);
        assertNotNull(organizationEServiceDto);
        assertNull(organizationEServiceDto.getProducerId());

        eService = getEservice();
        Organization organization = getOrganization();
        organization.setId(null);
        eService.setProducer(organization);
        organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, this.eventId);
        assertNotNull(organizationEServiceDto);
        assertNull(organizationEServiceDto.getProducerId());

        eService.setState(null);
        organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, this.eventId);
        assertNotNull(organizationEServiceDto);
        assertNull(organizationEServiceDto.getState());

        eService.setId(null);
        organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, this.eventId);
        assertNotNull(organizationEServiceDto);
        assertNull(organizationEServiceDto.getEserviceId());
    }

    @Test
    void fromEServiceToOrganizationEServiceDtoNullCaseTest() {
        EService eService = null;
        OrganizationEServiceDto organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, null);
        assertNull(organizationEServiceDto);

        eService = null;
        organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, this.eventId);
        assertNotNull(organizationEServiceDto);

        eService = getEservice();
        organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, null);
        assertNotNull(organizationEServiceDto);
    }


    @Test
    void toDtoFromEntityTest() {
        OrganizationEService organizationEService = getOrganizationEService();
        OrganizationEServiceDto organizationEServiceDto = organizationEServiceMapper.toDtoFromEntity(organizationEService);
        assertNotNull(organizationEServiceDto);
        assertEquals(this.eventId, organizationEServiceDto.getEventId());
        assertEquals(this.eserviceId, organizationEServiceDto.getEserviceId());
        assertEquals(this.producerId, organizationEServiceDto.getProducerId());
        assertEquals(this.state, organizationEServiceDto.getState());
    }

    @Test
    void toDtoFromEntityNullCaseTest() {
        OrganizationEService organizationEService = null;
        OrganizationEServiceDto organizationEServiceDto = organizationEServiceMapper.toDtoFromEntity(organizationEService);
        assertNull(organizationEServiceDto);
    }


    @Test
    void toCacheFromEntityTest() {
        OrganizationEService organizationEService = getOrganizationEService();
        OrganizationEServiceCache organizationEServiceCache = organizationEServiceMapper.toCacheFromEntity(organizationEService);
        assertNotNull(organizationEServiceCache);
        assertEquals(this.eserviceId, organizationEServiceCache.getEserviceId());
        assertEquals(this.producerId, organizationEServiceCache.getProducerId());
        assertEquals(this.state, organizationEServiceCache.getState());
        assertEquals(this.tmstInsert, organizationEServiceCache.getTmstInsert());
        assertEquals(this.tmstLastEdit, organizationEServiceCache.getTmstLastEdit());
    }

    @Test
    void toCacheFromEntityNullCaseTest() {
        OrganizationEService organizationEService = null;
        OrganizationEServiceCache organizationEServiceCache = organizationEServiceMapper.toCacheFromEntity(organizationEService);
        assertNull(organizationEServiceCache);
    }

    @Test
    void toEntityFromPropsTest() {
        OrganizationEService organizationEService = organizationEServiceMapper.toEntityFromProps(this.eserviceId, this.producerId, this.state);
        assertNotNull(organizationEService);
        assertEquals(this.eserviceId, organizationEService.getEserviceId());
        assertEquals(this.producerId, organizationEService.getProducerId());
        assertEquals(this.state, organizationEService.getState());
    }

    @Test
    void toEntityFromPropsNullCaseTest() {
        OrganizationEService organizationEService = organizationEServiceMapper.toEntityFromProps(null, null, null);
        assertNull(organizationEService);

        organizationEService = organizationEServiceMapper.toEntityFromProps(this.eserviceId, null, null);
        assertNotNull(organizationEService);

        organizationEService = organizationEServiceMapper.toEntityFromProps(null, this.producerId, null);
        assertNotNull(organizationEService);

        organizationEService = organizationEServiceMapper.toEntityFromProps(null, null, this.state);
        assertNotNull(organizationEService);
    }

    private Organization getOrganization() {
        Organization organization = new Organization();
        String organizationId = "5ed26688-389b-4a62-9605-0f14d0ab9605".replace("-", "");
        UUID uuid = new UUID(new BigInteger(organizationId.substring(0, 16), 16).longValue(),
                new BigInteger(organizationId.substring(16), 16).longValue());
        organization.setId(uuid);
        String organizationName = "Ufficio pubblico";
        organization.setName(organizationName);
        String organizationCategory = "PA";
        organization.setCategory(organizationCategory);
        ExternalId externalId = new ExternalId();
        String organizationExternalId = "ExternalId";
        externalId.setId(organizationExternalId);
        String organizationExternalOrigin = "ExternalOrigin";
        externalId.setOrigin(organizationExternalOrigin);
        organization.setExternalId(externalId);
        return organization;
    }

    private EService getEservice() {
        EService eService = new EService();
        String eserviceId = "0f14d0ab-9605-4a62-a9e4-5ed26688389b".replace("-", "");
        UUID uuid = new UUID(new BigInteger(eserviceId.substring(0, 16), 16).longValue(),
                new BigInteger(eserviceId.substring(16), 16).longValue());
        eService.setId(uuid);

        Organization organization = getOrganization();
        eService.setProducer(organization);
        EServiceDescriptorState eServiceDescriptorState = EServiceDescriptorState.ARCHIVED;
        eService.setState(eServiceDescriptorState);
        return eService;
    }

    private OrganizationEService getOrganizationEService() {
        OrganizationEService organizationEService = new OrganizationEService();
        organizationEService.setEventId(this.eventId);
        organizationEService.setEserviceId(this.eserviceId);
        organizationEService.setProducerId(this.producerId);
        organizationEService.setState(this.state);
        organizationEService.setTmstInsert(this.tmstInsert);
        organizationEService.setTmstLastEdit(this.tmstLastEdit);
        return organizationEService;
    }

    private void setUp() {
        this.eventId = 0L;
        this.eserviceId = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
        this.producerId = "5ed26688-389b-4a62-9605-0f14d0ab9605";
        this.state = EServiceDescriptorState.ARCHIVED.toString();
        this.tmstInsert = Timestamp.from(Instant.parse("2023-10-20T18:15:00.000Z"));
        this.tmstLastEdit = Timestamp.from(Instant.parse("2023-10-22T08:40:00.000Z"));
        this.organizationEServiceMapper = Mappers.getMapper(OrganizationEServiceMapper.class);
    }
}

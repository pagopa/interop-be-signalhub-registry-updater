package it.pagopa.interop.signalhub.updater.mapper;

import it.pagopa.interop.signalhub.updater.entity.OrganizationEService;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.*;
import it.pagopa.interop.signalhub.updater.model.OrganizationEServiceDto;
import it.pagopa.interop.signalhub.updater.cache.model.OrganizationEServiceCache;
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
        OrganizationEServiceDto organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, eventId);
        assertNotNull(organizationEServiceDto);
        assertEquals(eserviceId, organizationEServiceDto.getEserviceId());
        assertEquals(producerId, organizationEServiceDto.getProducerId());
        assertEquals(state, organizationEServiceDto.getState());
        assertEquals(eventId, organizationEServiceDto.getEventId());

        eService.setProducer(null);
        organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, eventId);
        assertNotNull(organizationEServiceDto);
        assertNull(organizationEServiceDto.getProducerId());

        eService = getEservice();
        Organization organization = getOrganization();
        organization.setId(null);
        eService.setProducer(organization);
        organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, eventId);
        assertNotNull(organizationEServiceDto);
        assertNull(organizationEServiceDto.getProducerId());

        eService.setState(null);
        organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, eventId);
        assertNotNull(organizationEServiceDto);
        assertNull(organizationEServiceDto.getState());

        eService.setId(null);
        organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, eventId);
        assertNotNull(organizationEServiceDto);
        assertNull(organizationEServiceDto.getEserviceId());
    }

    @Test
    void fromEServiceToOrganizationEServiceDtoNullCaseTest() {
        EService eService = null;
        OrganizationEServiceDto organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, null);
        assertNull(organizationEServiceDto);

        eService = null;
        organizationEServiceDto = organizationEServiceMapper.fromEServiceToOrganizationEServiceDto(eService, eventId);
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
        assertEquals(eventId, organizationEServiceDto.getEventId());
        assertEquals(eserviceId, organizationEServiceDto.getEserviceId());
        assertEquals(producerId, organizationEServiceDto.getProducerId());
        assertEquals(state, organizationEServiceDto.getState());
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
        assertEquals(eserviceId, organizationEServiceCache.getEserviceId());
        assertEquals(producerId, organizationEServiceCache.getProducerId());
        assertEquals(state, organizationEServiceCache.getState());
        assertEquals(tmstInsert, organizationEServiceCache.getTmstInsert());
        assertEquals(tmstLastEdit, organizationEServiceCache.getTmstLastEdit());
    }

    @Test
    void toCacheFromEntityNullCaseTest() {
        OrganizationEService organizationEService = null;
        OrganizationEServiceCache organizationEServiceCache = organizationEServiceMapper.toCacheFromEntity(organizationEService);
        assertNull(organizationEServiceCache);
    }

    @Test
    void toEntityFromDtoTest() {
        OrganizationEService organizationEService = getOrganizationEService();
        OrganizationEServiceDto organizationEServiceDto = organizationEServiceMapper.toDtoFromEntity(organizationEService);
        organizationEService = organizationEServiceMapper.toEntityFromDto(organizationEServiceDto);
        assertNotNull(organizationEService);
        assertEquals(eserviceId, organizationEService.getEserviceId());
        assertEquals(producerId, organizationEService.getProducerId());
        assertEquals(state, organizationEService.getState());
    }

    @Test
    void toEntityFromDtoNullCaseTest() {
        OrganizationEService organizationEService = organizationEServiceMapper.toEntityFromDto(null);
        assertNull(organizationEService);
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
        organizationEService.setEventId(eventId);
        organizationEService.setEserviceId(eserviceId);
        organizationEService.setProducerId(producerId);
        organizationEService.setState(state);
        organizationEService.setTmstInsert(tmstInsert);
        organizationEService.setTmstLastEdit(tmstLastEdit);
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

package it.pagopa.interop.signalhub.updater.mapper;

import it.pagopa.interop.signalhub.updater.entity.DeadEvent;
import it.pagopa.interop.signalhub.updater.mapper.impl.DeadEventMapperImpl;
import it.pagopa.interop.signalhub.updater.model.AgreementEventDto;
import it.pagopa.interop.signalhub.updater.model.EServiceEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.pagopa.interop.signalhub.updater.utility.Const.AGREEMENT_EVENT;
import static it.pagopa.interop.signalhub.updater.utility.Const.ESERVICE_EVENT;
import static org.junit.jupiter.api.Assertions.*;


class DeadEventMapperImplTest {
    private Long eventId;
    private String eserviceId;
    private String agreementId;
    private String descriptorId;
    private String objectType;
    private String eventType;
    private DeadEventMapperImpl deadEventMapper;


    @BeforeEach
    void preTest(){
        this.setUp();
    }


    @Test
    void toDeadEventEServiceCaseTest() {
        EServiceEventDto eServiceEventDto = getEServiceEventDto();
        DeadEvent deadEvent = deadEventMapper.toDeadEvent(eServiceEventDto);
        assertNotNull(deadEvent);
        assertEquals(eventId, deadEvent.getEventId());
        assertEquals(eserviceId, deadEvent.getEserviceId());
        assertEquals(objectType, deadEvent.getObjectType());
        assertEquals(eventType, deadEvent.getEventType());
        assertEquals(descriptorId, deadEvent.getDescriptorId());
    }

    @Test
    void toDeadEventAgreementCaseTest() {
        AgreementEventDto agreementEventDto = getAgreementEventDto();
        DeadEvent deadEvent = deadEventMapper.toDeadEvent(agreementEventDto);
        assertNotNull(deadEvent);
        assertEquals(eventId, deadEvent.getEventId());
        assertEquals(agreementId, deadEvent.getAgreementId());
        assertEquals(objectType, deadEvent.getObjectType());
        assertEquals(eventType, deadEvent.getEventType());
        assertEquals(descriptorId, deadEvent.getDescriptorId());
    }

    @Test
    void toDeadEventNullCaseTest() {
        EServiceEventDto eServiceEventDto = null;
        DeadEvent deadEvent = deadEventMapper.toDeadEvent(eServiceEventDto);
        assertNull(deadEvent);
    }

    private AgreementEventDto getAgreementEventDto() {
        objectType = AGREEMENT_EVENT;
        AgreementEventDto agreementEventDto = new AgreementEventDto();
        agreementEventDto.setEventId(eventId);
        agreementEventDto.setAgreementId(agreementId);
        agreementEventDto.setObjectType(objectType);
        agreementEventDto.setEventType(eventType);
        agreementEventDto.setDescriptorId(descriptorId);
        return agreementEventDto;
    }

    private EServiceEventDto getEServiceEventDto() {
        objectType = ESERVICE_EVENT;
        EServiceEventDto eServiceEventDto = new EServiceEventDto();
        eServiceEventDto.setEventId(eventId);
        eServiceEventDto.setEServiceId(eserviceId);
        eServiceEventDto.setObjectType(objectType);
        eServiceEventDto.setEventType(eventType);
        eServiceEventDto.setDescriptorId(descriptorId);
        return eServiceEventDto;
    }

    private void setUp() {
        this.eventId = 0L;
        this.eserviceId = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
        this.agreementId = "4a620f14-d0ab-9605-a9e4-5ed26688389b";
        this.descriptorId = "01920f14-d0ab-9605-a9e4-374650192842";
        this.eventType = "ADDED";
        this.deadEventMapper = new DeadEventMapperImpl();
    }
}
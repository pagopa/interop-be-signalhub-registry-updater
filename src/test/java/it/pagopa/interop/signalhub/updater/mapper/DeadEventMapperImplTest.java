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
        assertEquals(this.eventId, deadEvent.getEventId());
        assertEquals(this.eserviceId, deadEvent.getEserviceId());
        assertEquals(this.objectType, deadEvent.getObjectType());
        assertEquals(this.eventType, deadEvent.getEventType());
    }

    @Test
    void toDeadEventAgreementCaseTest() {
        AgreementEventDto agreementEventDto = getAgreementEventDto();
        DeadEvent deadEvent = deadEventMapper.toDeadEvent(agreementEventDto);
        assertNotNull(deadEvent);
        assertEquals(this.eventId, deadEvent.getEventId());
        assertEquals(this.agreementId, deadEvent.getAgreementId());
        assertEquals(this.objectType, deadEvent.getObjectType());
        assertEquals(this.eventType, deadEvent.getEventType());
    }

    @Test
    void toDeadEventNullCaseTest() {
        EServiceEventDto eServiceEventDto = null;
        DeadEvent deadEvent = deadEventMapper.toDeadEvent(eServiceEventDto);
        assertNull(deadEvent);
    }

    private AgreementEventDto getAgreementEventDto() {
        this.objectType = AGREEMENT_EVENT;
        AgreementEventDto agreementEventDto = new AgreementEventDto();
        agreementEventDto.setEventId(this.eventId);
        agreementEventDto.setAgreementId(this.agreementId);
        agreementEventDto.setObjectType(this.objectType);
        agreementEventDto.setEventType(this.eventType);
        return agreementEventDto;
    }

    private EServiceEventDto getEServiceEventDto() {
        this.objectType = ESERVICE_EVENT;
        EServiceEventDto eServiceEventDto = new EServiceEventDto();
        eServiceEventDto.setEventId(this.eventId);
        eServiceEventDto.setEServiceId(this.eserviceId);
        eServiceEventDto.setObjectType(this.objectType);
        eServiceEventDto.setEventType(this.eventType);
        return eServiceEventDto;
    }

    private void setUp() {
        this.eventId = 0L;
        this.eserviceId = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
        this.agreementId = "4a620f14-d0ab-9605-a9e4-5ed26688389b";
        this.eventType = "ADDED";
        this.deadEventMapper = new DeadEventMapperImpl();
    }
}
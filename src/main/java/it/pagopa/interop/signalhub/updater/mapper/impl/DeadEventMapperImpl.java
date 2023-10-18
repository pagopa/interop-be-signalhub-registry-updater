package it.pagopa.interop.signalhub.updater.mapper.impl;

import it.pagopa.interop.signalhub.updater.entity.DeadEvent;
import it.pagopa.interop.signalhub.updater.mapper.DeadEventMapper;
import it.pagopa.interop.signalhub.updater.model.AgreementEventDto;
import it.pagopa.interop.signalhub.updater.model.EServiceEventDto;
import it.pagopa.interop.signalhub.updater.model.EventDto;
import org.springframework.stereotype.Component;

import static it.pagopa.interop.signalhub.updater.utility.Const.AGREEMENT_EVENT;
import static it.pagopa.interop.signalhub.updater.utility.Const.ESERVICE_EVENT;


@Component
public class DeadEventMapperImpl implements DeadEventMapper {
    private DeadEventMapperImpl() {}


    public DeadEvent toDeadEvent(EventDto eventDto) {
        DeadEvent deadEvent = new DeadEvent();
        if(eventDto.getEventType().equals(ESERVICE_EVENT)) {
            EServiceEventDto eServiceEventDTO = (EServiceEventDto) eventDto;
            deadEvent.setEventId(eventDto.getEventId());
            deadEvent.setErrorReason("The number of attempts to retrieve the event has been exceeded");
            deadEvent.setEserviceId(eServiceEventDTO.getEServiceId());
            deadEvent.setEventType(ESERVICE_EVENT);
            deadEvent.setObjectType(eServiceEventDTO.getObjectType());
        } else {
            AgreementEventDto agreementEventDto = (AgreementEventDto) eventDto;
            deadEvent.setEventId(eventDto.getEventId());
            deadEvent.setErrorReason("The number of attempts to retrieve the event has been exceeded");
            deadEvent.setEventType(AGREEMENT_EVENT);
            deadEvent.setAgreementId(agreementEventDto.getAgreementId());
            deadEvent.setObjectType(agreementEventDto.getObjectType());
        }
        return deadEvent;
    }
}
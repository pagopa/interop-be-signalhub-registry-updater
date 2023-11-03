package it.pagopa.interop.signalhub.updater.config;

import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.*;
import it.pagopa.interop.signalhub.updater.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static it.pagopa.interop.signalhub.updater.utility.Const.*;

public class DataBuilder {


    private DataBuilder(){}

    public static ConsumerEServiceDto getConsumerDto(){
        ConsumerEServiceDto dto = new ConsumerEServiceDto();
        dto.setAgreementId("09662b6b-5cbf-4bb8-b353-e23d7af6cb5e");
        dto.setConsumerId("63362ead-f496-4a00-8d1e-1073d744a13f");
        dto.setDescriptorId("3627f106-00c5-4ddc-8c0b-9ba68cd4446b");
        dto.setEserviceId("1925cc0d-b65c-4a78-beca-b990b933ecf3");
        dto.setEventId(2L);
        dto.setState(AgreementState.ACTIVE.getValue());
        return dto;
    }

    public static Agreement getDetailAgreement(){
        Agreement data = new Agreement();
        data.setEserviceId(UUID.fromString("1925cc0d-b65c-4a78-beca-b990b933ecf3"));
        data.setId(UUID.randomUUID());
        data.setState(AgreementState.ACTIVE);
        data.setDescriptorId(UUID.fromString("3627f106-00c5-4ddc-8c0b-9ba68cd4446b"));
        data.setConsumerId(UUID.fromString("63362ead-f496-4a00-8d1e-1073d744a13f"));
        data.setProducerId(UUID.fromString("63362ead-f496-4a00-8d1e-1073d744a13f"));
        return data;
    }


    public static EService getDetailEservice(){
        EService eService = new EService();
        eService.setId(UUID.fromString("1925cc0d-b65c-4a78-beca-b990b933ecf3"));
        Organization organization = new Organization();
        organization.setId(UUID.fromString("63362ead-f496-4a00-8d1e-1073d744a13f"));
        eService.setProducer(organization);
        return eService;
    }

    public static OrganizationEServiceDto getEservice(){
        OrganizationEServiceDto dto = new OrganizationEServiceDto();
        dto.setEserviceId("1925cc0d-b65c-4a78-beca-b990b933ecf3");
        dto.setDescriptorId("3627f106-00c5-4ddc-8c0b-9ba68cd4446b");
        dto.setProducerId("63362ead-f496-4a00-8d1e-1073d744a13f");
        dto.setState(EServiceDescriptorState.PUBLISHED.getValue());
        dto.setEventId(2L);
        return dto;
    }

    public static EServiceDescriptor getEserviceDescriptor(){
        EServiceDescriptor dto = new EServiceDescriptor();
        dto.setState(EServiceDescriptorState.ARCHIVED);
        return dto;
    }

    public static Events getEventsWithDuplicate(){
        Events events = new Events();
        events.setLastEventId(5L);

        events.setEvents(
                List.of(
                        getEventDto("63362ead-f496-4a00-8d1e-1073d744a13f"),
                        getEventDto("3627f106-00c5-4ddc-8c0b-9ba68cd4446b"),
                        getEventDto("63362ead-f496-4a00-8d1e-1073d744a13f"),
                        getEventDto("1925cc0d-b65c-4a78-beca-b990b933ecf3"),
                        getEventDto("09662b6b-5cbf-4bb8-b353-e23d7af6cb5e")
                )
        );
        return events;
    }

    public static Event getEventDto(String agreementId){
        Event event= new Event();
        event.setEventId(2L);
        event.setEventType("test");
        event.setObjectType(AGREEMENT_EVENT);
        Map<String, String> objectId = new HashMap<>();
        objectId.put(AGREEMENT_KEY_ID, agreementId);
        objectId.put(DESCRIPTOR_ID, "3627f106-00c5-4ddc-8c0b-9ba68cd4446b");
        event.setObjectId(objectId);
        return event;
    }

}

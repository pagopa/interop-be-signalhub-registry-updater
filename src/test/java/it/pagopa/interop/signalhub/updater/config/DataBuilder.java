package it.pagopa.interop.signalhub.updater.config;

import it.pagopa.interop.signalhub.updater.entity.ConsumerEService;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.*;
import it.pagopa.interop.signalhub.updater.model.*;
import org.springframework.security.oauth2.client.endpoint.NimbusJwtClientAuthenticationParametersConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import software.amazon.awssdk.services.kms.model.SignResponse;
import software.amazon.awssdk.services.kms.model.SigningAlgorithmSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static it.pagopa.interop.signalhub.updater.utility.Const.*;


public class DataBuilder {
    private DataBuilder(){}

    public static ClientRegistration.Builder clientCredentials() {
        return ClientRegistration.withRegistrationId("client-credentials")
                .tokenUri("http://test.provider.com")
                .registrationId("client-credentials")
                .clientId("client-id")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS);
    }



    public static ConsumerEServiceDto getConsumerDto(){
        ConsumerEServiceDto dto = new ConsumerEServiceDto();
        dto.setAgreementId("09662b6b-5cbf-4bb8-b353-e23d7af6cb5e");
        dto.setConsumerId("63362ead-f496-4a00-8d1e-1073d744a13f");
        dto.setDescriptorId("3627f106-00c5-4ddc-8c0b-9ba68cd4446b");
        dto.setEserviceId("1925cc0d-b65c-4a78-beca-b990b933ecf3");
        dto.setProducerId("1925cc0d-b65c-4a78-beca-b990b933ecf3");
        dto.setEventId(2L);
        dto.setState(AgreementState.ACTIVE.getValue());
        return dto;
    }

    public static ConsumerEService getConsumerEntity(){
        ConsumerEService dto = new ConsumerEService();
        dto.setAgreementId("09662b6b-5cbf-4bb8-b353-e23d7af6cb5e");
        dto.setConsumerId("63362ead-f496-4a00-8d1e-1073d744a13f");
        dto.setDescriptorId("3627f106-00c5-4ddc-8c0b-9ba68cd4446b");
        dto.setEserviceId("1925cc0d-b65c-4a78-beca-b990b933ecf3");
        dto.setEventId(2L);
        dto.setState(AgreementState.ACTIVE.getValue());
        return dto;
    }

    public static AgreementEventDto getAgreementEventDTO(){
        AgreementEventDto dto = new AgreementEventDto();
        dto.setDescriptorId("3627f106-00c5-4ddc-8c0b-9ba68cd4446b");
        dto.setEventId(3L);
        dto.setObjectType("CREATE");
        dto.setEventType(AGREEMENT_EVENT);
        dto.setAgreementId("09662b6b-5cbf-4bb8-b353-e23d7af6cb5e");
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
                        getAgreementEvent("63362ead-f496-4a00-8d1e-1073d744a13f"),
                        getAgreementEvent("3627f106-00c5-4ddc-8c0b-9ba68cd4446b"),
                        getAgreementEvent("63362ead-f496-4a00-8d1e-1073d744a13f"),
                        getAgreementEvent("1925cc0d-b65c-4a78-beca-b990b933ecf3"),
                        getAgreementEvent("09662b6b-5cbf-4bb8-b353-e23d7af6cb5e"),
                        getEserviceEvent("84871fd4-2fd7-46ab-9d22-f6b452f4b3c5"),
                        getEserviceEvent("c9f46158-c3ab-4642-9614-bf97fef6bcfd"),
                        getEserviceEvent("84871fd4-2fd7-46ab-9d22-f6b452f4b3c5"),
                        getEserviceEvent("c9f46158-c3ab-4642-9614-bf97fef6bcfd"),
                        getEserviceEvent("9a7e5371-0832-4301-9d97-d762f703dd78")
                        )
        );
        return events;
    }

    public static Event getAgreementEvent(String agreementId){
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

    public static Event getEserviceEvent(String eserviceId){
        Event event= new Event();
        event.setEventId(2L);
        event.setEventType("test");
        event.setObjectType(ESERVICE_EVENT);
        Map<String, String> objectId = new HashMap<>();
        objectId.put(ESERVICE_KEY_ID, eserviceId);
        objectId.put(DESCRIPTOR_ID, "4d1eb3ed-ab17-4f51-a8ab-314c003276af");
        event.setObjectId(objectId);
        return event;
    }


}
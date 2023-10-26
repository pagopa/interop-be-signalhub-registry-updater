package it.pagopa.interop.signalhub.updater.externalclient;

import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.*;


public interface InteroperabilityClient {
    Events getEventsFromId(Long lastEventId);
    Agreement getAgreement(String agreementId);
    EService getEService(String eserviceId);
    EServiceDescriptor getEServiceDescriptor(String eserviceId, String descriptorId);
}

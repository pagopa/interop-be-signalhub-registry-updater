package it.pagopa.interop.signalhub.updater.externalclient;

import it.pagopa.interop.signalhub.updater.config.BaseTest;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.EService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.junit.jupiter.api.Assertions.*;

class InteroperabilityClientImplTest extends BaseTest.WithMockServer {
    @Autowired
    private InteroperabilityClient interoperabilityClient;



//    @BeforeEach
//    public void preTest(){
//    }

//    @Test
    void getEServiceTest() {
        String eserviceId = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
        EService eService = interoperabilityClient.getEService(eserviceId);
    }

//    @Test
    void getEServiceExceptionCaseTest() {
        String eserviceId = "498457c9-f4de-40d2-ad84-aed23c3584fd";
        assertThrows(WebClientResponseException.class, () -> interoperabilityClient.getEService(eserviceId));
    }
}

package it.pagopa.interop.signalhub.updater.clients;

import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.api.v1.GatewayApi;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Events;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class EserviceClients {
    private GatewayApi gatewayApi;



    public Mono<Events> getEvents(){
        return this.gatewayApi.getEventsFromId(0L, 100);
    }

}

package it.pagopa.interop.signalhub.updater.config;

import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.api.ApiClient;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.api.v1.GatewayApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class EServiceConfigurator {

    @Value("${pdnd.client.event.endpoint-url}")
    private String basePath;

    @Bean
    public GatewayApi getRecipientsApiDataVault(WebClient webClient){
        ApiClient apiClient = new ApiClient(webClient);
        apiClient.setBasePath(basePath);
        return new GatewayApi(apiClient);
    }
}

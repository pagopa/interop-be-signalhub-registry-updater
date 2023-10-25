package it.pagopa.interop.signalhub.updater.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientBeanBuilder {
    @Bean
    public WebClient getWebClientBeanTest() {
        return WebClient.builder().build();
    }
}

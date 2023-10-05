package it.pagopa.interop.signalhub.updater.config;


import io.netty.resolver.DefaultAddressResolverGroup;
import it.pagopa.interop.signalhub.updater.generated.openapi.client.interop.model.v1.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


@Slf4j
@Configuration
public class BeanBuilder {
    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .build();
    }
//    WebClient webClient() {
//        return WebClient.builder()
//                .clientConnector(new ReactorClientHttpConnector(
//                                HttpClient.create()
//                                        .resolver(DefaultAddressResolverGroup.INSTANCE)
//                        )
//                ).build();
//    }

    @Bean
    Organization organization() {
        return new Organization();
    }
}
package it.pagopa.interop.signalhub.updater.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class BeanBuilder {


    @Bean
    public MockServer getMockServer(@Value("${mockserver.bean.port}") int port){
        log.info("Port :  {}", port);
        return new MockServer(port);
    }


    @Bean
    public WebClient getWebClientBeanTest() {
        return WebClient.builder().build();
    }

}

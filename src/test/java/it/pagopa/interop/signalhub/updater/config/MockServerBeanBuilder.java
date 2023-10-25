package it.pagopa.interop.signalhub.updater.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class MockServerBeanBuilder {
    @Value("${mockserver.bean.port}")
    private int port;

    @Bean
    public MockServer getMockServer(){
        log.info("Port :  {}", port);
        return new MockServer(port);
    }
}
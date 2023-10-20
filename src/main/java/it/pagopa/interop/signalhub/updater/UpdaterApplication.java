package it.pagopa.interop.signalhub.updater;

import it.pagopa.interop.signalhub.updater.repository.cache.model.ConsumerEServiceCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;


@EnableReactiveMethodSecurity
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class UpdaterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpdaterApplication.class, args);
    }

}

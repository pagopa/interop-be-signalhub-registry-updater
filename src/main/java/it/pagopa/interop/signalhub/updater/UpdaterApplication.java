package it.pagopa.interop.signalhub.updater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableR2dbcAuditing
@EnableScheduling()
public class UpdaterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpdaterApplication.class, args);
    }

}

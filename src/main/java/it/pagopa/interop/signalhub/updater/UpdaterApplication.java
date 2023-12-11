package it.pagopa.interop.signalhub.updater;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@Slf4j
@SpringBootApplication
@EnableCaching
public class UpdaterApplication {


    public static void main(String[] args){
        SpringApplication.run(UpdaterApplication.class, args);
    }


}

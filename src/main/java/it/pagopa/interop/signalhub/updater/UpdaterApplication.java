package it.pagopa.interop.signalhub.updater;

import it.pagopa.interop.signalhub.updater.controller.AutoUpdaterController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;


@Slf4j
@SpringBootApplication
@EnableCaching
public class UpdaterApplication implements CommandLineRunner {

    private final AutoUpdaterController autoUpdaterController;

    public UpdaterApplication(AutoUpdaterController autoUpdaterController) {
        this.autoUpdaterController = autoUpdaterController;
    }

    public static void main(String[] args){
        SpringApplicationBuilder app = new SpringApplicationBuilder(UpdaterApplication.class);
        app.web(WebApplicationType.NONE);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        log.info("Running batch");
        autoUpdaterController.scheduleUpdater();
    }

}

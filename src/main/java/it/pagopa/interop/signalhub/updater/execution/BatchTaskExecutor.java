package it.pagopa.interop.signalhub.updater.execution;

import it.pagopa.interop.signalhub.updater.controller.AutoUpdaterController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class BatchTaskExecutor implements CommandLineRunner {
    @Autowired
    private AutoUpdaterController autoUpdaterController;


    @Override
    public void run(String... args) throws Exception {
        autoUpdaterController.scheduleUpdater();
    }
}

package it.pagopa.interop.signalhub.updater.execution;

import it.pagopa.interop.signalhub.updater.controller.AutoUpdaterController;
import it.pagopa.interop.signalhub.updater.utility.Const;
import it.pagopa.interop.signalhub.updater.utility.Predicates;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
@AllArgsConstructor
public class BatchTaskExecutor implements CommandLineRunner {
    private AutoUpdaterController autoUpdaterController;
    private Environment env;

    @Override
    public void run(String... args) throws Exception {
        if (Predicates.isCorrectApplicationType(env)) {
            String applicationType = env.getProperty(Const.APPLICATION_TYPE_ARG);
            autoUpdaterController.scheduleUpdater(applicationType);
        }
    }
}

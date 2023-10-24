package it.pagopa.interop.signalhub.updater.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.pago-pa-provider")
public class SecurityProps {
    private String clientId;
    private String clientSecret;
    private String tokenUri;
    private String kmsKeyId;
}

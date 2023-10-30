package it.pagopa.interop.signalhub.updater.config;


import com.nimbusds.jose.jwk.RSAKey;
import it.pagopa.interop.signalhub.updater.security.AwsKmsJwtClientAuthenticationParametersConverter;
import it.pagopa.interop.signalhub.updater.security.PublicKeyFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ClientCredentialsReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;


@Slf4j
@Configuration
public class BeanBuilder {

    @Bean
    public ReactiveClientRegistrationRepository getRegistration(SecurityProps props) {
        ClientRegistration registration = ClientRegistration
                .withRegistrationId("pago-pa-client")
                .tokenUri(props.getTokenUri())
                .clientId(props.getClientId())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }

    @Bean
    @Profile("!test")
    public RSAKey getRsaKey(KmsClient kmsClient, SecurityProps props){
        return new PublicKeyFactory(kmsClient, props.getKmsKeyId())
                .obtainPublicKey();
    }

    @Profile("!test")
    @Bean(name = "interop-webclient")
    public WebClient webClient(ReactiveClientRegistrationRepository clientRegistrations, SecurityProps props, KmsClient kmsClient, RSAKey rsaKey) {
        InMemoryReactiveOAuth2AuthorizedClientService clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations);
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations, clientService);

        ClientCredentialsReactiveOAuth2AuthorizedClientProvider clientProvider = new ClientCredentialsReactiveOAuth2AuthorizedClientProvider();
        WebClientReactiveClientCredentialsTokenResponseClient tokenResponseClient =
                new WebClientReactiveClientCredentialsTokenResponseClient();

        AwsKmsJwtClientAuthenticationParametersConverter<OAuth2ClientCredentialsGrantRequest> converter = new AwsKmsJwtClientAuthenticationParametersConverter<>(kmsClient, props);

        converter.setJwtClientAssertionCustomizer(context -> {
            context.getHeaders().header("typ", "JWT");
            context.getHeaders().header("kid", rsaKey.getKeyID());
            context.getClaims().claim("aud", "auth.uat.interop.pagopa.it/client-assertion");
        });

        tokenResponseClient.addParametersConverter(converter);
        clientProvider.setAccessTokenResponseClient(tokenResponseClient);

        authorizedClientManager.setAuthorizedClientProvider(clientProvider);
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultClientRegistrationId("pago-pa-client");
        return WebClient.builder()
                .filter(oauth)
                .build();
    }


    @Bean
    public KmsClient kmsClient(AwsPropertiesConfig props) {
        return configureBuilder(KmsClient.builder(), props);
    }


    private <C> C configureBuilder(AwsClientBuilder<?, C> builder, AwsPropertiesConfig props) {
        if( props != null ) {

            String profileName = props.getProfile();
            if( StringUtils.isNotBlank( profileName ) ) {
                builder.credentialsProvider( ProfileCredentialsProvider.create( profileName ) );
            } else {
                log.debug("Using WebIdentityTokenFileCredentialsProvider");
                builder.credentialsProvider( WebIdentityTokenFileCredentialsProvider.create() );
            }

            String regionCode = props.getRegion();
            if( StringUtils.isNotBlank( regionCode )) {
                log.debug("Setting region to: {}", regionCode);
                builder.region( Region.of( regionCode ));
            }
        }

        return builder.build();
    }
}

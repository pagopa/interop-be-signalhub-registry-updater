package it.pagopa.interop.signalhub.updater.config;


import com.nimbusds.jose.jwk.JWK;
import it.pagopa.interop.signalhub.updater.security.RSAKeyReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.endpoint.NimbusJwtClientAuthenticationParametersConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Function;


@Slf4j
@Configuration
public class BeanBuilder {

    @Bean
    ReactiveClientRegistrationRepository getRegistration(SecurityProps props) {
        ClientRegistration registration = ClientRegistration
                .withRegistrationId("pago-pa-client")
                .tokenUri(props.getTokenUri())
                .clientId(props.getClientId())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }


    @Bean(name = "interop-webclient")
    WebClient webClient(ReactiveClientRegistrationRepository clientRegistrations, SecurityProps props) {

        Function<ClientRegistration, JWK> jwkResolver = clientRegistration -> {
            if (clientRegistration.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.CLIENT_SECRET_JWT)) {
                return RSAKeyReader.getRSAKey(props);
            }
            return null;
        };


        InMemoryReactiveOAuth2AuthorizedClientService clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations);
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations, clientService);

        ClientCredentialsReactiveOAuth2AuthorizedClientProvider clientProvider = new ClientCredentialsReactiveOAuth2AuthorizedClientProvider();
        WebClientReactiveClientCredentialsTokenResponseClient tokenResponseClient =
                new WebClientReactiveClientCredentialsTokenResponseClient();

        NimbusJwtClientAuthenticationParametersConverter<OAuth2ClientCredentialsGrantRequest> converter =
                new NimbusJwtClientAuthenticationParametersConverter<>(jwkResolver);
        converter.setJwtClientAssertionCustomizer((context) -> {
            context.getHeaders().header("typ", "JWT");
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

}

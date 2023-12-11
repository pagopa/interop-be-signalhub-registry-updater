package it.pagopa.interop.signalhub.updater.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nimbusds.jose.util.Base64;
import it.pagopa.interop.signalhub.updater.config.DataBuilder;
import it.pagopa.interop.signalhub.updater.config.SecurityProps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.SignRequest;
import software.amazon.awssdk.services.kms.model.SignResponse;


import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class AwsKmsJwtConverterTest {
    @Mock
    private KmsClient kmsClient;
    @Mock
    private SecurityProps securityProps;
    @InjectMocks
    private AwsKmsJwtClientAuthenticationParametersConverter<OAuth2ClientCredentialsGrantRequest> converter;


    @Test
    void convertWhenAuthorizationGrantRequestNullThenThrowIllegalArgumentException() {
        assertThatIllegalArgumentException().isThrownBy(() -> this.converter.convert(null))
                .withMessage("authorizationGrantRequest cannot be null");
    }

    @Test
    void setJwtClientAssertionCustomizerWhenNullThenThrowIllegalArgumentException() {
        assertThatIllegalArgumentException().isThrownBy(() -> this.converter.setJwtClientAssertionCustomizer(null))
                .withMessage("jwtClientAssertionCustomizer cannot be null");
    }

    @Test
    void convertWhenOtherClientAuthenticationMethodThenNotCustomized() {
        ClientRegistration clientRegistration = DataBuilder.clientCredentials()
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .build();
        OAuth2ClientCredentialsGrantRequest clientCredentialsGrantRequest = new OAuth2ClientCredentialsGrantRequest(
                clientRegistration);
        assertThat(this.converter.convert(clientCredentialsGrantRequest)).isNull();
        Mockito.verifyNoInteractions(this.kmsClient);
        Mockito.verifyNoInteractions(this.securityProps);
    }

    @Test
    void convertWithoutCustomizerThenReturnJWT() {
        ClientRegistration clientRegistration = DataBuilder.clientCredentials()
                .clientAuthenticationMethod(ClientAuthenticationMethod.PRIVATE_KEY_JWT)
                .build();
        OAuth2ClientCredentialsGrantRequest clientCredentialsGrantRequest = new OAuth2ClientCredentialsGrantRequest(
                clientRegistration);

        SignResponse response = Mockito.mock(SignResponse.class);
        SdkBytes sdkBytes = Mockito.mock(SdkBytes.class);
        Mockito.when(sdkBytes.asByteArray()).thenReturn("Hello USER JWT".getBytes());
        Mockito.when(response.signature()).thenReturn(sdkBytes);
        Mockito.when(this.kmsClient.sign((SignRequest) Mockito.any())).thenReturn(response);

        MultiValueMap<String,String> result = this.converter.convert(clientCredentialsGrantRequest);
        Assertions.assertNotNull(result);
        assertThat(result.getFirst(OAuth2ParameterNames.CLIENT_ASSERTION_TYPE))
                .isEqualTo("urn:ietf:params:oauth:client-assertion-type:jwt-bearer");

        String encodedJws = result.getFirst(OAuth2ParameterNames.CLIENT_ASSERTION);

        Assertions.assertNotNull(encodedJws);
        Assertions.assertTrue(encodedJws.contains(Base64.encode("Hello USER JWT".getBytes()).toString()));


    }


    @Test
    void convertWithCustomizerThenReturnCorrectJWT() {
        ClientRegistration clientRegistration = DataBuilder.clientCredentials()
                .clientAuthenticationMethod(ClientAuthenticationMethod.PRIVATE_KEY_JWT)
                .build();
        OAuth2ClientCredentialsGrantRequest clientCredentialsGrantRequest = new OAuth2ClientCredentialsGrantRequest(
                clientRegistration);

        converter.setJwtClientAssertionCustomizer(context -> {
            context.getHeaders().header("typ", "JWT");
            context.getHeaders().header("kid", "1234");
            context.getClaims().claim("aud", "auth.uat.interop.pagopa.it/client-assertion");
        });

        SignResponse response = Mockito.mock(SignResponse.class);
        SdkBytes sdkBytes = Mockito.mock(SdkBytes.class);
        Mockito.when(sdkBytes.asByteArray()).thenReturn("Hello USER JWT".getBytes());
        Mockito.when(response.signature()).thenReturn(sdkBytes);
        Mockito.when(this.kmsClient.sign((SignRequest) Mockito.any())).thenReturn(response);

        MultiValueMap<String,String> result = this.converter.convert(clientCredentialsGrantRequest);
        Assertions.assertNotNull(result);
        assertThat(result.getFirst(OAuth2ParameterNames.CLIENT_ASSERTION_TYPE))
                .isEqualTo("urn:ietf:params:oauth:client-assertion-type:jwt-bearer");

        String encodedJws = result.getFirst(OAuth2ParameterNames.CLIENT_ASSERTION);

        Assertions.assertNotNull(encodedJws);
        Assertions.assertTrue(encodedJws.contains(Base64.encode("Hello USER JWT".getBytes()).toString()));

        DecodedJWT jwtDecoder = JWT.decode(encodedJws);
        Assertions.assertEquals("JWT", jwtDecoder.getType());
        Assertions.assertEquals("1234", jwtDecoder.getKeyId());
        Assertions.assertEquals("auth.uat.interop.pagopa.it/client-assertion", jwtDecoder.getClaim("aud").asString());

    }


}

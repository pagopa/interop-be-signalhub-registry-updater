package it.pagopa.interop.signalhub.updater.security;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.JWTClaimsSet;
import it.pagopa.interop.signalhub.updater.config.SecurityProps;
import lombok.Getter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.client.endpoint.AbstractOAuth2AuthorizationGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.util.*;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.SignRequest;
import software.amazon.awssdk.services.kms.model.SignResponse;
import software.amazon.awssdk.services.kms.model.SigningAlgorithmSpec;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

public final class AwsKmsJwtClientAuthenticationParametersConverter<T extends AbstractOAuth2AuthorizationGrantRequest> implements Converter<T, MultiValueMap<String, String>> {

    private final KmsClient kmsClient;
    private final SecurityProps securityProps;

    public AwsKmsJwtClientAuthenticationParametersConverter(KmsClient kmsClient, SecurityProps securityProps) {
        this.kmsClient = kmsClient;
        this.securityProps = securityProps;
    }

    private Consumer<AwsKmsJwtClientAuthenticationParametersConverter.JwtClientAuthenticationContext<T>> jwtClientAssertionCustomizer = context -> {};

    @Override
    public MultiValueMap<String, String> convert(@NonNull T authorizationGrantRequest) {
        Assert.notNull(authorizationGrantRequest, "authorizationGrantRequest cannot be null");
        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();
        if (!ClientAuthenticationMethod.PRIVATE_KEY_JWT.equals(clientRegistration.getClientAuthenticationMethod()) && !ClientAuthenticationMethod.CLIENT_SECRET_JWT.equals(clientRegistration.getClientAuthenticationMethod())) {
            return null;
        } else {
            JwsHeader.Builder headersBuilder = JwsHeader.with(SignatureAlgorithm.RS256);
            Instant issuedAt = Instant.now();
            Instant expiresAt = issuedAt.plus(Duration.ofSeconds(60L));
            org.springframework.security.oauth2.jwt.JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder().issuer(clientRegistration.getClientId()).subject(clientRegistration.getClientId()).audience(Collections.singletonList(clientRegistration.getProviderDetails().getTokenUri())).id(UUID.randomUUID().toString()).issuedAt(issuedAt).expiresAt(expiresAt);
            AwsKmsJwtClientAuthenticationParametersConverter.JwtClientAuthenticationContext<T> jwtClientAssertionContext =
                    new AwsKmsJwtClientAuthenticationParametersConverter.JwtClientAuthenticationContext<>(authorizationGrantRequest, headersBuilder, claimsBuilder);
            this.jwtClientAssertionCustomizer.accept(jwtClientAssertionContext);
            JwsHeader jwsHeader = headersBuilder.build();
            JwtClaimsSet jwtClaimsSet = claimsBuilder.build();

            JWSHeader jwsHeader1 = convert(jwsHeader);
            JWTClaimsSet jwtClaimsSet1 = convert(jwtClaimsSet);

            String signingInputString =
                    jwsHeader1.toBase64URL().toString() + '.' + jwtClaimsSet1.toPayload().toBase64URL().toString();


            String signature = signWithKms(signingInputString);

            String token = signingInputString + '.' + signature;
            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            parameters.set("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
            parameters.set("client_assertion", token);
            return parameters;
        }

    }

    private String signWithKms(String signingInputString) {
        SignRequest signRequest = SignRequest.builder()
                .message(SdkBytes.fromByteArray(signingInputString.getBytes()))
                .keyId(securityProps.getKmsKeyId())
                .signingAlgorithm(SigningAlgorithmSpec.RSASSA_PKCS1_V1_5_SHA_256)
                .build();

        SignResponse response = kmsClient.sign(signRequest);
        return Base64.encode(response.signature().asByteArray()).toString();
    }

    public void setJwtClientAssertionCustomizer(Consumer<AwsKmsJwtClientAuthenticationParametersConverter.JwtClientAuthenticationContext<T>> jwtClientAssertionCustomizer) {
        Assert.notNull(jwtClientAssertionCustomizer, "jwtClientAssertionCustomizer cannot be null");
        this.jwtClientAssertionCustomizer = jwtClientAssertionCustomizer;
    }

    @Getter
    public static final class JwtClientAuthenticationContext<T extends AbstractOAuth2AuthorizationGrantRequest> {
        private final T authorizationGrantRequest;
        private final JwsHeader.Builder headers;
        private final org.springframework.security.oauth2.jwt.JwtClaimsSet.Builder claims;

        private JwtClientAuthenticationContext(T authorizationGrantRequest, JwsHeader.Builder headers, org.springframework.security.oauth2.jwt.JwtClaimsSet.Builder claims) {
            this.authorizationGrantRequest = authorizationGrantRequest;
            this.headers = headers;
            this.claims = claims;
        }

    }

    private static JWSHeader convert(JwsHeader headers) {
        com.nimbusds.jose.JWSHeader.Builder builder = new com.nimbusds.jose.JWSHeader.Builder(JWSAlgorithm.parse(headers.getAlgorithm().getName()));

        String keyId = headers.getKeyId();
        if (StringUtils.hasText(keyId)) {
            builder.keyID(keyId);
        }

        String type = headers.getType();
        if (StringUtils.hasText(type)) {
            builder.type(new JOSEObjectType(type));
        }

        return builder.build();
    }

    private static JWTClaimsSet convert(JwtClaimsSet claims) {
        com.nimbusds.jwt.JWTClaimsSet.Builder builder = new com.nimbusds.jwt.JWTClaimsSet.Builder();
        Object issuer = claims.getClaim("iss");
        if (issuer != null) {
            builder.issuer(issuer.toString());
        }

        String subject = claims.getSubject();
        if (StringUtils.hasText(subject)) {
            builder.subject(subject);
        }

        List<String> audience = claims.getAudience();
        if (!CollectionUtils.isEmpty(audience)) {
            builder.audience(audience);
        }

        Instant expiresAt = claims.getExpiresAt();
        if (expiresAt != null) {
            builder.expirationTime(Date.from(expiresAt));
        }

        Instant notBefore = claims.getNotBefore();
        if (notBefore != null) {
            builder.notBeforeTime(Date.from(notBefore));
        }

        Instant issuedAt = claims.getIssuedAt();
        if (issuedAt != null) {
            builder.issueTime(Date.from(issuedAt));
        }

        String jwtId = claims.getId();
        if (StringUtils.hasText(jwtId)) {
            builder.jwtID(jwtId);
        }

        return builder.build();
    }






}

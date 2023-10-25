package it.pagopa.interop.signalhub.updater.security;

import com.nimbusds.jose.jwk.RSAKey;
import it.pagopa.interop.signalhub.updater.exception.PDNDKeyFactoryException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.GetPublicKeyRequest;
import software.amazon.awssdk.services.kms.model.GetPublicKeyResponse;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@ExtendWith(MockitoExtension.class)
class PublicKeyFactoryTest {
    private static final String pubKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuvcs2yNi1wNnO7f8V323nAKVTSivhqaF+e/B55GzyzF8zH3cnlbS04wlqNee1qyHg492t2bxX4N1I+nydUzwzq2OOPRsDyKk6NW2Bp18fxyQskLA0FY9VL8dWdAAVk3pPJCMi0skt4GrMRsFSDNef0PY0fuCjRrZTFsWt42b1Tl0P8TnS4sMJotHLqPoaZve0s75oFkF5g7/mkNIP+J6m+/wKGEGBY5IvdkNmZW3Gn2abxFjGMG0yNsiDn2C5kNnT7So0ZbkebhdSm2acaCQhrP90ro428IMVLSAlRBgsmW2UHljbI3bbfFcyGeAnbS2ZhF9PeoEOHQr3d37mCWIAQIDAQAB";
    private PublicKeyFactory publicKeyFactory;
    private KmsClient kmsClient;
    private MockedStatic<KeyFactory> mockedKeyFactory;


    @BeforeEach
    public void setUp(){
        this.kmsClient = Mockito.mock(KmsClient.class);
        this.publicKeyFactory = new PublicKeyFactory(kmsClient,  "ARN-ddfj54-ppeor");
        this.mockedKeyFactory = Mockito.mockStatic(KeyFactory.class);
    }

    @AfterEach
    public void after(){
        if (mockedKeyFactory != null && !mockedKeyFactory.isClosed()) {
            mockedKeyFactory.close();
        }
    }

    @Test
    void whenObtainedKeyThenReturnRSAKey() {
        mockedKeyFactory.close();
        SdkBytes bytes = SdkBytes.fromByteArray(Base64.getDecoder().decode(pubKeyString));
        GetPublicKeyResponse response = GetPublicKeyResponse.builder().publicKey(bytes).build();

        Mockito.when(kmsClient.getPublicKey((GetPublicKeyRequest) Mockito.any()))
                .thenReturn(response);


        RSAKey key = this.publicKeyFactory.obtainPublicKey();
        Assertions.assertNotNull(key);
        Assertions.assertNotNull(key.getKeyID());
        Assertions.assertEquals("1udPzlTJvSj8fhdx_jRA001Zce5jjsYNUNHnclGIyBo", key.getKeyID());
    }

    @Test
    void whenObtainedKeyWithKeyBadlyFormatThenThrowException() {
        mockedKeyFactory.close();
        SdkBytes bytes = SdkBytes.fromString(pubKeyString, StandardCharsets.UTF_8);
        GetPublicKeyResponse response = GetPublicKeyResponse.builder().publicKey(bytes).build();

        Mockito.when(kmsClient.getPublicKey((GetPublicKeyRequest) Mockito.any()))
                .thenReturn(response);


        Assertions.assertThrows(
                PDNDKeyFactoryException.class,
                () -> publicKeyFactory.obtainPublicKey()
        );

    }


    @SneakyThrows
    @Test
    void whenGeneratePublicKeyWithMissedKeySpec() {
        KeyFactory keyFactory = Mockito.mock(KeyFactory.class);
        SdkBytes bytes = SdkBytes.fromString("XXXXX", StandardCharsets.UTF_8);
        GetPublicKeyResponse response = GetPublicKeyResponse.builder().publicKey(bytes).build();

        Mockito.when(kmsClient.getPublicKey((GetPublicKeyRequest) Mockito.any()))
                .thenReturn(response);

        mockedKeyFactory.when(() ->
                KeyFactory.getInstance(Mockito.any())
        ).thenReturn(keyFactory);

        Mockito.when(keyFactory.generatePublic(Mockito.any()))
                .thenThrow(new InvalidKeySpecException());

        Assertions.assertThrows(
                PDNDKeyFactoryException.class,
                () -> publicKeyFactory.obtainPublicKey()
        );

    }


    @Test
    void whenKeyFactoryAlgorithmIsBadlyType(){
        SdkBytes bytes = SdkBytes.fromString("XXXXX", StandardCharsets.UTF_8);
        GetPublicKeyResponse response = GetPublicKeyResponse.builder().publicKey(bytes).build();

        Mockito.when(kmsClient.getPublicKey((GetPublicKeyRequest) Mockito.any()))
                .thenReturn(response);

        mockedKeyFactory.when(() ->
            KeyFactory.getInstance(Mockito.any())
        ).thenThrow(new NoSuchAlgorithmException());


        Assertions.assertThrows(
                PDNDKeyFactoryException.class,
                () -> publicKeyFactory.obtainPublicKey()
        );

    }

    @Test
    void whenKmsResponseWithNull(){
        Mockito.when(kmsClient.getPublicKey((GetPublicKeyRequest) Mockito.any()))
                .thenReturn(null);

        Assertions.assertThrows(
                PDNDKeyFactoryException.class,
                () -> publicKeyFactory.obtainPublicKey()
        );
    }





}

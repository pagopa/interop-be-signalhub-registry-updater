package it.pagopa.interop.signalhub.updater.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import it.pagopa.interop.signalhub.updater.exception.PDNDKeyFactoryException;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.GetPublicKeyRequest;
import software.amazon.awssdk.services.kms.model.GetPublicKeyResponse;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;


@Slf4j
public class PublicKeyFactory {
    private final KmsClient kmsClient;
    private final String keyId;

    public PublicKeyFactory(KmsClient kmsClient, String keyId){
        this.kmsClient = kmsClient;
        this.keyId = keyId;
    }

    public RSAKey obtainPublicKey(){
        byte[] contentPublicKey = getPublicKeyFromKMS().publicKey().asByteArray();
        RSAPublicKey publicKey = createPublicKey(contentPublicKey);
        return buildRSAKeyWithThumbprint(publicKey);
    }

    private RSAKey buildRSAKeyWithThumbprint(RSAPublicKey publicKey){
        try {
            return new RSAKey.Builder(publicKey).keyIDFromThumbprint().build();
        } catch (JOSEException e) {
            log.error("Error during building RSA key ", e);
            throw new PDNDKeyFactoryException("Error during building RSA Key or with calculation KID", e);
        }
    }


    private RSAPublicKey createPublicKey(byte[] contentPublicKey){
        KeyFactory factory = null;
        try {
            factory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(contentPublicKey);
            PublicKey publicKey = factory.generatePublic(keySpec);
            log.debug("Public key build");
            return (RSAPublicKey) publicKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Error with find algorithm or invalid Key specification ", e);
            throw new PDNDKeyFactoryException("No such a Algorithm for specific key or missing specification", e);
        }
    }


    private GetPublicKeyResponse getPublicKeyFromKMS(){
        GetPublicKeyRequest request = GetPublicKeyRequest.builder()
                .keyId(keyId)
                .build();
        log.debug("Get public key from KMS");
        return kmsClient.getPublicKey(request);
    }

}

package it.pagopa.interop.signalhub.updater.security;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.IOUtils;
import it.pagopa.interop.signalhub.updater.config.SecurityProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Slf4j
public class RSAKeyReader {

    private RSAKeyReader(){}

    public static RSAKey getRSAKey(SecurityProps props) {
        RSAPrivateKey privateKey = RSAKeyReader.readPrivateFromFile(props.getPathPrivateKey());
        RSAPublicKey publicKey = RSAKeyReader.readPublicFromFile(props.getPathPublicKey());
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(props.getKid())
                .build();
    }

    private static RSAPrivateKey readPrivateFromFile(String fileName) {
        try {
            byte[] privateKeyBytes = getBytesFromFile(fileName);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            if (privateKey instanceof RSAPrivateKey key){
                return key;
            }
            return null;
        } catch (Exception e) {
            log.error("Error on generate private key: {}", e.getMessage(), e);
            return null;
        }
    }

    private static RSAPublicKey readPublicFromFile(String fileName) {
        try {

            byte[] publicKeyBytes = getBytesFromFile(fileName);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey privateKey = factory.generatePublic(keySpec);
            if (privateKey instanceof RSAPublicKey key){
                return key;
            }
            return null;
        } catch (Exception e) {
            log.error("Error on generate private key: {}", e.getMessage(), e);
            return null;
        }
    }

    private static byte[] getBytesFromFile(String filename) throws IOException {
        ClassPathResource resource = new ClassPathResource(filename);
        InputStream inputStream = resource.getInputStream();
        String contentData = IOUtils.readInputStreamToString(inputStream);
        inputStream.close();
        log.info("Letto : {}", contentData);
        contentData = contentData
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("[\\r\\n]", "");
        log.info(contentData);
        return Base64.getDecoder().decode(contentData);

    }
}

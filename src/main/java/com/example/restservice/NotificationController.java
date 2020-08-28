package com.example.restservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.microsoft.graph.requests.extensions.*;
import com.microsoft.graph.auth.confidentialClient.*;
import com.microsoft.graph.models.extensions.*;
import com.microsoft.graph.auth.enums.*;
import org.springframework.http.*;
import com.google.gson.JsonPrimitive;
import java.util.Arrays;
import java.util.List;
import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.security.*;
import java.security.cert.*;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.*;
import javax.crypto.spec.*;
import io.jsonwebtoken.*;
import org.apache.commons.text.StringEscapeUtils;

@RestController
public class NotificationController {
//region app registration information
    private final String clientId = "";
    private final String clientSecret = "";
    private final String tenantId = "";
    // endregion
    // region subscription information
    private final String publicUrl = ""; // eg https://c2ddde53.ngrok.io no trailing slash
    private final String resource = ""; // eg
                                        // teams/9c05f27f-f866-4cc0-b4c2-6225a4568bc5/channels/19:015c392a3030451f8b52fac6084be56d@thread.skype/messages
    private final String changeType = "created";
    // endregion
    // region certificate information
    private final String storename = "JKSkeystore.jks";
    private final String alias = "selfsignedjks";
    private final String storepass = "";
    // endregion

    private final NationalCloud endpoint = NationalCloud.Global;
    private final List<String> scopes = Arrays.asList("https://graph.microsoft.com/.default");
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private JwkKeyResolver jwksResolver;

    @GetMapping("/notification")
    @ResponseBody
    public String subscribe() throws KeyStoreException, FileNotFoundException, IOException, CertificateException,
            NoSuchAlgorithmException {
        final ClientCredentialProvider authProvider = new ClientCredentialProvider(this.clientId, this.scopes,
                this.clientSecret, this.tenantId, this.endpoint);

        final IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(authProvider)
                .buildClient();
        graphClient.setServiceRoot("https://graph.microsoft.com/beta");
        Subscription subscription = new Subscription();
        subscription.changeType = this.changeType;
        subscription.notificationUrl = this.publicUrl + "/notification";
        subscription.resource = this.resource;
        subscription.expirationDateTime = Calendar.getInstance();
        subscription.clientState = "secretClientValue";

        subscription.expirationDateTime.add(Calendar.HOUR, 1);

        if (this.resource.startsWith("teams")) {
            subscription.additionalDataManager().put("includeResourceData", new JsonPrimitive(true));
            subscription.additionalDataManager().put("encryptionCertificate",
                    new JsonPrimitive(GetBase64EncodedCertificate()));
            subscription.additionalDataManager().put("encryptionCertificateId", new JsonPrimitive(this.alias));
            LOGGER.warn("encoded cert");
            LOGGER.info(GetBase64EncodedCertificate());
        }

        subscription = graphClient.subscriptions().buildRequest().post(subscription);
        return "Subscribed to entity with subscription id " + subscription.id;
    }

    private KeyStore GetCertificateStore() throws KeyStoreException, FileNotFoundException, IOException,
            CertificateException, NoSuchAlgorithmException {
        final KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(this.storename), this.storepass.toCharArray());
        return ks;
    }

    private String GetBase64EncodedCertificate() throws CertificateEncodingException, KeyStoreException,
            FileNotFoundException, IOException, CertificateException, NoSuchAlgorithmException {
        final KeyStore ks = this.GetCertificateStore();
        final java.security.cert.Certificate cert = ks.getCertificate(this.alias);
        return new String(Base64.encodeBase64(cert.getEncoded()));
    }

    private byte[] GetEncryptionKey(final String base64encodedSymetricKey) throws KeyStoreException,
            NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, FileNotFoundException,
            NoSuchPaddingException, IOException, UnrecoverableKeyException, BadPaddingException, CertificateException {
        final KeyStore ks = this.GetCertificateStore();
        final Key asymmetricKey = ks.getKey(this.alias, this.storepass.toCharArray());
        final byte[] encryptedSymetricKey = Base64.decodeBase64(base64encodedSymetricKey);
        final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, asymmetricKey);
        return cipher.doFinal(encryptedSymetricKey);
    }

    private boolean IsDataSignatureValid(final byte[] encryptionKey, final String encryptedData,
            final String comparisonSignature) throws NoSuchAlgorithmException, InvalidKeyException {
        final byte[] decodedEncryptedData = Base64.decodeBase64(encryptedData);
        final Mac mac = Mac.getInstance("HMACSHA256");
        final SecretKey skey = new SecretKeySpec(encryptionKey, "HMACSHA256");
        mac.init(skey);
        final byte[] hashedData = mac.doFinal(decodedEncryptedData);
        final String encodedHashedData = new String(Base64.encodeBase64(hashedData));
        return comparisonSignature.equals(encodedHashedData);
    }

    private String GetDecryptedData(final byte[] encryptionKey, final String encryptedData)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException,
            BadPaddingException, InvalidAlgorithmParameterException {
        final SecretKey skey = new SecretKeySpec(encryptionKey, "AES");
        final IvParameterSpec ivspec = new IvParameterSpec(Arrays.copyOf(encryptionKey, 16));
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");// should be 7 but
                                                                         // https://stackoverflow.com/a/53139355/3808675
        cipher.init(Cipher.DECRYPT_MODE, skey, ivspec);
        return new String(cipher.doFinal(Base64.decodeBase64(encryptedData)));
    }

    private boolean IsValidationTokenValid(final String[] validAudiences, final String[] validTenantIds,
            final String serializedToken) {
        try {
            if (this.jwksResolver == null) {
                this.jwksResolver = new JwkKeyResolver();
            }
            final Jws<Claims> token = Jwts.parserBuilder().setSigningKeyResolver(this.jwksResolver).build()
                    .parseClaimsJws(serializedToken);
            final Claims body = token.getBody();
            final String audience = body.getAudience();
            boolean isAudienceValid = false;
            for (final String validAudience : validAudiences) {
                isAudienceValid = isAudienceValid || validAudience.equals(audience);
            }
            final String issuer = body.getIssuer();
            boolean isTenantValid = false;
            for (final String validTenantId : validTenantIds) {
                isTenantValid = isTenantValid || issuer.endsWith(validTenantId + "/");
            }
            return isAudienceValid && isTenantValid; // nbf,exp and signature are already validated by library
        } catch (final Exception e) {
            LOGGER.error("could not validate token");
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    @PostMapping(value = "/notification", headers = { "content-type=text/plain" })
    @ResponseBody
    public ResponseEntity<String> handleValidation(
            @RequestParam(value = "validationToken") final String validationToken) {
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN)
                .body(StringEscapeUtils.escapeHtml4(validationToken));
    }

    @PostMapping("/notification")
    @ResponseBody
    public ResponseEntity<String> handleNotification(@RequestBody() final ChangeNotificationCollection notifications)
            throws KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException,
            FileNotFoundException, NoSuchPaddingException, IOException, UnrecoverableKeyException, BadPaddingException,
            CertificateException, InvalidAlgorithmParameterException {
        LOGGER.info("notification received");
        LOGGER.info(notifications.value.get(0).resource);
        final byte[] decryptedKey = this.GetEncryptionKey(notifications.value.get(0).encryptedContent.dataKey);
        final boolean isDataSignatureValid = this.IsDataSignatureValid(decryptedKey,
                notifications.value.get(0).encryptedContent.data,
                notifications.value.get(0).encryptedContent.dataSignature);
        boolean areValidationTokensValid = true;
        for (final String validationToken : notifications.validationTokens) {
            areValidationTokensValid = areValidationTokensValid && this.IsValidationTokenValid(
                    new String[] { this.clientId }, new String[] { this.tenantId }, validationToken);
        }
        if (areValidationTokensValid && isDataSignatureValid) {
            final String decryptedData = this.GetDecryptedData(decryptedKey,
                    notifications.value.get(0).encryptedContent.data);
            LOGGER.info("decrypted data");
            LOGGER.info(decryptedData);
            return ResponseEntity.ok().body("");
        } else if(!isDataSignatureValid) {
            return ResponseEntity.badRequest().body("data signature validation failed");
        } else {
            return ResponseEntity.badRequest().body("token validation failed");
        }
	}
}

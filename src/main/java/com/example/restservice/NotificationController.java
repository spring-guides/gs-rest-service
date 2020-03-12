package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

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
import com.microsoft.graph.authentication.*;
import com.microsoft.graph.httpcore.*;
import org.springframework.http.*;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
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

@RestController
public class NotificationController {
//region app registration information
    private String clientId  = "24f1a7a0-bda7-4bf8-b8a6-c915da1d0ddb";
    private String clientSecret = "";
    private String tenantId = "bd4c6c31-c49c-4ab6-a0aa-742e07c20232";
//endregion
//region subscription information
    private String publicUrl = "https://e7ff1771.ngrok.io";
    private String resource = "teams/9c05f27f-f866-4cc0-b4c2-6225a4568bc5/channels/19:015c392a3030451f8b52fac6084be56d@thread.skype/messages";
//endregion
//region certificate information
    private String storename = "JKSkeystore.jks";
    private String alias = "selfsignedjks";
    private String storepass = "";
//endregion

    private NationalCloud endpoint = NationalCloud.Global;
    private List scopes = Arrays.asList("https://graph.microsoft.com/.default");
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @GetMapping("/notification")
    @ResponseBody
    public String subscribe() throws KeyStoreException, FileNotFoundException, IOException, CertificateException, NoSuchAlgorithmException {
        ClientCredentialProvider authProvider = new ClientCredentialProvider(
            this.clientId,
            this.scopes,
            this.clientSecret,
            this.tenantId,
            this.endpoint);

        IGraphServiceClient graphClient = GraphServiceClient
                            .builder()
                            .authenticationProvider(authProvider)
                            .buildClient();
        graphClient.setServiceRoot("https://graph.microsoft.com/beta");
        Subscription subscription = new Subscription();
        subscription.changeType = "created";
        subscription.notificationUrl = this.publicUrl + "/notification";
        subscription.resource = this.resource;
        subscription.expirationDateTime = Calendar.getInstance();
        subscription.clientState = "secretClientValue";
        
        subscription.expirationDateTime.add(Calendar.HOUR, 1);
        
        if (this.resource.startsWith("teams")) {
            subscription.additionalDataManager().put("includeResourceData", new JsonPrimitive(true));
            subscription.additionalDataManager().put("encryptionCertificate",   new JsonPrimitive(GetBase64EncodedCertificate()));
            subscription.additionalDataManager().put("encryptionCertificateId", new JsonPrimitive(this.alias));
            LOGGER.warn("encoded cert");
            LOGGER.info(GetBase64EncodedCertificate());
        }

        subscription = graphClient.subscriptions()
            .buildRequest()
            .post(subscription);
        return "Subscribed to entity with subscription id " + subscription.id;
    }
    private KeyStore GetCertificateStore() throws KeyStoreException, FileNotFoundException, IOException, CertificateException, NoSuchAlgorithmException  {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(this.storename), this.storepass.toCharArray());
        return ks;
    }
    private String GetBase64EncodedCertificate() throws CertificateEncodingException, KeyStoreException, FileNotFoundException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore ks = this.GetCertificateStore();
        java.security.cert.Certificate cert = ks.getCertificate(this.alias);
        return new String(Base64.encodeBase64(cert.getEncoded()));
    }
    private byte[] GetEncryptionKey(String base64encodedSymetricKey) throws KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, FileNotFoundException, NoSuchPaddingException, IOException, UnrecoverableKeyException, BadPaddingException, CertificateException {
        KeyStore ks = this.GetCertificateStore();
        Key asymmetricKey = ks.getKey(this.alias, this.storepass.toCharArray());
        byte[] encryptedSymetricKey = Base64.decodeBase64(base64encodedSymetricKey);
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, asymmetricKey);
        return cipher.doFinal(encryptedSymetricKey);
    }
    private boolean IsDataSignatureValid(byte[] encryptionKey, String encryptedData, String comparisonSignature) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] decodedEncryptedData = Base64.decodeBase64(encryptedData);
        Mac mac = Mac.getInstance("HMACSHA256");
        SecretKey skey = new SecretKeySpec(encryptionKey, "HMACSHA256");
        mac.init(skey);
        byte[] hashedData = mac.doFinal(decodedEncryptedData);
        String encodedHashedData = new String(Base64.encodeBase64(hashedData));
        return comparisonSignature.equals(encodedHashedData);
    }
    private String GetDecryptedData(byte[] encryptionKey, String encryptedData) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException {
        SecretKey skey = new SecretKeySpec(encryptionKey, "AES");
        IvParameterSpec ivspec = new IvParameterSpec(Arrays.copyOf(encryptionKey, 16));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");// should be 7 but https://stackoverflow.com/a/53139355/3808675
        cipher.init(Cipher.DECRYPT_MODE, skey, ivspec);
        return new String(cipher.doFinal(Base64.decodeBase64(encryptedData)));
    }
    @PostMapping(value="/notification", headers = {"content-type=text/plain"})
    @ResponseBody
    public ResponseEntity<String> handleValidation(@RequestParam(value = "validationToken") String validationToken){
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(validationToken);
    }
    @PostMapping("/notification")
    @ResponseBody
	public ResponseEntity<String> handleNotification(@RequestBody() ChangeNotificationsCollection notifications) throws KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, FileNotFoundException, NoSuchPaddingException, IOException, UnrecoverableKeyException, BadPaddingException, CertificateException, InvalidAlgorithmParameterException {
        LOGGER.info(notifications.value.get(0).resource);
        byte[] decryptedKey = this.GetEncryptionKey(notifications.value.get(0).encryptedContent.dataKey);
        boolean isDataSignatureValid = this.IsDataSignatureValid(decryptedKey, notifications.value.get(0).encryptedContent.data, notifications.value.get(0).encryptedContent.dataSignature);
        if(isDataSignatureValid) {
            String decryptedData = this.GetDecryptedData(decryptedKey, notifications.value.get(0).encryptedContent.data);
            LOGGER.info("decrypted data");
            LOGGER.info(decryptedData);
            return ResponseEntity.ok().body("");
        } else {
            return ResponseEntity.badRequest().body("data signature validation failed");
        }
	}
}

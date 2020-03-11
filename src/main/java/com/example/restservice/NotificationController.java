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

@RestController
public class NotificationController {
//region app registration information
    private String clientId  = "24f1a7a0-bda7-4bf8-b8a6-c915da1d0ddb";
    private String clientSecret = "";
    private String tenantId = "bd4c6c31-c49c-4ab6-a0aa-742e07c20232";
//endregion
//region subscription information
    private String publicUrl = "https://94801ddb.ngrok.io";
    private String resource = "teams/01b4b70e-2ea6-432f-a3d7-eefd826c2a8e/channels/19:81cf89b7ecef4e7994a84ee2cfb3248a@thread.skype/messages";
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
        subscription.changeType = "updated";
        subscription.notificationUrl = this.publicUrl + "/notification";
        subscription.resource = this.resource;
        subscription.expirationDateTime = Calendar.getInstance();
        subscription.clientState = "secretClientValue";
        
        subscription.expirationDateTime.add(Calendar.HOUR, 1);
        
        if (true) { //TODO update condition for teams
            subscription.additionalDataManager().put("includeResourceData", new JsonPrimitive(true));
            subscription.additionalDataManager().put("encryptionCertificate",   new JsonPrimitive(GetBase64EncodedKey()));
            subscription.additionalDataManager().put("encryptionCertificateId", new JsonPrimitive(this.alias));
            LOGGER.warn("cert public key");
            LOGGER.info(GetBase64EncodedKey());
        }

        subscription = graphClient.subscriptions()
            .buildRequest()
            .post(subscription);
        return "Subscribed to entity with subscription id " + subscription.id;
    }
    private String GetBase64EncodedKey() throws KeyStoreException, FileNotFoundException, IOException, CertificateException, NoSuchAlgorithmException  {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(this.storename), this.storepass.toCharArray());
        java.security.cert.Certificate[] cchain = ks.getCertificateChain(this.alias);
        List mylist = new ArrayList();
        for (int i = 0; i < cchain.length; i++) {
            mylist.add(cchain[i]);
        }
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        CertPath cp = cf.generateCertPath(mylist);
        java.security.cert.Certificate certificate = cp.getCertificates().get(0);
        PublicKey publicKey = certificate.getPublicKey();
        byte[] encodedPublicKey = publicKey.getEncoded();
        return new String(Base64.encodeBase64(encodedPublicKey));
    }
    @PostMapping(value="/notification", headers = {"content-type=text/plain"})
    @ResponseBody
    public ResponseEntity<String> handleValidation(@RequestParam(value = "validationToken") String validationToken){
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(validationToken);
    }
    @PostMapping("/notification")
    @ResponseBody
	public ResponseEntity<String> handleNotification(@RequestBody() ChangeNotificationsCollection notifications) {
        LOGGER.info(notifications.value.get(0).resource);
        return ResponseEntity.ok().body("");
	}
}

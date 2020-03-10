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
import java.util.Arrays;
import java.util.List;
import java.util.Calendar;

@RestController
public class NotificationController {
    private String clientId  = "24f1a7a0-bda7-4bf8-b8a6-c915da1d0ddb";
    private String clientSecret = "";
    private String tenantId = "bd4c6c31-c49c-4ab6-a0aa-742e07c20232";
    private NationalCloud endpoint = NationalCloud.Global;
    private List scopes = Arrays.asList("https://graph.microsoft.com/.default");

    @GetMapping("/notification")
    @ResponseBody
    public String subscribe() {
        ClientCredentialProvider authProvider = new ClientCredentialProvider(
            this.clientId,
            this.scopes,
            this.clientSecret,
            this.tenantId,
            this.endpoint);

        GraphServiceClient graphClient = GraphServiceClient
                            .builder()
                            .authenticationProvider(authProvider)
                            .buildClient();
        Subscription subscription = new Subscription();
        subscription.changeType = "updated";
        subscription.notificationUrl = "https://webhook.azurewebsites.net/api/send/myNotifyClient";
        subscription.resource = "me/mailFolders('Inbox')/messages";
        subscription.expirationDateTime = Calendar.getInstance();
        subscription.clientState = "secretClientValue";
        
        graphClient.subscriptions()
            .buildRequest()
            .post(subscription);
        return "Subscribed to entity with subscription id";
    }
    @PostMapping("/notification")
    @ResponseBody
	public String handleNotification(@RequestParam(value = "validationToken", defaultValue = "") String validationToken, @RequestBody(required=false) ChangeNotificationsCollection notifications) {

		return validationToken;
	}
}

package com.example.restservice;

public class ChangeNotification {
    public String subscriptionId;
    public String changeType;
    public String resource;
    public String clientState;
    public ChangeNotificationEncryptedContent encryptedContent;
    public ChangeNotification() {
        this.subscriptionId = "";
        this.changeType = "";
        this.resource = "";
        this.clientState = "";
        this.encryptedContent = null;
    }
}
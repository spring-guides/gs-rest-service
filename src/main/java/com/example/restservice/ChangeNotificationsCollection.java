package com.example.restservice;

import java.util.ArrayList;

public class ChangeNotificationsCollection {
    public ArrayList<ChangeNotification> value;
    public ArrayList<String> validationTokens;
    public ChangeNotificationsCollection() {
        this.value = new ArrayList<ChangeNotification>();
        this.validationTokens = new ArrayList<String>();
    }
}
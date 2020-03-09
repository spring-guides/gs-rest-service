package com.example.restservice;

public class ChangeNotificationEncryptedContent {
    public String data;
    public String dataSignature;
    public String dataKey;
    public String encryptionCertificateId;
    public String encryptionCertificateThumbprint;
    public ChangeNotificationEncryptedContent() {
        this.data = "";
        this.dataSignature = "";
        this.dataKey = "";
        this.encryptionCertificateId = "";
        this.encryptionCertificateThumbprint = "";
    }
}
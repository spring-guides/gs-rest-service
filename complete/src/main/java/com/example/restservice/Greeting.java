package com.example.restservice;

public class Greeting {
    private long id;
    private String content;

    public Greeting(long aId, String aContent) {
        id = aId;
        content = aContent;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}

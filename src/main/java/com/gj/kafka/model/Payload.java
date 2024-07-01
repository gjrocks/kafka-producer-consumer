package com.gj.kafka.model;

public class Payload {
    String key;
    String payload;

    public Payload(String key, String payload) {
        this.key = key;
        this.payload = payload;
    }
    public Payload() {
            }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}

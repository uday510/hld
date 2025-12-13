package com.app.hld.load_balancer;

public class Request {

    private final String key;

    public Request(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

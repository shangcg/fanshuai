package com.fanshuai.netty.protocol.message;

public class Body {

    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toString() {
        return String.format("Body[body=%s]", body);
    }
}

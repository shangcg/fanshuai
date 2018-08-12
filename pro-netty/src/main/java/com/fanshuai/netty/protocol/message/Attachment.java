package com.fanshuai.netty.protocol.message;

public class Attachment {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String toString() {
        return String.format("Attachment[data=%s]", data == null ? "null" : data);
    }
}

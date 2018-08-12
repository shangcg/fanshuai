package com.fanshuai.netty.protocol.message;

public class NettyMessage {
    private Header header;
    private Body body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public String toString() {
        return String.format("NettyMessage[header=%s, body=%s]", header.toString(), body.toString());
    }
}

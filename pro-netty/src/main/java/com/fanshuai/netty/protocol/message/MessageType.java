package com.fanshuai.netty.protocol.message;

public enum MessageType {
    REQ(0),//业务请求消息
    RESP(1),//业务相应消息
    ONE_WAY(2),//即请求又相应
    LOGIN_REQ(3),//握手请求
    LOGIN_RESP(4),//握手相应
    HEART_BEAT_REQ(5),//心跳请求
    HEART_BEAT_RESP(6);//心跳相应

    byte value;

    MessageType(int value) {
        this.value = (byte)value;
    }

    public byte getValue() {
        return value;
    }
}

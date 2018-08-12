package com.fanshuai.netty.protocol.message;

import java.util.HashMap;
import java.util.Map;

public class Header {
    private static final int CRC_CODE = 0xabef0101;//消息magic  number  版本号 子版本号

    private int crcCode = CRC_CODE;
    private int length;
    private long sessionId;
    private byte type;
    private byte priority;
    private Map<String, Attachment> attachment = new HashMap<>();

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getCrcCode() {
        return crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, Attachment> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Attachment> attachment) {
        this.attachment = attachment;
    }

    public String toString() {
        return String.format("Header[crcCode=%s, length=%s, sessionId=%s, type=%s, priority=%s, attachment=%s]", crcCode, length, sessionId, type, priority, attachment == null ? "null" : attachment.toString());
    }
}

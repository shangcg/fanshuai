package com.fanshuai;

public class Status {
    private String text;
    private boolean truncated;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }
}

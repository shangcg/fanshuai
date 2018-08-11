package com.fanshuai;

public class StatusView extends Status{

    public String getText() {
        if (getTruncated()) {
            return "已删除";
        }
        return super.getText();
    }

    public String getRawText() {
        return super.getText();
    }
}

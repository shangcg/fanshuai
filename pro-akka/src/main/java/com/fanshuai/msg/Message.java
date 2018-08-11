package com.fanshuai.msg;

import java.util.Collections;
import java.util.List;

public class Message {
    private int age;
    private List<String> list;

    public Message(int age, List<String> list) {
        this.age = age;
        this.list = Collections.unmodifiableList(list);
    }

    public int getAge() {
        return age;
    }

    public List<String> getList() {
        return list;
    }
}

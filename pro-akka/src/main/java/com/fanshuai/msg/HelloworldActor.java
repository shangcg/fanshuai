package com.fanshuai.msg;

import akka.actor.UntypedActor;
import com.google.gson.Gson;

public class HelloworldActor extends UntypedActor {
    public void onReceive(Object msg) {
        System.out.println("收到的数据位：" + new Gson().toJson(msg));
    }
}

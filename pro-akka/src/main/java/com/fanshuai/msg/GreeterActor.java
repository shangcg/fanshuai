package com.fanshuai.msg;

import akka.actor.UntypedActor;
import com.google.gson.Gson;

public class GreeterActor extends UntypedActor{
    public void onReceive(Object msg) {
        try {
            String message = new Gson().toJson(msg);
            System.out.println(message);
            getSender().tell("消息处理完成", getSelf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

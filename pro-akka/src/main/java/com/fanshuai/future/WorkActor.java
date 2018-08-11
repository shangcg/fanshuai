package com.fanshuai.future;

import akka.actor.UntypedActor;

public class WorkActor extends UntypedActor {

    public void onReceive(Object msg) {
        if (msg instanceof Integer) {
            Integer i = (Integer) msg;
            sender().tell(i * i, self());
        } else {
            unhandled(msg);
        }
    }
}

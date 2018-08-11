package com.fanshuai.helloworld;

import akka.actor.UntypedActor;

public class Greeter extends UntypedActor {

    public void onReceive(Object msg) throws InterruptedException {
        if (msg == HelloWorld.MSG.greet) {
            System.out.println("hello world");
            Thread.sleep(1000L);
            sender().tell(HelloWorld.MSG.done, getSelf());
        }
    }
}

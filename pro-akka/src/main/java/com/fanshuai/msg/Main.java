package com.fanshuai.msg;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("hello");
        ActorRef actorHello = system.actorOf(Props.create(HelloworldActor.class), "hello");
        ActorRef actorGreeter = system.actorOf(Props.create(GreeterActor.class), "greeter");
        System.out.println(actorHello.path());
        System.out.println(actorGreeter.path());

        Message message = new Message(18, Arrays.asList("1", "2"));
        actorGreeter.tell(message, actorHello);

    }
}

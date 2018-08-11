package com.fanshuai.helloworld;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("hello");
        ActorRef h = actorSystem.actorOf(Props.create(HelloWorld.class), "helloworld");

        System.out.println(h.path());
    }
}

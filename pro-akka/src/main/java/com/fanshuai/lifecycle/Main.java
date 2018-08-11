package com.fanshuai.lifecycle;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("lifecycle", ConfigFactory.load());
        ActorRef workActor = system.actorOf(Props.create(WorkActor.class), "work");
        ActorRef watchActor = system.actorOf(Props.create(WatchActor.class, workActor), "watch");

        workActor.tell(WorkActor.MSG.working, ActorRef.noSender());
        workActor.tell(WorkActor.MSG.working, ActorRef.noSender());
        workActor.tell(WorkActor.MSG.working, ActorRef.noSender());
        workActor.tell(WorkActor.MSG.done, ActorRef.noSender());
        workActor.tell(WorkActor.MSG.close, ActorRef.noSender());

    }
}

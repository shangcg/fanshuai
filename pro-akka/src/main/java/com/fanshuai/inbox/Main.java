package com.fanshuai.inbox;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("inbox", ConfigFactory.load());
        ActorRef workActor = system.actorOf(Props.create(WorkActor.class), "workActor");

        Inbox inbox = Inbox.create(system);
        inbox.watch(workActor);
        inbox.send(workActor, WorkActor.MSG.working);
        inbox.send(workActor, WorkActor.MSG.working);
        inbox.send(workActor, WorkActor.MSG.working);
        inbox.send(workActor, WorkActor.MSG.done);
        inbox.send(workActor, WorkActor.MSG.close);

        while(true) {
            try {
                Object message = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
                if (message == WorkActor.MSG.close) {
                    System.out.println("workActor si closing");
                } else if (message instanceof Terminated) {
                    System.out.println("workActor has been closed");
                    system.shutdown();
                    break;
                } else {
                    System.out.println(message.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

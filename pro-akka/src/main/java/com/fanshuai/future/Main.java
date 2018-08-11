package com.fanshuai.future;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.pattern.Patterns;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception{
        ActorSystem system = ActorSystem.create("future");
        ActorRef workActor = system.actorOf(Props.create(WorkActor.class), "workActor");
        ActorRef printActor = system.actorOf(Props.create(PrintActor.class), "printActor");

        Future<Object> future = Patterns.ask(workActor, 5, 3000);
        Future<Object> future2 = Patterns.ask(workActor, 8, 3000);

        int res = (int)Await.result(future, Duration.create(1, TimeUnit.SECONDS));
        System.out.println(res);

        Patterns.pipe(future2, system.dispatcher()).to(printActor);

        workActor.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
}

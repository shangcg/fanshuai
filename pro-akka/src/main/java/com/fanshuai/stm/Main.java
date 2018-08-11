package com.fanshuai.stm;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.transactor.Coordinated;
import akka.util.Timeout;
import scala.concurrent.Await;

import java.util.concurrent.TimeUnit;

public class Main {
    public static CompanyActor companyActor = null;
    public static StaffActor staffActor = null;

    public static void main(String[] args) throws Exception{

        ActorSystem system = ActorSystem.create("stm");
        ActorRef companyActor = system.actorOf(Props.create(CompanyActor.class), "companyActor");
        ActorRef staffActor = system.actorOf(Props.create(StaffActor.class), "staffActor");

        Timeout timeout = new Timeout(10, TimeUnit.SECONDS);
        for (int i = 0; i < 23; i++) {
            companyActor.tell(new Coordinated(i, timeout), ActorRef.noSender());

            Thread.sleep(500L);

            int companyCount = (Integer) Await.result(Patterns.ask(companyActor, "getCount", timeout), timeout.duration());
            int staffCount = (Integer) Await.result(Patterns.ask(staffActor, "getCount", timeout), timeout.duration());

            System.out.println(String.format("companyCount=%s, staffCount=%s", companyCount, staffCount));
            System.out.println("--------------------");
        }
    }
}

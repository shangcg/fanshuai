package com.fanshuai.helloworld;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.TypedActor;
import akka.actor.UntypedActor;

public class HelloWorld extends UntypedActor {
    public enum MSG{
        greet, done
    }

    public void preStart() {
        final ActorRef greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
        greeter.tell(MSG.greet, getSelf());
    }

    public void onReceive(Object msg) {
        if (msg == MSG.done) {
            System.out.println("actor is done");
            getContext().stop(getSelf());
        } else {
            unhandled(msg);
        }
    }
 }

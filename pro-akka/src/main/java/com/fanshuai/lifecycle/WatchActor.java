package com.fanshuai.lifecycle;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WatchActor extends UntypedActor{
    private LoggingAdapter logger = Logging.getLogger(context().system(), this);

    public WatchActor(ActorRef actorRef) {
        getContext().watch(actorRef);
    }
    public void onReceive(Object msg) {
        if (msg instanceof Terminated) {
            logger.error(((Terminated)msg).getActor().path() + " has Terminated. now shutdown the system");
            context().system().shutdown();
        } else {
            unhandled(msg);
        }
    }

}

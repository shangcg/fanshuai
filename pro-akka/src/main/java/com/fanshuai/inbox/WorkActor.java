package com.fanshuai.inbox;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WorkActor extends UntypedActor{
    private LoggingAdapter logger = Logging.getLogger(context().system(), this);
    public enum MSG {
        working, done, close
    }

    public void onReceive(Object msg) {
        if (msg == MSG.working) {
            logger.info("work actor is working");
        } else if (msg == MSG.done){
            logger.info("work actor is done");
        } else if (msg == MSG.close) {
            logger.info("working actor receive close message");
            sender().tell(MSG.close, self());
            context().stop(self());
        }
    }
}

package com.fanshuai.lifecycle;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WorkActor extends UntypedActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    public enum MSG {
        working, done, close
    }

    public void preStart() {
        logger.info("work actor is starting");
    }

    public void postStop() {
        logger.info("work actor is stopping");
    }

    public void onReceive(Object msg) {
        if (msg == MSG.working) {
            logger.info("working actor is working");
        }
        if (msg == MSG.done) {
            logger.info("working actor has done the work");
        }
        if (msg == MSG.close) {
            logger.info("working actor receive close message");
            getSender().tell(MSG.close, self());
            getContext().stop(self());
        }
    }
}

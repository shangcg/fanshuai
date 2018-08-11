package com.fanshuai.future;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PrintActor extends UntypedActor {
    private LoggingAdapter logger = Logging.getLogger(context().system(), this);
    public void onReceive(Object msg) {
        if (msg instanceof Integer) {
            logger.info(msg.toString());
        } else {
            unhandled(msg);
        }
    }
}

package com.fanshuai.stm;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.transactor.Coordinated;
import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;

public class StaffActor extends UntypedActor {
    LoggingAdapter logger = Logging.getLogger(context().system(), this);
    Ref.View<Integer> count = STM.newRef(20);

    public void onReceive(Object msg) {
        if (msg instanceof Coordinated) {
            Coordinated c = (Coordinated) msg;
            int downCount = (int)c.getMessage();

            try {
                c.atomic(() -> {
                    STM.increment(count, downCount);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (msg.equals("getCount")) {
            sender().tell(count.get(), self());
        } else {
            unhandled(msg);
        }
    }
}

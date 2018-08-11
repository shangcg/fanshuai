package com.fanshuai.stm;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.transactor.Coordinated;
import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;

public class CompanyActor extends UntypedActor {
    private LoggingAdapter logger = Logging.getLogger(context().system(), this);
    private Ref.View<Integer> count = STM.newRef(100);

    public void onReceive(Object msg) {
        if (msg instanceof Coordinated) {
            Coordinated c = (Coordinated) msg;
            int downCount = (int)c.getMessage();
            sender().tell(c.coordinate(downCount), self());

            try {
                c.atomic(() -> {
                    if (count.get() < downCount) {
                        throw new RuntimeException("余额不足");
                    }
                    STM.increment(count, -1 * downCount);
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

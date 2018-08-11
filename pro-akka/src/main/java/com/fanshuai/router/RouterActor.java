package com.fanshuai.router;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.typesafe.config.ConfigFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RouterActor extends UntypedActor {
    private LoggingAdapter logger = Logging.getLogger(context().system(), this);
    private Router router = null;

    public void preStart() {
        List<Routee> routees = new ArrayList<>();

        for (int i = 0;i < 5;i++) {
            ActorRef actor = context().actorOf(Props.create(WorkActor.class), "worker-" + i);
            context().watch(actor);
            ActorRefRoutee refRoutee = new ActorRefRoutee(actor);
            routees.add(refRoutee);
        }

        router = new Router(new RoundRobinRoutingLogic(), routees);
    }

    public void onReceive(Object msg) {
        if (msg instanceof WorkActor.MSG) {
            router.route(msg, sender());
        } else if (msg instanceof Terminated) {
            router = router.removeRoutee(((Terminated) msg).actor());
            logger.info(((Terminated) msg).actor().path() + "改actor已删除.router.size = " + router.routees().size());

            if (router.routees().size() == 0) {
                logger.info("actor已全部删除");
                flag.compareAndSet(true, false);
                context().system().shutdown();
            }
        } else {
            unhandled(msg);
        }
    }

    private static AtomicBoolean flag = new AtomicBoolean(true);

    public static void main(String[] args) throws Exception{
        ActorSystem system = ActorSystem.create("router", ConfigFactory.load());

        ActorRef routerActor = system.actorOf(Props.create(RouterActor.class), "routerActor");

        int i = 1;
        while (flag.get()) {
            routerActor.tell(WorkActor.MSG.working, ActorRef.noSender());

            if (i % 10 == 0) {
                routerActor.tell(WorkActor.MSG.close, ActorRef.noSender());
            }
            Thread.sleep(500L);
            i++;
        }
    }
}

package com.fanshuai;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        //logger.info("hello world");
        zookeeperTest1();
    }

    public static void zookeeperTest1() {
        String connString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        ZooKeeper zooKeeper = null;
        try{
            zooKeeper = new ZooKeeper(connString, 3000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    logger.info(String.format("path:%s, eventType:%s, state:%s", watchedEvent.getPath(), watchedEvent.getType(), watchedEvent.getState()));
                }
            });
        } catch (IOException e) {
            logger.error(e.toString());
        }

        if (null != zooKeeper) {
            try {
                List<String> rootList = zooKeeper.getChildren("/", true);
                logger.info(rootList + "");

                //创建node节点
                if (zooKeeper.exists("/node", true) == null) {
                    zooKeeper.create("/node", "linshimei".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    logger.info("create node /node linshimei");
                }

                //创建sub1子节点
                if (zooKeeper.exists("/node/sub1", true) == null) {
                    zooKeeper.create("/node/sub1", "zhiqingzhixing".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    logger.info("create node /node/sub1 zhiqingzhixing");
                }

                //更改节点
                if (zooKeeper.exists("/node", true) != null) {
                    Stat stat = zooKeeper.exists("/node", true);
                    zooKeeper.setData("/node", "new data".getBytes(), stat.getVersion());
                    logger.info("set data on /node");
                }

                if (zooKeeper.exists("/node/sub1", true) != null) {
                    Stat stat1 = zooKeeper.exists("/node/sub1", true);
                    Stat stat2 = zooKeeper.exists("/node", true);

                    zooKeeper.delete("/node/sub1", stat1.getVersion());
                    zooKeeper.delete("/node", stat2.getVersion());

                    logger.info("delete nodes");
                }
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
    }
}

package com.fanshuai.zookeeper1;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZkClient{
    private static Logger logger = LoggerFactory.getLogger(ZkClient.class);

    private ZooKeeper zk = null;
    private CountDownLatch latch = new CountDownLatch(1);
    private String addr = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public void connect() {
        try {
            zk = new ZooKeeper(addr, 30000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        logger.info("zookeeper connected");
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (Exception e) {
            logger.error(e + "");
        }
    }

    private void checkConnect(ZooKeeper zooKeeper) {
        ZooKeeper.States states = zooKeeper.getState();
        //连接中断
        if (states == ZooKeeper.States.CONNECTING || states == ZooKeeper.States.CLOSED) {
            connect();
        }
    }

    public boolean create(String path, byte[] data, CreateMode mode){

        try {
            Stat stat = zk.exists(path, false);
            if (stat == null) {
                String cpath = zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
                logger.info("created path, " + cpath);
            }
            return true;
        } catch (KeeperException e) {
            logger.error(e.toString());
        } catch (InterruptedException e) {
            logger.error(e.toString());
        }
        return false;
    }

    public void setData(String path, byte[] data) {
        try {
            synchronized (this) {
                Stat stat = zk.exists(path, false);
                if (stat != null) {
                    zk.setData(path, data, stat.getVersion());
                }
            }
        } catch (KeeperException e) {
            logger.error(e.toString());
        } catch (InterruptedException e) {
            logger.error(e.toString());
        }
    }

    public boolean exists(String path) {
        try {
            return zk.exists(path, false) == null;
        } catch (KeeperException e) {
            logger.error(e.toString());
        } catch (InterruptedException e) {
            logger.error(e.toString());
        }
        return false;
    }

    public List<String> list(String path, Watcher watcher) {
        try {
            Stat stat = zk.exists(path, false);
            if (stat == null) {
                logger.info("node not exists, " + path);
                return new ArrayList<>();
            }
            return zk.getChildren(path, watcher);
        } catch (KeeperException e) {
            logger.error(e.toString());
        } catch (InterruptedException e) {
            logger.error(e.toString());
        }
        return new ArrayList<>();
    }

    public byte[] getData(String path, Watcher watcher) {
        try {
            Stat stat = zk.exists(path, false);
            if (stat == null) {
                logger.info("node not exists, " + path);
                return null;
            }
            return zk.getData(path, watcher, null);
        } catch (KeeperException e) {
            logger.error(e.toString());
        } catch (InterruptedException e) {
            logger.error(e.toString());
        }
        return null;
    }

    public void close(){
        try {
            zk.close();
        } catch (InterruptedException e) {
            logger.error(e.toString());
        }

    }

    public static List<String> list = null;
    public static byte[] data = null;

    public static void main(String[] args) {
        ZkClient connector = new ZkClient();
        connector.connect();

        connector.create("/node-a", "node-a".getBytes(), CreateMode.PERSISTENT);
        connector.create("/node-a/a", "a".getBytes(), CreateMode.EPHEMERAL);
        connector.create("/node-a/b", "b".getBytes(), CreateMode.EPHEMERAL);
        connector.create("/node-a/c", "c".getBytes(), CreateMode.EPHEMERAL);

        //通过watcher获取最新的list数据
        list = connector.list("/node-a", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                    list = connector.list("/node-a", this);
                }
            }
        });

        //通过watcher获取最新的data
        data = connector.getData("/node-a/c", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                    data = connector.getData("/node-a/c", this);
                }
                if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                    data = null;
                }
            }
        });

        //更新数据
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i< 5; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    ZkClient client = new ZkClient();
                    client.connect();
                    String name = Thread.currentThread().getName();
                    client.create("/node-a/" + name, name.getBytes(), CreateMode.EPHEMERAL);
                    client.close();
                }
            });
        }

        connector.setData("/node-a/c", "ccc".getBytes());

        try {
            Thread.sleep(10000);
            logger.info(list.toString());
            logger.info(new String(data));
        } catch (InterruptedException e) {

        }
        connector.close();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                executor.shutdown();
            }
        }));
    }
}

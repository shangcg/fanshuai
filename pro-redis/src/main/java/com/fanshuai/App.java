package com.fanshuai;

import com.fanshuai.util.JedisSentinelPoolUtil;
import com.fanshuai.util.JedisUtil;
import com.fanshuai.util.ShardedJedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args )
    {
        //testJedis();
        //testJedisSentinel();
        testShardedJedis();
    }

    public static void testJedis() {

        String key = "asen";
        String lkey = "lasen";
        String hkey = "hasen";
        String skey = "sasen";
        String zkey = "zasen";

        JedisUtil.set(key, "1");
        String v1 = JedisUtil.get(key);
        logger.info(String.format("%s -> %s", key, v1));

        JedisUtil.lpush(lkey, "1");
        JedisUtil.lpush(lkey, "2");
        JedisUtil.lpush(lkey, "3");

        long llen = JedisUtil.llen(lkey);
        List<String> list = JedisUtil.lrange(lkey, 0,-1);

        logger.info(String.format("llen -> %s", llen));
        logger.info(String.format("list -> %s", list));

        JedisUtil.sadd(skey, "honglouneng");
        JedisUtil.sadd(skey, "shuihuzhuan");
        JedisUtil.sadd(skey, "xiyouji");
        JedisUtil.sadd(skey, "hongloumeng");

        long slen = JedisUtil.scard(skey);
        Set<String> ss = JedisUtil.smembers(skey);
        logger.info(String.format("ss - %s", ss));

        JedisUtil.zadd(zkey, 11, "111");
        JedisUtil.zadd(zkey, 22, "222");
        JedisUtil.zadd(zkey, 33, "333");
        JedisUtil.zadd(zkey, 333, "333");

        Set<String> z1 = JedisUtil.zrange(zkey, 0,-1);
        Set<Tuple> z2 = JedisUtil.zrangeWithScores(zkey, 0,-1);

        logger.info(String.format("z1 - %s", z1));
        logger.info(String.format("z2 - %s", z2));

    }

    public static void testJedisSentinel() {
        String key = "asen";
        String lkey = "lasen";
        String hkey = "hasen";
        String skey = "sasen";
        String zkey = "zasen";

        JedisSentinelPoolUtil.set(key, "1");
        String v1 = JedisSentinelPoolUtil.get(key);
        logger.info(String.format("%s -> %s", key, v1));

        JedisSentinelPoolUtil.lpush(lkey, "1");
        JedisSentinelPoolUtil.lpush(lkey, "2");
        JedisSentinelPoolUtil.lpush(lkey, "3");

        long llen = JedisSentinelPoolUtil.llen(lkey);
        List<String> list = JedisSentinelPoolUtil.lrange(lkey, 0,-1);

        logger.info(String.format("llen -> %s", llen));
        logger.info(String.format("list -> %s", list));

        JedisSentinelPoolUtil.sadd(skey, "honglouneng");
        JedisSentinelPoolUtil.sadd(skey, "shuihuzhuan");
        JedisSentinelPoolUtil.sadd(skey, "xiyouji");
        JedisSentinelPoolUtil.sadd(skey, "hongloumeng");

        long slen = JedisSentinelPoolUtil.scard(skey);
        Set<String> ss = JedisSentinelPoolUtil.smembers(skey);
        logger.info(String.format("ss - %s", ss));

        JedisSentinelPoolUtil.zadd(zkey, 11, "111");
        JedisSentinelPoolUtil.zadd(zkey, 22, "222");
        JedisSentinelPoolUtil.zadd(zkey, 33, "333");
        JedisSentinelPoolUtil.zadd(zkey, 333, "333");

        Set<String> z1 = JedisSentinelPoolUtil.zrange(zkey, 0,-1);
        Set<Tuple> z2 = JedisSentinelPoolUtil.zrangeWithScores(zkey, 0,-1);

        logger.info(String.format("z1 - %s", z1));
        logger.info(String.format("z2 - %s", z2));
    }

    public static void testShardedJedis() {
        String key = "ashard";
        String lkey = "lashard";
        String hkey = "hashard";
        String skey = "sashard";
        String zkey = "zashard";

        ShardedJedisUtil.set(key, "1");
        String v1 = ShardedJedisUtil.get(key);
        logger.info(String.format("%s -> %s", key, v1));

        ShardedJedisUtil.lpush(lkey, "1");
        ShardedJedisUtil.lpush(lkey, "2");
        ShardedJedisUtil.lpush(lkey, "3");

        long llen = ShardedJedisUtil.llen(lkey);
        List<String> list = ShardedJedisUtil.lrange(lkey, 0,-1);

        logger.info(String.format("llen -> %s", llen));
        logger.info(String.format("list -> %s", list));

        ShardedJedisUtil.sadd(skey, "honglouneng");
        ShardedJedisUtil.sadd(skey, "shuihuzhuan");
        ShardedJedisUtil.sadd(skey, "xiyouji");
        ShardedJedisUtil.sadd(skey, "hongloumeng");

        long slen = ShardedJedisUtil.scard(skey);
        Set<String> ss = ShardedJedisUtil.smembers(skey);
        logger.info(String.format("ss - %s", ss));

        ShardedJedisUtil.zadd(zkey, 11, "111");
        ShardedJedisUtil.zadd(zkey, 22, "222");
        ShardedJedisUtil.zadd(zkey, 33, "333");
        ShardedJedisUtil.zadd(zkey, 333, "333");

        Set<String> z1 = ShardedJedisUtil.zrange(zkey, 0,-1);
        Set<Tuple> z2 = ShardedJedisUtil.zrangeWithScores(zkey, 0,-1);

        logger.info(String.format("z1 - %s", z1));
        logger.info(String.format("z2 - %s", z2));
    }
}

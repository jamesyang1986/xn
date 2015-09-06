package com.xiaonei.db.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtils {
    private static JedisPool pool;
    
    private static void createJedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(100);
        config.setMaxWaitMillis(1000);
        config.setMaxIdle(10);
        pool = new JedisPool(config, "101.200.0.16", 6379);
    }

    private static synchronized void poolInit() {
        if (pool == null)
            createJedisPool();
    }

    public static Jedis getJedis() {
        if (pool == null) {
            synchronized (JedisPoolUtils.class) {
                poolInit();
            }
        }
        return pool.getResource();
    }

    public static void returnRes(Jedis jedis) {
        pool.returnResource(jedis);
    }

    public static void returnBrokenRes(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }
}
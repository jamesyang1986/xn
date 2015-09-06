package com.xiaonei.dao;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.xiaonei.db.utils.Constants;
import com.xiaonei.db.utils.JedisPoolUtils;

public class UserShowDao {
    private final String SHOW_DATA_PREFIX = "user_show_";

    public Map<String, String> getShowIds(String uid) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getJedis();
            return jedis.hgetAll(SHOW_DATA_PREFIX + uid);
        } catch (Exception e) {
            if (jedis != null) {
                JedisPoolUtils.returnBrokenRes(jedis);
            }
        } finally {
            if (jedis != null) {
                JedisPoolUtils.returnRes(jedis);
            }
        }

        return null;
    }

    public void show(String uid, List<String> mids) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getJedis();
            String key = SHOW_DATA_PREFIX + uid;
            boolean ifExists = jedis.exists(key);
            for (String mid : mids) {
                jedis.hincrBy(key, mid, 1);
            }
            if (!ifExists) {
                long curTime = System.currentTimeMillis() / 1000;
                jedis.expireAt(key, curTime + Constants.EXPIRE_TIME);
            }
        } catch (Exception e) {
            if (jedis != null) {
                JedisPoolUtils.returnBrokenRes(jedis);
            }
        } finally {
            if (jedis != null) {
                JedisPoolUtils.returnRes(jedis);
            }
        }

    }
}

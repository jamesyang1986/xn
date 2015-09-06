package com.xiaonei.db.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;

public class WordUtils {
    private static final String COLOR_RED_STR = "红色";
    private static final String COLOR_BLUE_STR = "粉色";

    private static Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();

    public static boolean wordIsSim(String word1, String word2) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getJedis();
            return jedis.hexists(word1, word2) || jedis.hexists(word2, word1);
        } catch (Exception e) {
            if (jedis != null) {
                JedisPoolUtils.returnBrokenRes(jedis);
            }
        } finally {
            if (jedis != null) {
                JedisPoolUtils.returnRes(jedis);
            }
        }
        return false;
    }

    public static void getWordsMap(String path) {
        String line = null;
        try {
            data.clear();
            BufferedReader reader = new BufferedReader(new FileReader(path));
            while ((line = reader.readLine()) != null) {
                genWordTagMap(line, data);
            }
            Jedis jedis = JedisPoolUtils.getJedis();
            for (String key : data.keySet()) {
                Map<String, String> result = data.get(key);
                if (result.size() != 0) {
                    jedis.del(key);
                    jedis.hmset(key, data.get(key));
                }

            }
            JedisPoolUtils.returnRes(jedis);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void genWordTagMap(String line,
            Map<String, Map<String, String>> data) {
        if (StringUtils.isEmpty(line)) {
            return;
        }
        String[] tmp2 = line.split("：");
        int start = line.indexOf(COLOR_RED_STR);
        int end = line.indexOf(COLOR_BLUE_STR);

        Map<String, String> result = new HashMap<String, String>();
        String text2 = line.substring(start, end).replace(COLOR_RED_STR, "")
                .trim();
        String[] tmp3 = text2.replace("（", "").replace("）", "").split("，");
        for (String test : tmp3) {
            if (!StringUtils.isEmpty(test.trim())) {
                result.put(test.trim(), "1");
            }
        }

        String text3 = line.substring(end).replace(COLOR_BLUE_STR, "").trim();
        String[] tmp4 = text3.replace("（", "").replace("）", "").split("，");
        for (String test2 : tmp4) {
            if (!StringUtils.isEmpty(test2.trim())) {
                result.put(test2.trim(), "2");
            }
        }
        data.put(tmp2[0].trim(), result);
    }

    public static void main(String[] args) {
        getWordsMap("/Users/jianjunyang/Downloads/bq2.txt");
    }
}

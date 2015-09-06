package com.xiaonei.rec.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;

import com.xiaonei.dao.UserDao;
import com.xiaonei.dao.UserShowDao;
import com.xiaonei.db.utils.Constants;
import com.xiaonei.db.utils.CosineSimilarAlgorithm;
import com.xiaonei.db.utils.JedisPoolUtils;
import com.xiaonei.db.utils.WordUtils;
import com.xiaonei.pojo.MatchResult;
import com.xiaonei.pojo.UserPushSetting;
import com.xiaonei.pojo.UserTags;
import com.xiaonei.pojo.XnUser;

public class UserRecService {
    private UserDao userDao = new UserDao();
    private UserShowDao showDao = new UserShowDao();

    public void calUserRecResult() {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getJedis();
            while (true) {
                String uid = jedis
                        .rpop(Constants.USER_ONLINE_NOTIFY_QUEUE_NAME);
                genRecUserResult(uid, 20);
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

    public MatchResult calUserSim(String userId, String targetId) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("id", userId);
        List<XnUser> users = userDao.getCondition(paramMap);
        if (users == null || users.size() == 0) {
            throw new RuntimeException("not user exists...user id:" + userId);
        }
        XnUser originUser = users.get(0);

        paramMap.put("id", targetId);
        List<XnUser> users2 = userDao.getCondition(paramMap);
        if (users2 == null || users2.size() == 0) {
            throw new RuntimeException("not user exists...user id:" + targetId);
        }

        XnUser targetUser = users2.get(0);
        MatchResult result = calUserSim(originUser, targetUser);
        return result;
    }

    public static void main(String[] args) {
        System.out.println("============");
        UserRecService userService = new UserRecService();
        userService.genRecUserResult("17", 10);
        // userService.calUserSim("17", "18");

    }

    public List<MatchResult> genRecUserResult(String userId, int maxNum) {

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("id", userId);
        List<MatchResult> resultList = new ArrayList<MatchResult>();
        List<XnUser> users = userDao.getCondition(paramMap);

        if (!CollectionUtils.isEmpty(users)) {
            XnUser originalUser = users.get(0);

            UserPushSetting setting = originalUser.getPushSetting();
            if (setting == null) {
                setting = new UserPushSetting();
                setting.setHometown("all");
                setting.setCity("all");
                setting.setAstrology("all");
                setting.setProvince("all");
                setting.setArea("all");
                setting.setUid(originalUser.getId());
            }

            setting.setHometown("all");
            setting.setCity("all");
            setting.setAstrology("all");
            setting.setProvince("all");
            setting.setArea("all");

            List<XnUser> recUsers = userDao.getCondition(setting);

            Map<String, String> idMap = showDao.getShowIds(userId);

            for (XnUser targetUser : recUsers) {
                if (idMap != null
                        && idMap.containsKey(targetUser.getId() + "")
                        && Integer.parseInt(idMap.get(targetUser.getId() + "")) >= Constants.MAX_USER_REC_COUNT) {
                    continue;
                }
                resultList.add(calUserSim(originalUser, targetUser));
            }

            Collections.sort(resultList, new Comparator<MatchResult>() {
                @Override
                public int compare(MatchResult o1, MatchResult o2) {
                    if (o1.getScore() > o2.getScore())
                        return -1;
                    if (o1.getScore() < o2.getScore())
                        return 1;
                    return 0;
                }
            });
        }

        if (resultList.size() > maxNum) {
            resultList = resultList.subList(0, maxNum);
        }

        return resultList;
    }

    private MatchResult calUserSim(XnUser originalUser, XnUser targetUser) {

        UserTags originalUserTags = originalUser.getTag();
        UserTags targetTags = targetUser.getTag();

        MatchResult result = new MatchResult(originalUser.getId(),
                targetUser.getId());

        Map<String, Double> tagScoreMap = new HashMap<String, Double>();

        result.setDetailScore(tagScoreMap);
        Map<String, Map<String, String>> simWords = new HashMap<String, Map<String, String>>();
        result.setSimWordsMap(simWords);

        simWords.put("tag_aichi", new HashMap<String, String>());
        simWords.put("tag_aiwan", new HashMap<String, String>());
        simWords.put("tag_tingkan", new HashMap<String, String>());
        simWords.put("tag_youfan", new HashMap<String, String>());
        simWords.put("tag_hunji", new HashMap<String, String>());

        double score_aichi = 0.0;
        double score_aiwan = 0.0;
        double score_tinkan = 0.0;
        double score_youfan = 0.0;
        double score_hunji = 0.0;

        if (originalUserTags != null && targetTags != null) {
            score_aichi = getWordSimilarity(simWords.get("tag_aichi"),
                    originalUserTags.getTag_aichi(), targetTags.getTag_aichi());
            score_aiwan = getWordSimilarity(simWords.get("tag_aiwan"),
                    originalUserTags.getTag_aiwan(), targetTags.getTag_aiwan());
            score_tinkan = getWordSimilarity(simWords.get("tag_tingkan"),
                    originalUserTags.getTag_tingkan(),
                    targetTags.getTag_tingkan());
            score_youfan = getWordSimilarity(simWords.get("tag_youfan"),
                    originalUserTags.getTag_youfan(),
                    targetTags.getTag_youfan());
            score_hunji = getWordSimilarity(simWords.get("tag_hunji"),
                    originalUserTags.getTag_hunji(), targetTags.getTag_hunji());
        }

        genSimWordList(result, simWords, "tag_aichi");
        genSimWordList(result, simWords, "tag_aiwan");
        genSimWordList(result, simWords, "tag_tingkan");
        genSimWordList(result, simWords, "tag_youfan");
        genSimWordList(result, simWords, "tag_hunji");

        result.setScore(score_aichi + score_aiwan + score_tinkan + score_youfan
                + score_hunji);

        tagScoreMap.put("tag_aichi", score_aichi);
        tagScoreMap.put("tag_aiwan", score_aiwan);
        tagScoreMap.put("tag_tingkan", score_tinkan);
        tagScoreMap.put("tag_youfan", score_youfan);
        tagScoreMap.put("tag_hunji", score_hunji);

        return result;
    }

    private void genSimWordList(MatchResult result,
            Map<String, Map<String, String>> simWords, String tag) {
        Map<String, String> words = simWords.get(tag);
        List<String> ewordList = new ArrayList<String>();
        List<String> simWordList = new ArrayList<String>();
        if (words != null && words.size() > 0) {
            String[] tmp = words.values().toArray(new String[] {});
            for (String key : tmp) {
                if (words.containsKey(key)) {
                    ewordList.add(key);
                } else {
                    simWordList.add(key);
                }
            }
        }
        result.getSw().put(tag, simWordList);
        result.getEw().put(tag, ewordList);
    }

    public void saveMatchResult(String uid, List<MatchResult> resultList) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getJedis();
            Map<String, Double> scoreMap = new HashMap<String, Double>();
            for (MatchResult result : resultList) {
                scoreMap.put(result.getTargetId() + "", result.getScore());
                String key = Constants.USER_REC_RESULT_DETAIL_PREFIX
                        + result.getSourceId() + "#" + result.getTargetId();
                jedis.zadd(key, result.getDetailScore());
            }
            jedis.zadd(Constants.USER_REC_RESULT_PREFIX + uid, scoreMap);
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

    public double getWordSimilarity(Map<String, String> simWords,
            String words1, String words2) {
        if (StringUtils.isEmpty(words1) || StringUtils.isEmpty(words2)) {
            return 0.0;
        }
        String[] tmp1 = new String[] {};
        String[] tmp2 = new String[] {};

        if (words1.contains(",")) {
            tmp1 = words1.split("\\,");
        } else {
            tmp1 = words1.split(Constants.WORD_SPLIT_CHAR);
        }

        if (words2.contains(",")) {
            tmp2 = words2.split("\\,");
        } else {
            tmp2 = words2.split(Constants.WORD_SPLIT_CHAR);
        }

        Set<String> wordSet = new HashSet<String>();

        double totalSimValue = 0.0;
        for (String word1 : tmp1) {
            for (String word2 : tmp2) {
                word1 = word1.trim();
                word2 = word2.trim();

                if (!StringUtils.isEmpty(word1)) {
                    wordSet.add(word1);
                }

                if (!StringUtils.isEmpty(word2)) {
                    wordSet.add(word2);
                }

                double simValue = 0.0;
                if (StringUtils.isEmpty(word1) || StringUtils.isEmpty(word2)) {
                    continue;
                }

                if (word1.equals(word2)) {
                    simValue = 1.0;
                    simWords.put(word1, word2);
                } else if (WordUtils.wordIsSim(word1, word2)) {
                    simValue = 1.0;
                    simWords.put(word1, word2);
                } else {
                    simValue = CosineSimilarAlgorithm.getSimilarity(word1,
                            word2);
                    if (simValue > 1 || simValue <= 0 || Double.isNaN(simValue)
                            || Double.isInfinite(simValue)) {
                        simValue = 0;
                    } else {
//                        System.out.println("===:" + word1 + "===" + word2);
                    }
                }
                totalSimValue += simValue;
            }
        }
        return (totalSimValue / wordSet.size()) > 1.0 ? 1.0
                : (totalSimValue / wordSet.size());
    }
}

package com.xiaonei.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaonei.db.utils.JsonUtils;

public class MatchResult {
    private Integer sourceId;
    private Integer targetId;
    private double score;

    private Map<String, Double> detailScore = new HashMap<String, Double>();
    private Map<String, Map<String, String>> simWordsMap = new HashMap<String, Map<String, String>>();

    private Map<String, List<String>> ew = new HashMap<String, List<String>>();
    private Map<String, List<String>> sw = new HashMap<String, List<String>>();

    public MatchResult(Integer sourceId, Integer targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Map<String, Double> getDetailScore() {
        return detailScore;
    }

    public void setDetailScore(Map<String, Double> detailScore) {
        this.detailScore = detailScore;
    }

    public Map<String, Map<String, String>> getSimWordsMap() {
        return simWordsMap;
    }

    public void setSimWordsMap(Map<String, Map<String, String>> simWordsMap) {
        this.simWordsMap = simWordsMap;
    }

    public Map<String, List<String>> getEw() {
        return ew;
    }

    public void setEw(Map<String, List<String>> ew) {
        this.ew = ew;
    }

    public Map<String, List<String>> getSw() {
        return sw;
    }

    public void setSw(Map<String, List<String>> sw) {
        this.sw = sw;
    }

    @Override
    public String toString() {
        return JsonUtils.toJSON(this);
    }
}

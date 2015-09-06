package com.xiaonei.pojo;

import java.util.HashMap;
import java.util.Map;

public class PushCommand {
    private String uid;
    private PlatformType platformType;
    private String title;
    private String content;
    private String type;

    private String tag;
    private String alias;

    private Map<String, String> options = new HashMap<String, String>();

    public PlatformType getPlatformType() {
        return platformType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

package com.xiaonei.pojo;

import java.util.Date;

public class UserTags {
    private int id;
    private int user_id;

    private String tag_youfan;
    private String tag_aiwan;
    private String tag_aichi;
    private String tag_tingkan;
    private String tag_hunji;

    private Date create_at;
    private Date updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTag_youfan() {
        return tag_youfan;
    }

    public void setTag_youfan(String tag_youfan) {
        this.tag_youfan = tag_youfan;
    }

    public String getTag_aiwan() {
        return tag_aiwan;
    }

    public void setTag_aiwan(String tag_aiwan) {
        this.tag_aiwan = tag_aiwan;
    }

    public String getTag_aichi() {
        return tag_aichi;
    }

    public void setTag_aichi(String tag_aichi) {
        this.tag_aichi = tag_aichi;
    }

    public String getTag_tingkan() {
        return tag_tingkan;
    }

    public void setTag_tingkan(String tag_tingkan) {
        this.tag_tingkan = tag_tingkan;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getTag_hunji() {
        return tag_hunji;
    }

    public void setTag_hunji(String tag_hunji) {
        this.tag_hunji = tag_hunji;
    }
}

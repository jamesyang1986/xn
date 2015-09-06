package com.xiaonei.pojo;

import java.util.Date;

public class XnUser {
    private int id;
    private String uuid;
    private String nickname;
    private String passWord;
    private String astrology;
    private String mobile;
    private String gender;
    private String province;
    private String city;
    private String area;

    private String birth_year;
    private String birth_month;
    private String birth_day;

    private String company;
    private String school;
    private String hometown;
    private String career;
    private String sexual;

    private String avatar;
    private String lng;
    private String lat;

    private Date created_at;

    private UserTags tag;

    private UserPushSetting pushSetting;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getSexual() {
        return sexual;
    }

    public void setSexual(String sexual) {
        this.sexual = sexual;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }

    public String getBirth_month() {
        return birth_month;
    }

    public void setBirth_month(String birth_month) {
        this.birth_month = birth_month;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }

    public String getAstrology() {
        return astrology;
    }

    public void setAstrology(String astrology) {
        this.astrology = astrology;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public UserTags getTag() {
        return tag;
    }

    public void setTag(UserTags tag) {
        this.tag = tag;
    }

    public UserPushSetting getPushSetting() {
        return pushSetting;
    }

    public void setPushSetting(UserPushSetting pushSetting) {
        this.pushSetting = pushSetting;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        builder.append("[");
        builder.append(" nickname:" + nickname + ",");
        builder.append(" passWord:" + passWord + ",");
        builder.append(" mobile:" + mobile + ",");
        builder.append(" gender:" + gender + ",");
        builder.append(" province:" + province + ",");
        builder.append(" city:" + city + ",");
        builder.append(" area:" + area + ",");
        builder.append(" birth_year:" + birth_year + ",");
        builder.append("]");
        return builder.toString();
    }

}

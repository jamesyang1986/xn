package com.xiaonei.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.xiaonei.db.utils.Constants;
import com.xiaonei.db.utils.DataSourceFactory;
import com.xiaonei.db.utils.JsonUtils;
import com.xiaonei.pojo.UserPushSetting;
import com.xiaonei.pojo.XnUser;

public class UserDao {
    private static Logger logger = Logger.getLogger(UserDao.class);

    private UserTagDao tagDao = new UserTagDao();
    private UserPushSettingDao pushSettingDao = new UserPushSettingDao();

    public List<String> getAllUser(Map<String, String> paramMap) {
        StringBuilder sql = new StringBuilder(
                " select * from `users` where 1=1  ");

        List<String> ids = new ArrayList<String>();
        Connection conn = null;
        try {
            DataSource ds = DataSourceFactory.getDataSource(null);
            conn = ds.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql.toString());

            while (rs.next()) {
                int uid = rs.getInt("id");
                ids.add(uid + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return ids;
    }

    public List<XnUser> getCondition(Map<String, String> paramMap) {
        StringBuilder sql = new StringBuilder(
                " select * from `users` where 1=1  ");

        if (paramMap != null && paramMap.size() != 0) {
            for (String key : paramMap.keySet()) {
                String val = paramMap.get(key);
                sql.append(" and ");
                sql.append(" " + key);
                sql.append(" = ");
                sql.append(" '" + val + "' ");
            }
        }

        List<XnUser> recUsers = new ArrayList<XnUser>();
        Connection conn = null;
        try {
            DataSource ds = DataSourceFactory.getDataSource(null);
            conn = ds.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql.toString());

            while (rs.next()) {
                XnUser xnUser = new XnUser();
                int uid = rs.getInt("id");
                xnUser.setId(uid);
                String uuid = rs.getString("uuid");

                xnUser.setNickname(rs.getString("nickname"));
                xnUser.setMobile(rs.getString("mobile"));
                xnUser.setGender(rs.getString("gender"));
                xnUser.setProvince(rs.getString("province"));
                xnUser.setCity(rs.getString("city"));
                xnUser.setArea(rs.getString("area"));

                xnUser.setBirth_year(rs.getString("birth_year"));
                xnUser.setBirth_month(rs.getString("birth_month"));
                xnUser.setBirth_day(rs.getString("birth_day"));

                xnUser.setAstrology(rs.getString("astrology"));
                xnUser.setSchool(rs.getString("school"));
                xnUser.setCompany(rs.getString("company"));
                xnUser.setHometown(rs.getString("hometown"));
                xnUser.setCareer(rs.getString("career"));
                xnUser.setSexual(rs.getString("sexual"));
                xnUser.setAvatar(rs.getString("avatar"));
                xnUser.setLng(rs.getString("lng"));
                xnUser.setLat(rs.getString("lat"));
                xnUser.setCreated_at(rs.getDate("created_at"));

                xnUser.setPushSetting(pushSettingDao.getUserPushSettingById(uid
                        + ""));
                xnUser.setTag(tagDao.getTagById(uid + ""));
                recUsers.add(xnUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return recUsers;
    }

    public List<XnUser> getCondition(UserPushSetting pushSetting) {

        long t1 = System.currentTimeMillis();
        String rec_date = genDate();

        StringBuilder sql = new StringBuilder(
                " select * from `users` u , user_tags t  where u.id = t.user_id ");

        sql.append(" and u.id != " + pushSetting.getUid() + "  ");
        
        sql.append(" and u.updated_at >= '" + rec_date + "'  ");
//
//        sql.append(" and u.id  not in ( select following_id from followers where follower_id= "
//                + pushSetting.getUid() + ") ");
        //
        // sql.append(" and u.id not in ( select follower_id  from followers where  following_id = "
        // + pushSetting.getUid() + ") ");

        sql.append(" and u.id not in ( select blocked_id  from blockeds  where user_id = "
                + pushSetting.getUid() + ") ");

        sql.append(" and u.id not in ( select user_id from user_settings where field_name = 'online_mode' and field_val='3' "
                + ") ");

        String gender = pushSetting.getGender();
        String astrology = pushSetting.getAstrology();
        String city = pushSetting.getCity();

        String area = pushSetting.getArea();
        String hometown = pushSetting.getHometown();
        String province = pushSetting.getProvince();

        if (!StringUtils.isEmpty(gender) && !"all".equals(gender)
                && !"-1".equals(gender)) {
            sql.append(" and u.gender = '" + gender + "' ");
        }

        if (!StringUtils.isEmpty(city) && !"all".equals(city)) {
            sql.append(" and u.city = '" + city + "' ");
        }

        if (!StringUtils.isEmpty(astrology) && !"all".equals(astrology)) {
            sql.append(" and u.astrology = '" + astrology + "' ");
        }

        if (!StringUtils.isEmpty(province) && !"all".equals(province)) {
            sql.append(" and u.province = '" + province + "' ");
        }

        if (!StringUtils.isEmpty(hometown) && !"all".equals(hometown)) {
            String[] tmps = hometown.split(Constants.WORD_SPLIT_CHAR);
            for (String key : tmps) {
                if (StringUtils.isEmpty(key) || "all".equals(key))
                    continue;
                sql.append(" and u.hometown like '%" + key + "%' ");
            }
        }

        if (!StringUtils.isEmpty(area) && !"all".equals(area)) {
            sql.append(" and u.area = '" + area + "' ");
        }

        sql.append(" and (");
        String tmp = sql.toString();
        parseSettingSql(pushSetting.getTag_aichi(), "t.tag_aichi", sql);
        parseSettingSql(pushSetting.getTag_aiwan(), "t.tag_aiwan", sql);
        parseSettingSql(pushSetting.getTag_tingkan(), "t.tag_tingkan", sql);
        parseSettingSql(pushSetting.getTag_hunji(), "t.tag_hunji", sql);
        parseSettingSql(pushSetting.getTag_youfan(), "t.tag_youfan", sql);

        if (tmp.equals(sql.toString())) {
            sql.append(" 1=1 ");
        } else {
            sql.append(" 1=0 ");
        }
        sql.append(" ) ");

        List<XnUser> recUsers = new ArrayList<XnUser>();
        Connection conn = null;
        try {
            DataSource ds = DataSourceFactory.getDataSource(null);
            conn = ds.getConnection();
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(sql.toString());

            while (rs.next()) {
                XnUser xnUser = new XnUser();
                int uid = rs.getInt("id");
                xnUser.setId(uid);
                String uuid = rs.getString("uuid");

                xnUser.setNickname(rs.getString("nickname"));
                xnUser.setMobile(rs.getString("mobile"));
                xnUser.setGender(rs.getString("gender"));
                xnUser.setProvince(rs.getString("province"));
                xnUser.setCity(rs.getString("city"));
                xnUser.setArea(rs.getString("area"));

                xnUser.setBirth_year(rs.getString("birth_year"));
                xnUser.setBirth_month(rs.getString("birth_month"));
                xnUser.setBirth_day(rs.getString("birth_day"));

                xnUser.setAstrology(rs.getString("astrology"));
                xnUser.setSchool(rs.getString("school"));
                xnUser.setCompany(rs.getString("company"));
                xnUser.setHometown(rs.getString("hometown"));
                xnUser.setCareer(rs.getString("career"));
                xnUser.setSexual(rs.getString("sexual"));
                xnUser.setAvatar(rs.getString("avatar"));
                xnUser.setLng(rs.getString("lng"));
                xnUser.setLat(rs.getString("lat"));
                xnUser.setCreated_at(rs.getDate("created_at"));

                xnUser.setPushSetting(pushSettingDao.getUserPushSettingById(uid
                        + ""));

                xnUser.setTag(tagDao.getTagById(uid + ""));
                recUsers.add(xnUser);
            }

        } catch (Exception e) {
            logger.error("fail to exceute sql uid is:" + pushSetting.getUid(),
                    e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        long t2 = System.currentTimeMillis();

        logger.info("the userid is: " + pushSetting.getUid() + " it cost:"
                + (t2 - t1) + " result num is:" + recUsers.size()
                + " pushsetting is:  " + JsonUtils.toJSON(pushSetting)
                + "  the sql is:   " + sql.toString());
        return recUsers;
    }

    private String genDate() {
        long t1 = System.currentTimeMillis();
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MINUTE, -1 * Constants.REC_USER_LAST_TIME);
        String date = smf.format(c.getTime());
        return date;
    }

    private void parseSettingSql(String tag, String field, StringBuilder sql) {
        if (!StringUtils.isEmpty(tag)) {
            String[] tags = new String[] {};
            if (tag.contains("||")) {
                tags = tag.split("\\|\\|");
            } else {
                tags = tag.split("\\,");
            }

            for (String aTag : tags) {
                sql.append("  " + field + "  like '%" + aTag + "%' ");
                sql.append(" || ");
            }
        }
    }

    public void saveUser(XnUser xnUser) {

        String sql = " insert into `users`(nickname, mobile, gender,"
                + " province,city, area, birth_year, "
                + "birth_month, birth_day, astrology, "
                + "uuid, school, company, hometown,career,"
                + "sexual,avatar,lng, lat,created_at, updated_at,deleted_at ,password) values(?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?, ?,?,?,?, ?,?, ?);";

        DataSource ds;
        Connection conn = null;
        try {
            ds = DataSourceFactory.getDataSource(null);
            conn = ds.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, xnUser.getNickname());
            pst.setString(2, xnUser.getMobile());
            pst.setString(3, xnUser.getGender());
            pst.setString(4, xnUser.getProvince());
            pst.setString(5, xnUser.getCity());
            pst.setString(6, xnUser.getArea());

            pst.setString(7, xnUser.getBirth_year());
            pst.setString(8, xnUser.getBirth_month());
            pst.setString(9, xnUser.getBirth_day());
            pst.setString(10, xnUser.getAstrology());

            pst.setString(11, xnUser.getUuid());
            pst.setString(12, xnUser.getSchool());
            pst.setString(13, xnUser.getCompany());
            pst.setString(14, xnUser.getHometown());
            pst.setString(15, xnUser.getCareer());
            pst.setString(16, xnUser.getSexual());
            pst.setString(17, xnUser.getAvatar());

            pst.setString(18, xnUser.getLng());
            pst.setString(19, xnUser.getLat());
            pst.setDate(20, new java.sql.Date(System.currentTimeMillis()));
            pst.setDate(21, new java.sql.Date(System.currentTimeMillis()));
            pst.setDate(22, new java.sql.Date(System.currentTimeMillis()));

            pst.setString(23, xnUser.getPassWord());
            pst.executeUpdate();

            if (xnUser.getTag() != null) {
                tagDao.saveUserTags(xnUser.getTag());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        XnUser xnUser = new XnUser();
        xnUser.setNickname("jamesyang");
        xnUser.setArea("beijing");
        xnUser.setAvatar("/tmp");
        xnUser.setGender("M");
        xnUser.setBirth_year("1986");
        xnUser.setUuid("sdfsdfasd");
        xnUser.setPassWord("123");

        xnUser.setNickname("fuckasdf");
        xnUser.setMobile("186001238");
        xnUser.setGender("F");
        xnUser.setProvince("jiangxi");
        xnUser.setCity("beijing");
        xnUser.setArea("haidian");

        xnUser.setBirth_year("1986");
        xnUser.setBirth_month("12");
        xnUser.setBirth_day("24");
        xnUser.setAstrology("chunv");

        xnUser.setUuid("sadf");
        xnUser.setSchool("renbishi");
        xnUser.setCompany("sina");
        xnUser.setHometown("jiangxi");
        xnUser.setCareer("manong");
        xnUser.setSexual("M");
        xnUser.setAvatar("/tmp/aa.jpg");
        xnUser.setLng("14.9");
        xnUser.setLat("36.555");
        UserDao dao = new UserDao();
        // dao.saveUser(xnUser);
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("id", "18");
        List<XnUser> userList = dao.getCondition(paramMap);

        for (XnUser user : userList) {
            System.out.println("user data:" + user.toString());
        }

        System.out.println("======finish=======");
    }
}

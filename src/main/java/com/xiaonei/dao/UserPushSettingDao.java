package com.xiaonei.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import org.apache.log4j.Logger;

import com.xiaonei.db.utils.DataSourceFactory;
import com.xiaonei.pojo.UserPushSetting;

public class UserPushSettingDao {
    private Logger logger = Logger.getLogger(UserPushSettingDao.class);

    public void SaveUserPushSetting(UserPushSetting setting) {
        String sql = "insert into push_settings(gender, astrology, province,city,"
                + " area,hometown,push_flag,tag_aichi,"
                + "tag_aiwan,tag_tingkan,tag_youfan,tag_hunji)"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?) ";

        Connection conn = null;
        try {
            DataSource ds = DataSourceFactory.getDataSource(null);
            conn = ds.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, setting.getGender());
            pst.setString(2, setting.getAstrology());
            pst.setString(3, setting.getProvince());
            pst.setString(4, setting.getCity());
            pst.setString(5, setting.getArea());
            pst.setString(6, setting.getHometown());
            pst.setInt(7, setting.getPush_flag());
            pst.setString(8, setting.getTag_aichi());
            pst.setString(9, setting.getTag_aiwan());
            pst.setString(10, setting.getTag_tingkan());
            pst.setString(11, setting.getTag_youfan());
            pst.setString(12, setting.getTag_hunji());
            conn.close();
            conn = null;
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

    public UserPushSetting getUserPushSettingById(String userId) {
        String sql = " select * from  push_settings where user_id = ?";
        Connection conn =  null;
        try {
            DataSource ds = DataSourceFactory.getDataSource(null);
            conn = ds.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();
            UserPushSetting pushSetting = new UserPushSetting();

            if (rs.next()) {
                pushSetting.setUid(rs.getInt("user_id"));
                pushSetting.setGender(rs.getString("gender"));
                pushSetting.setAstrology(rs.getString("astrology"));
                pushSetting.setProvince(rs.getString("province"));
                pushSetting.setCity(rs.getString("city"));
                pushSetting.setArea(rs.getString("area"));
                pushSetting.setHometown(rs.getString("hometown"));

                pushSetting.setPush_flag(rs.getInt("push_flag"));

                pushSetting.setTag_aichi(rs.getString("tag_aichi"));
                pushSetting.setTag_aiwan(rs.getString("tag_aiwan"));
                pushSetting.setTag_tingkan(rs.getString("tag_tingkan"));
                pushSetting.setTag_youfan(rs.getString("tag_youfan"));
                pushSetting.setTag_hunji(rs.getString("tag_hunji"));
                return pushSetting;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return null;
    }

}

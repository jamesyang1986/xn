package com.xiaonei.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.xiaonei.db.utils.DataSourceFactory;
import com.xiaonei.pojo.UserTags;

public class UserTagDao {
    public UserTags getTagById(String userId) {
        String sql = " select * from `user_tags`  where  user_id = ? ";
        DataSource ds;
        Connection conn = null;
        try {
            UserTags tag = new UserTags();
            ds = DataSourceFactory.getDataSource(null);
            conn = ds.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                tag.setUser_id(rs.getInt("user_id"));
                tag.setTag_aichi(rs.getString("tag_aichi"));
                tag.setTag_aiwan(rs.getString("tag_aiwan"));
                tag.setTag_tingkan(rs.getString("tag_tingkan"));
                tag.setTag_youfan(rs.getString("tag_youfan"));
                tag.setTag_hunji(rs.getString("tag_hunji"));
                tag.setCreate_at(rs.getDate("created_at"));
                return tag;
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
        return null;
    }

    public void saveUserTags(UserTags tag) {
        String sql = " insert into `user_tags`(user_id,tag_aichi,tag_aiwan,"
                + "  tag_tingkan ,tag_youfan,tag_hunji)  values (?,?,?,?,?,?)";
        DataSource ds;
        Connection conn = null;
        try {
            ds = DataSourceFactory.getDataSource(null);
            conn = ds.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, tag.getUser_id());
            pst.setString(2, tag.getTag_aichi());
            pst.setString(3, tag.getTag_aiwan());
            pst.setString(4, tag.getTag_tingkan());
            pst.setString(5, tag.getTag_youfan());
            pst.setString(6, tag.getTag_hunji());
            pst.executeUpdate();
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
}

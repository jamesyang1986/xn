package com.xiaonei.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.xiaonei.db.utils.DataSourceFactory;

public class FollowerDao {

    public List<Integer> getFollowers(String followId) {
        String sql = " select * from `followers`  where  following_id = ? ";

        List<Integer> results = new ArrayList<Integer>();
        DataSource ds;
        
        try {
            ds = DataSourceFactory.getDataSource(null);
            Connection conn = ds.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, followId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                results.add(rs.getInt("follower_id"));
            }
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Integer> getFollowings(String followIngId) {
        String sql = " select * from `followers`  where follower_id = ? ";

        List<Integer> results = new ArrayList<Integer>();
        DataSource ds;
        try {
            ds = DataSourceFactory.getDataSource(null);
            Connection conn = ds.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, followIngId);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                results.add(rs.getInt("following_id"));
            }
            conn.close();
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        FollowerDao dao = new FollowerDao();
        List<Integer> results = dao.getFollowers("16");
        for (Integer result : results) {
            System.out.println("=====:" + result);
        }
        System.out.println("====================");
        List<Integer> results2 = dao.getFollowings("16");
        for (Integer result2 : results2) {
            System.out.println("=====:" + result2);
        }
    }
}

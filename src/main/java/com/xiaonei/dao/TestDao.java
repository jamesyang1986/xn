package com.xiaonei.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import com.xiaonei.db.utils.DataSourceFactory;

public class TestDao {

    public static void main(String[] args) {
        
        try {
            DataSource ds = DataSourceFactory.getDataSource(null);
            Connection conn = ds.getConnection();
            PreparedStatement pst = conn
                    .prepareStatement("insert into test_0.kv_1 (key) values(?) ");
            pst.setString(1, "sadf11");
            
            // pst.setString(2, "test88");
            // pst.setBinaryStream(2, new
            // ByteArrayInputStream("test".getBytes()));
            // pst.setLong(3, System.currentTimeMillis() / 1000);

            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

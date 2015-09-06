package com.xiaonei.db.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class MyDataSource implements DataSource {

    private static final String DBDRIVER = "com.mysql.jdbc.Driver";// 驱动类类名
    private static final String DBNAME = "xiaonei";// 数据库名
    private static final String DBURL = "jdbc:mysql://101.200.0.16:3306/xiaonei?useUnicode=true&characterEncoding=utf-8";// 连接URL
    private static final String DBUSER = "xiaonei";// 数据库用户名
    private static final String DBPASSWORD = "123456qwerty";// 数据库密码
    
    private static final String DEFAULT_DB_CONFIG_NAME = "db.properties";

    static {
        Properties props = new Properties();
        try {
            props.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(DEFAULT_DB_CONFIG_NAME));
        } catch (IOException e) {
        }
        
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            Class.forName(DBDRIVER);
            return DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Connection getConnection(String username, String password)
            throws SQLException {
        try {
            Class.forName(DBDRIVER);
            return DriverManager.getConnection(DBURL,username,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}

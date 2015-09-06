package com.xiaonei.db.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import java.io.IOException;
import java.sql.Connection;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import utils.Constants;
import utils.BeanUtils;

public class DataSourceFactory {
    private static Logger logger = Logger.getLogger(DataSourceFactory.class);
    private static final String DEFAULT_DB_CONFIG_NAME = "db.properties";

    private static Map<String, String> defaultConfigMap = new ConcurrentHashMap<String, String>();

    private static volatile DataSource dataSource = new MyDataSource();

    // init default db config
//    static {
//        try {
//            Properties props = new Properties();
//            props.load(Thread.currentThread().getContextClassLoader()
//                    .getResourceAsStream(DEFAULT_DB_CONFIG_NAME));
//
//            Set<Map.Entry<Object, Object>> propSet = props.entrySet();
//            for (Map.Entry<Object, Object> entry : propSet) {
//                defaultConfigMap.put(entry.getKey().toString().trim(), entry
//                        .getValue().toString().trim());
//            }
//            
////            getDataSource2(null);
//        } catch (Exception e) {
//            logger.error("Error loading properties, filepath: "
//                    + DEFAULT_DB_CONFIG_NAME, e);
//        }
//    }

    public static void initDataSourceParams(Map<String, String> props) {
        if (defaultConfigMap == null) {
            defaultConfigMap = props;
        } else {
            defaultConfigMap.putAll(props);
        }
    }

    public static synchronized DataSource getDataSource(
            Map<String, String> props) {
        return dataSource;
    }
    
    public static synchronized DataSource getDataSource5(
            Map<String, String> props) throws Exception {
        // init db config map

        if (dataSource != null)
            return dataSource;

        Map<String, String> dbConfigMap = new HashMap<String, String>();
        dbConfigMap.putAll(defaultConfigMap);
        if (props != null && props.size() != 0) {
            dbConfigMap.putAll(props);
        }

        try {
            dataSource = new ComboPooledDataSource();
            Set<String> configKeys = dbConfigMap.keySet();
            for (String key : configKeys) {
                String value = dbConfigMap.get(key);
                try {
                    BeanUtils.invokeSet(dataSource, key, value);
                } catch (Exception e) {
                    logger.error("fail to set config to datasource, key is:"
                            + key + " value is:" + value, e);
                }
            }

            List<Connection> connList = new ArrayList<Connection>();
            for (int i = 0; i < Constants.DB_INIT_CONN_NUM; i++) {
                try {
                    connList.add(dataSource.getConnection());
                } catch (Exception e) {
                    logger.error("fail to create connection ipAndPort is:"
                            + dbConfigMap.get("jdbcUrl"), e);
                    if (i > 2)
                        throw e;
                }
            }
            for (Connection conn : connList) {
                try {
                    conn.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return dataSource;
    }
}

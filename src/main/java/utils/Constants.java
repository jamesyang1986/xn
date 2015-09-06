package utils;

public class Constants {
    public static final String DATASOURCE_DRIVER_CLASS = "driverClass";
    public static final String DATASOURCE_USER_KEY = "user";
    public static final String DATASOURCE_PASSWORD_KEY = "password";
    public static final String DATASOURCE_MINPOOLSIZE_KEY = "minPoolSize";
    public static final String DATASOURCE_MAXPOOLSIZE_KEY = "maxPoolSize";
    public static final String DATASOURCE_INITIALPOOLSIZE_KEY = "initialPoolSize";
    public static final String DATASOURCE_MAXIDLETIME_KEY = "maxIdleTime";
    public static final String DATASOURCE_ACQUIREINCREMENT_KEY = "acquireIncrement";
    public static final String DATASOURCE_TESTCONNECTIONONCHECKIN_KEY = "testConnectionOnCheckin";
    public static final String DATASOURCE_IDLECONNECTIONTESTPERIOD_KEY = "idleConnectionTestPeriod";

    public static final int DB_INIT_CONN_NUM = 5;

    public static final String JDBC_URL_SUFFIX = "?useUnicode=true&characterEncoding=utf-8";

    public static final String CONFIG_ZK_ROOT_PATH = "/mysqlconfigs";
    
    public static final String SQL_USE_MASTER_TOKEN = " /** use master */ ";
    
    public static final String DEFAULT_DB_SHARD_RULE = "";
    public static final String DEFAULT_TABLE_SHARD_RULE = "";
    
}
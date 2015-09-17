package com.xiaonei.db.utils;

public class Constants {
    public static final String USER_REC_RESULT_PREFIX = "user_rec_";
    public static final String USER_REC_RESULT_DETAIL_PREFIX = "user_rec_detail_";
    public static final String USER_ONLINE_NOTIFY_QUEUE_NAME = "user_online_notify";
    public static final String WORD_SPLIT_CHAR = "\\|\\|";

    public static int MAX_USER_REC_COUNT = 1;

    public static int EXPIRE_TIME = (int) (12 * 3600);

    public static int REC_USER_LAST_TIME = 15;

    public static int REC_USER_LOOP_TIME = 10;
}

package com.xiaonei.db.utils;

public class Constants {
    public static final String USER_REC_RESULT_PREFIX = "user_rec_";
    public static final String USER_REC_RESULT_DETAIL_PREFIX = "user_rec_detail_";
    public static final String USER_ONLINE_NOTIFY_QUEUE_NAME = "user_online_notify";
    public static final String WORD_SPLIT_CHAR = "\\|\\|";

    public static final int MAX_USER_REC_COUNT = 10;

    public static final int EXPIRE_TIME = (int) (0.1 * 3600);
}

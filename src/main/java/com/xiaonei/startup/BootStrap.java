package com.xiaonei.startup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.xiaonei.dao.UserDao;
import com.xiaonei.db.utils.Constants;
import com.xiaonei.pojo.MatchResult;
import com.xiaonei.pojo.PlatformType;
import com.xiaonei.pojo.PushCommand;
import com.xiaonei.rec.service.PushService;
import com.xiaonei.rec.service.UserRecService;

public class BootStrap {
    private static Logger logger = Logger.getLogger(BootStrap.class);

    private static final UserDao userDao = new UserDao();
    private static final UserRecService recservice = new UserRecService();
    private static final PushService pushService = new PushService();

    private static final ExecutorService es = Executors.newFixedThreadPool(5);

    public static void start() {
        final ScheduledExecutorService esa = Executors
                .newScheduledThreadPool(2);
        esa.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                List<String> uids = userDao.getAllUser(null);
                for (final String userId : uids) {
                    es.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                List<MatchResult> resultList = recservice
                                        .genRecUserResult(userId,
                                                Integer.MAX_VALUE);
                                if (resultList != null && resultList.size() > 0) {
                                    PushCommand command = new PushCommand();
                                    command.setContent("最新推送："
                                            + resultList.size() + "位符合条件的新用户");
                                    command.setUid(userId);
                                    command.setType("2");
                                    command.setPlatformType(PlatformType.all);
                                    command.setAlias(userId);
                                    command.getOptions().put("num",
                                            resultList.size() + "");
                                    pushService.pushUserNotice(command);
                                    logger.info(" finish to rec user for userId:"
                                            + userId
                                            + " it has "
                                            + resultList.size()
                                            + " person to be rec..");
                                }
                            } catch (Exception e) {
                                logger.error(
                                        "user rec push fail... the uid is:"
                                                + userId, e);
                            }
                        }
                    });

                }
            }
        }, 1, Constants.REC_USER_LOOP_TIME, TimeUnit.MINUTES);
    }

    private static String genDate() {
        long t1 = System.currentTimeMillis();
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MINUTE, -15);
        String date = smf.format(c.getTime());
        return date;
    }

    public static void main(String[] args) {
        start();
    }

    private static void test() {
        PushCommand command = new PushCommand();
        command.setContent("test" + "上线了");
        command.setUid("23");
        command.setType("1");
        command.setPlatformType(PlatformType.all);
        command.setAlias("23");
        try {
            pushService.pushUserNotice(command);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

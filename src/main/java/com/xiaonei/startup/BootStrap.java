package com.xiaonei.startup;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.xiaonei.dao.UserDao;
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
        esa.scheduleAtFixedRate(new Runnable() {
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
                                    command.setContent("" + resultList.size()
                                            + "位符合条件的新用户");
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
        }, 0, 60, TimeUnit.MINUTES);
    }

    public static void main(String[] args) {
        start();
    }
}

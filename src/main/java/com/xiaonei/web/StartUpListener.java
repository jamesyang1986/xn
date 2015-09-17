package com.xiaonei.web;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.xiaonei.db.utils.Constants;
import com.xiaonei.db.utils.JsonUtils;
import com.xiaonei.startup.BootStrap;

public class StartUpListener implements ServletContextListener {

    private Logger logger = Logger.getLogger(StartUpListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sc) {

        try {
            InputStream in = sc.getServletContext().getResourceAsStream(
                    "WEB-INF/conf/conf.properties");
            Properties prop = new Properties();
            prop.load(in);

            if (prop.containsKey("MAX_USER_REC_COUNT")) {
                Constants.MAX_USER_REC_COUNT = Integer.parseInt(prop.get(
                        "MAX_USER_REC_COUNT").toString());
            }

            if (prop.containsKey("EXPIRE_TIME")) {
                Constants.EXPIRE_TIME = Integer.parseInt(prop
                        .get("EXPIRE_TIME").toString());
            }

            if (prop.containsKey("REC_USER_LAST_TIME")) {
                Constants.REC_USER_LAST_TIME = Integer.parseInt(prop.get(
                        "REC_USER_LAST_TIME").toString());
            }

            if (prop.containsKey("REC_USER_LOOP_TIME")) {
                Constants.REC_USER_LOOP_TIME = Integer.parseInt(prop.get(
                        "REC_USER_LOOP_TIME").toString());
            }

            logger.info("REC_USER_LOOP_TIME:" + Constants.REC_USER_LOOP_TIME
                    + "REC_USER_LAST_TIME:" + Constants.REC_USER_LAST_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BootStrap.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}

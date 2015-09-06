package com.xiaonei.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.xiaonei.startup.BootStrap;

public class StartUpListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        BootStrap.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}

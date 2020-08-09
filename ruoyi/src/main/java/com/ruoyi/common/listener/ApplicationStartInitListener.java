package com.ruoyi.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.ruoyi.common.utils.RegionAnalyzeUtil;

@WebListener
public class ApplicationStartInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        //初始化行政区域
        RegionAnalyzeUtil.initRegion();

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub

    }

}

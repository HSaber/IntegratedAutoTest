package com.autotest.ui.pageshelper;

import com.autotest.ui.pages.SimpleOrderCMPage;
import com.autotest.ui.utils.SeleniumUtil;
import org.apache.log4j.Logger;

public class SimpleOrderCMPageHelper {
    // 提供本类中日志输出对象
    public static Logger logger = Logger.getLogger(SimpleOrderCMPageHelper.class);
    public static void waitCloudSimpleOrderPageLoad(SeleniumUtil seleniumUtil, int timeOut) {
        logger.info("开始等待云简单列表元素加载");
        seleniumUtil.waitForElementToLoad(timeOut, SimpleOrderCMPage.SOC_LINK_NAV);
        logger.info("云简单列表元素加载完毕");
    }

    public static void waitCMSimpleOrderPageLoad(SeleniumUtil seleniumUtil, int timeOut) {
        logger.info("开始等待客户简单列表元素加载");
        seleniumUtil.click(SimpleOrderCMPage.SOC_TAB_CMSIMORDER);
        seleniumUtil.waitForElementToLoad(timeOut, SimpleOrderCMPage.SOC_TEXT_CMSIMHEADER);
        logger.info("客户简单列表元素加载完毕");
    }
}

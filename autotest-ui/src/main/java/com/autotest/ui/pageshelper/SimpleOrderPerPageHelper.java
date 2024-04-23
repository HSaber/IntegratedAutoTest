package com.autotest.ui.pageshelper;

import com.autotest.ui.pages.SimpleOrderCMPage;
import com.autotest.ui.pages.SimpleOrderPerPage;
import com.autotest.ui.utils.SeleniumUtil;
import org.apache.log4j.Logger;

public class SimpleOrderPerPageHelper {
    // 提供本类中日志输出对象
    public static Logger logger = Logger.getLogger(SimpleOrderPerPageHelper.class);
    public static void waitSimpleOrderPageLoad(SeleniumUtil seleniumUtil, int timeOut) {
        logger.info("开始等待简单列表元素加载");
        seleniumUtil.waitForElementToLoad(timeOut, SimpleOrderPerPage.SOP_LINK_NAV);
        logger.info("简单列表元素加载完毕");
    }

}

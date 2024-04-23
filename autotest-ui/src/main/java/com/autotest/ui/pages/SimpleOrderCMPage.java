package com.autotest.ui.pages;

import org.openqa.selenium.By;

public class SimpleOrderCMPage {
    /**左侧栏目打开简单页面**/
    public static final By SOC_LINK_NAV=By.xpath("//*[@id=\"nav_chat\"]/div[2]/div/ul/li[2]/span/a");
    /**tab 我的云简单**/
    public static final By SOC_TAB_CLOUDORDER=By.id("rc-tabs-2-tab-1");
    /**tab 客户简单**/
    public static final By SOC_TAB_CMSIMORDER=By.id("rc-tabs-2-tab-2");
    public static final By SOC_TEXT_CMSIMHEADER=By.xpath("//*[@id=\"rc-tabs-2-panel-2\"]/div/div[2]/div/span[3]");

}

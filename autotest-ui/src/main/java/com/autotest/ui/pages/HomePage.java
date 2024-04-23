package com.autotest.ui.pages;

import org.openqa.selenium.By;

/**
 * @description 首页面元素定位声明
 * */
public class HomePage {
    /**登录用户名**/
    public static final By HP_TEXT_USERNAME=By.xpath("//*[@id=\"htmlRoot\"]/body/div[5]/div/div/div/ul/li[1]/span/div/div[2]/span[1]");
    /**头部头像**/
    public static final By HP_DROPDOWN_HEADER=By.xpath("//*[@id=\"app\"]/section/section/div[2]/div[1]/span/img");
    /**用户名显示区域*/
    /**Flights按钮*/
    public static final By HP_BUTTON_FLIGHTS = By.xpath("//*[@src='/WebTours/images/flights.gif']");
    /**Itinerary按钮*/
    public static final By HP_BUTTON_ITINERARY = By.xpath("//*[@src='/WebTours/images/itinerary.gif']");
    /**Home按钮*/
    public static final By HP_BUTTON_HOME = By.xpath("//*[@src='/WebTours/images/in_home.gif']");
    /**Sign Off按钮*/
    public static final By HP_BUTTON_SIGNOFF = By.xpath("//*[@src='/WebTours/images/signoff.gif']");
    /**首页完整文本*/
    public static final By HP_TEXT_HOME= By.xpath("//blockquote");
    /**首页Tab*/
    public static final By HP_TEXT_TAB=By.cssSelector("span.ml-1:nth-child(1)");
    /**左下底部*/
    public static final By HP_IMG_LEFTBOTTOM=By.cssSelector(".iitoo-header-user-dropdown__header");
    /**我的企业**/
    public static final By HP_BUTTON_MYCOMPANY = By.className("add-business-dropdown");
    /**交易**/
    public static final By HP_BUTTON_TRANSACTION = By.cssSelector("#app > section > section > div.mini-sider > div:nth-child(1) > ul > li:nth-child(5)");

}
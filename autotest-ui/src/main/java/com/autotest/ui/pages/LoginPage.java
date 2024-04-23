package com.autotest.ui.pages;

import org.openqa.selenium.By;
/**
 * @description 登录页面元素定位声明
 * */
public class LoginPage {
    /**用户名输入框*/
    public static final By LP_INPUT_USERNAME = By.id("form_item_account");
    /**密码输入框*/
    public static final By LP_INPUT_PASSWORD = By.id("form_item_password");
    /**登录按钮*/
    public static final By LP_BUTTON_LOGIN = By.xpath("//*[@id=\"rc-tabs-0-panel-1\"]/form/div[4]/div/div/div/button");
//*[@id="rc-tabs-0-panel-1"]/form/div[4]/div/div/div/button
//*[@id="rc-tabs-0-panel-1"]/form/div[4]/div/div/div/button
    /**登录错误信息*/
    public static final By LP_TEXT_ERROR= By.xpath("//*[@color='red']");

}
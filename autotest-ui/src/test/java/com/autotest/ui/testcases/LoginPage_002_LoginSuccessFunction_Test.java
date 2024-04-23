package com.autotest.ui.testcases;

import com.autotest.ui.base.BasePrepare;
import com.autotest.ui.pageshelper.HomePageHelper;
import com.autotest.ui.pageshelper.LoginPageHelper;
import org.testng.annotations.Test;

/**
 * @description 登录之后验证用户名是不是正确的
 * */

public class LoginPage_002_LoginSuccessFunction_Test extends BasePrepare{
    @Test
    public void loginSuccessFunction() {
        //等待登录页面加载
        LoginPageHelper.waitLoginPageLoad(seleniumUtil, timeOut);
        // 输入登录信息
        LoginPageHelper.typeLoginInfo(seleniumUtil,"86135049444444", "111111");
        //等待首页元素显示出来
        HomePageHelper.waitHomePageLoad(seleniumUtil, timeOut);
        //检查用户名是不是期望的"jojo"
        HomePageHelper.checkUserName(seleniumUtil, timeOut, "孙绍宇");
    }
}
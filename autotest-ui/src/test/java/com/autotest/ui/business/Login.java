package com.autotest.ui.business;

import com.autotest.ui.base.BasePrepare;
import com.autotest.ui.pageshelper.HomePageHelper;
import com.autotest.ui.pageshelper.LoginPageHelper;
import org.testng.annotations.Test;

public class Login extends BasePrepare {

    @Test(priority = 0,parameters = {"username","password","realname"})
    public void loginPersonSuccessFunction(String username,String password,String realname) {
        //等待登录页面加载
        LoginPageHelper.waitLoginPageLoad(seleniumUtil, timeOut);
        // 输入登录信息
        LoginPageHelper.typeLoginInfo(seleniumUtil,username, password);
        //等待首页元素显示出来
        HomePageHelper.waitHomePageLoad(seleniumUtil, timeOut);
        //检查用户名是不是期望的"jojo"
        HomePageHelper.checkUserName(seleniumUtil, timeOut, realname);
    }
}

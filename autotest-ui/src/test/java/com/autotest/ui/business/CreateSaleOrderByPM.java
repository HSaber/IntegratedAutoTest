package com.autotest.ui.business;

import com.autotest.ui.base.BasePrepare;
import com.autotest.ui.pageshelper.HomePageHelper;
import com.autotest.ui.pageshelper.LoginPageHelper;
import com.autotest.ui.utils.Tools;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class CreateSaleOrderByPM extends BasePrepare {
    Tools tool=new Tools();
    String subject = null;
    int num=23;//商品条数
    String ordersubject="请勿动2023-07-25 17:07:35 孙绍宇新建简单 共22个商品";
    //产品登录
    @Test(priority = 4)
    public void loginPMSuccessFunction() {
        //等待登录页面加载
        LoginPageHelper.waitLoginPageLoad(seleniumUtil, timeOut);
        // 输入登录信息
        LoginPageHelper.typeLoginInfo(seleniumUtil,"8613597864640", "111111");
        //等待首页元素显示出来
        HomePageHelper.waitHomePageLoad(seleniumUtil, timeOut);
        //检查用户名是不是期望的"jojo"
        HomePageHelper.checkUserName(seleniumUtil, timeOut, "王平");
    }

    public void createSaleOrderByPM(){
        seleniumUtil.click(By.xpath("/html/body/div[1]/section/section/div[3]/div[2]/div/div[1]/div/ul/li[2]/span/a"));
        seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector(".row--current > td:nth-child(2) > div:nth-child(1) > div:nth-child(1) > span:nth-child(1)"));
        //客户简单
        seleniumUtil.click(By.cssSelector("#rc-tabs-2-tab-2"));
        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div[2]/div/div[2]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[10]/div"));

        String getSubject=seleniumUtil.getText(By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div[2]/div/div[2]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[10]"));
        for(int i=1;i<num;i++){
            getSubject=seleniumUtil.getText(By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div[2]/div/div[2]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr["+i+"]/td[10]/div/span"));
            if(getSubject.equals(subject)){
                seleniumUtil.click(By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div[2]/div/div[2]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr["+i+"]/td[12]/div/span[1]"));
            }
        }

    }
}

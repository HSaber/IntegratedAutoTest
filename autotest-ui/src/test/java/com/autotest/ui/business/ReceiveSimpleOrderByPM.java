package com.autotest.ui.business;

import com.autotest.ui.base.BasePrepare;
import com.autotest.ui.pages.SimpleOrderCMPage;
import com.autotest.ui.utils.Tools;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class ReceiveSimpleOrderByPM extends BasePrepare {
    Tools tool=new Tools();
    String subject = null;
    int num=19;//商品条数
//    //产品登录
//    @Test(priority = 4)
//    public void loginPMSuccessFunction() {
//        //等待登录页面加载
//        LoginPageHelper.waitLoginPageLoad(seleniumUtil, timeOut);
//        // 输入登录信息
//        LoginPageHelper.typeLoginInfo(seleniumUtil,"8613597864640", "111111");
//        //等待首页元素显示出来
//        HomePageHelper.waitHomePageLoad(seleniumUtil, timeOut);
//        //检查用户名是不是期望的"jojo"
//        HomePageHelper.checkUserName(seleniumUtil, timeOut, "王平");
//    }
    @Test(priority = 5)
    public void receiveSimpleOrderByPM()throws Exception{
        seleniumUtil.click(SimpleOrderCMPage.SOCLOUD_LINK_NAV);
        seleniumUtil.waitForElementToLoad(timeOut,SimpleOrderCMPage.SOCLOUDLISTPM_TEXT_ID);
//        Assert.assertEquals(subject,cloudSubject);
        seleniumUtil.click(SimpleOrderCMPage.SOCLOUDLISTPM_TEXT_ID);
        seleniumUtil.waitForElementToLoad(timeOut,SimpleOrderCMPage.SOCLOUDDETAILPM_BUTTON_TAKEORDER);
        for(int i=3;i<=num-1;i++){
            seleniumUtil.click(By.xpath(SimpleOrderCMPage.BUTTON_TAKEORDER+"/tr["+i+"]/td[10]/div/button"));
            if(i==1){

                seleniumUtil.waitForElementToLoad(timeOut,By.className("ant-modal-content"));
                seleniumUtil.pause(1);
//                seleniumUtil.click(By.cssSelector("label.ant-checkbox-wrapper:nth-child(3) > span:nth-child(1) > input:nth-child(1)"));
//                seleniumUtil.click(By.cssSelector("button.ant-btn:nth-child(4)"));

                seleniumUtil.click(SimpleOrderCMPage.SOCLOUDDETAILPM_INPUT_NOTTAKEN);
                seleniumUtil.click(SimpleOrderCMPage.SOCLOUDDETAILPM_BUTTION_NOTTAKEN);

            }
            seleniumUtil.pause(1);
           /* //合并订单
            if(i==2){
                seleniumUtil.waitForElementToLoad(timeOut, By.xpath("/html/body/div[7]/div/div[2]/div/div[2]"));
                Thread.sleep(1000);
                seleniumUtil.click(By.xpath("/html/body/div[7]/div/div[2]/div/div[2]/div[3]/label[1]/span/input"));
                seleniumUtil.click(By.xpath("/html/body/div[7]/div/div[2]/div/div[2]/div[3]/button[1]"));
            }*/
            //生成新单
            if(i==2){
                seleniumUtil.waitForElementToLoad(timeOut, By.className("ant-modal-content"));
                seleniumUtil.pause(1);
                seleniumUtil.click(SimpleOrderCMPage.SOCLOUDDETAILPM_INPUT_MERGE);
                seleniumUtil.click(SimpleOrderCMPage.SOCLOUDDETAILPM_BUTTION_MERGE);
            }
            String attributeText=seleniumUtil.getAttributeText(By.xpath(SimpleOrderCMPage.BUTTON_TAKEORDER+"/tr["+i+"]/td[10]/div/button"),"style");
            while(attributeText.contains("blue")){
                seleniumUtil.pause(1);
                attributeText=seleniumUtil.getAttributeText(By.xpath(SimpleOrderCMPage.BUTTON_TAKEORDER+"/tr["+i+"]/td[10]/div/button"),"style");
            }
        }

    }
}

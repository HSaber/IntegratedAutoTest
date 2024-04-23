package com.autotest.ui.business;

import com.autotest.ui.base.BasePrepare;
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
        seleniumUtil.click(By.xpath("/html/body/div[1]/section/section/div[3]/div[2]/div/div[2]/div/ul/li[2]/span/a"));
        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div[2]/div/div[2]/div/div[1]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[9]"));
//        Assert.assertEquals(subject,cloudSubject);
        seleniumUtil.click(By.cssSelector(".row--current > td:nth-child(4) > div:nth-child(1) > a:nth-child(1)"));
        seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector("tr.vxe-body--row:nth-child(1) > td:nth-child(11) > div:nth-child(1) > button:nth-child(1)"));
        for(int i=1;i<=num-1;i++){
            seleniumUtil.click(By.cssSelector("tr.vxe-body--row:nth-child("+i+") > td:nth-child(11) > div:nth-child(1) > button:nth-child(1)"));
            if(i==1){

                seleniumUtil.waitForElementToLoad(timeOut,By.className("ant-modal-content"));
                Thread.sleep(1000);
                seleniumUtil.click(By.cssSelector("label.ant-checkbox-wrapper:nth-child(3) > span:nth-child(1) > input:nth-child(1)"));
                seleniumUtil.click(By.cssSelector("button.ant-btn:nth-child(4)"));
            }
            System.out.println("i="+i);
            Thread.sleep(1000);
           /* //合并订单
            if(i==2){
                seleniumUtil.waitForElementToLoad(timeOut, By.xpath("/html/body/div[7]/div/div[2]/div/div[2]"));
                Thread.sleep(1000);
                seleniumUtil.click(By.xpath("/html/body/div[7]/div/div[2]/div/div[2]/div[3]/label[1]/span/input"));
                seleniumUtil.click(By.xpath("/html/body/div[7]/div/div[2]/div/div[2]/div[3]/button[1]"));
            }*/
            //生成新单
            if(i==2){
                seleniumUtil.waitForElementToLoad(timeOut, By.xpath("/html/body/div[7]/div/div[2]/div/div[2]"));
                Thread.sleep(1000);
                seleniumUtil.click(By.xpath("/html/body/div[7]/div/div[2]/div/div[2]/div[3]/label[2]/span/input"));
                seleniumUtil.click(By.xpath("/html/body/div[7]/div/div[2]/div/div[2]/div[3]/button[2]"));
            }
            String attributeText=seleniumUtil.getAttributeText(By.cssSelector("tr.vxe-body--row:nth-child("+i+") > td:nth-child(11) > div:nth-child(1) > button:nth-child(1)"),"style");
            while(attributeText.contains("blue")){
                Thread.sleep(1000);
                attributeText=seleniumUtil.getAttributeText(By.cssSelector("tr.vxe-body--row:nth-child("+i+") > td:nth-child(11) > div:nth-child(1) > button:nth-child(1)"),"style");
            }
        }

    }
}

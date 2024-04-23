package com.autotest.ui.testcases;

import com.autotest.ui.base.BasePrepare;
import com.autotest.ui.utils.Tools;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @description 登录之后验证用户名是不是正确的
 * */

public class Login extends BasePrepare{
    Tools tool=new Tools();
    @Test()
    public void loginSuccessFunction() throws Exception{
        JavascriptExecutor js=null;
        int num=23;//商品条数
        //等待登录页面加载
//        LoginPageHelper.waitLoginPageLoad(seleniumUtil, timeOut);
        // 输入登录信息
        //        LoginPageHelper.typeLoginInfo(seleniumUtil,"86135049444444", "111111");
        //等待首页元素显示出来
//        HomePageHelper.waitHomePageLoad(seleniumUtil, timeOut);
        //检查用户名是不是期望的"jojo"
//        HomePageHelper.checkUserName(seleniumUtil, timeOut, "孙绍宇");
 //       seleniumUtil.findElementBy(By.id("form_item_account")).click();


        seleniumUtil.waitForElementToLoad(timeOut+timeOut,By.id("form_item_account"));
        seleniumUtil.findElementBy(By.id("form_item_account")).sendKeys("sunshaoyu99999999999");
        seleniumUtil.findElementBy(By.id("form_item_password")).sendKeys("111111");
        seleniumUtil.click(By.xpath("//*[@id=\"app\"]/div/div/div/div[2]/div/form/div[4]/div/div/div/button"));
        seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector(".iitoo-header-user-dropdown__header"));
        seleniumUtil.mouseMoveToElement(By.cssSelector(".iitoo-header-user-dropdown__header"));
        seleniumUtil.waitForElementToLoad(timeOut+timeOut,By.className("ant-popover-inner-content"));
        String username=seleniumUtil.getText(By.className("ant-popover-inner-content"));
        Assert.assertEquals("孙绍宇",username);
        //获取ul进行判断
       //新建简单
        //进入简单列表
        seleniumUtil.click(By.xpath("//*[@id=\"nav_chat\"]/div[2]/div/ul/li[1]/span/a"));
        //校验列表字段

         seleniumUtil.waitForElementToLoad(timeOut=20,By.cssSelector("#iitoo-layout-main > div.iitoo-layout-content.full > div > div > div.tp > div.select-box > button"));
         String[] fieldarr= {"#","状态","优先级","用途","建单时间","编号","交货城市","截止时间","主题","子单统计","操作"};
        for(int i=1;i<=11;i++){
            String str=seleniumUtil.getText(By.xpath(
                    "/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div[2]/div[1]/div/div[2]/div[1]/div[1]/table/thead/tr/th["+i+"]/div"));
            seleniumUtil.isTextCorrect(str,fieldarr[i-1]);
        }

        seleniumUtil.click(By.cssSelector("#iitoo-layout-main > div.iitoo-layout-content.full > div > div > div.tp > div.select-box > button"));
        seleniumUtil.waitForElementToLoad(timeOut,By.id("form_item_missionNumber"));
        //校验详情字段
        String[] detailarr={"13","补充库存",tool.dateAdd(8),"","低级","","生成子单","删 除"};
        String subject = null;
        for(int i=1;i<=8;i++){
            By by=null;
            String str;
            switch (i){
                case 1:
                    by=By.xpath("//*[@id=\"form_item_missionNumber\"]");
                    break;
                case 2:
                   by=By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div/form/div/div[5]/div/div[2]/div/div/div/div/div/span[2]");
                    str=seleniumUtil.findElementBy(by).getAttribute("innerHTML");
                  //  Assert.assertEquals(str,detailarr[i-1]);
                    break;
                case 3:
                    by=By.id("form_item_endTime");
                    str=seleniumUtil.findElementBy(by).getAttribute("title");
                    Assert.assertEquals(str,detailarr[i-1]);
                    break;
                case 4:
                    by=By.id("form_item_deliveryAddress");
             //       str=seleniumUtil.findElementBy(by).getText();
                    break;
                case 5:
                    by=By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div/form/div/div[4]/div/div[2]/div/div/div/div/div/span[2]");
                    str=seleniumUtil.findElementBy(by).getAttribute("innerHTML");
                    break;
                case 6:
                    by=By.id("form_item_subject");
                    str=seleniumUtil.findElementBy(by).getText();
                    Assert.assertEquals(str,detailarr[i-1]);
                    break;
                case 7:
                     js = (JavascriptExecutor) seleniumUtil.driver;
                    String flag=js.executeScript("return document.querySelector(\"button.ant-btn:nth-child(2)\").hasAttribute(\"disabled\");").toString();
                    Assert.assertEquals("true",flag);
                    str=seleniumUtil.findElementBy(By.cssSelector("button.ant-btn:nth-child(2)")).getText();
                    Assert.assertEquals(str,detailarr[i-1]);
                    break;
                case 8:
                     js = (JavascriptExecutor) seleniumUtil.driver;
                    flag=js.executeScript("return document.querySelector(\"button.ant-btn:nth-child(3)\").hasAttribute(\"disabled\");").toString();
                    Assert.assertEquals("true",flag);
                    str=seleniumUtil.findElementBy(By.cssSelector("button.ant-btn:nth-child(3)")).getText();
                    Assert.assertEquals(str,detailarr[i-1]);
                    break;

            }
            if(i==6){
                subject="请勿动"+tool.currentTime()+" "+username+"新建简单 共"+(num-1)+"个商品";
                seleniumUtil.findElementBy(by).sendKeys(subject);
            }
        }
        //选择城市
        seleniumUtil.findElementBy(By.cssSelector(".ant-cascader > div:nth-child(1) > span:nth-child(1)")).click();
        seleniumUtil.findElementBy(By.xpath("/html/body/div[5]/div/div/div/div/ul/li[1]/div[1]")).click();
        seleniumUtil.findElementBy(By.xpath("/html/body/div[5]/div/div/div/div/ul[2]/li[1]/div[1]")).click();
        seleniumUtil.findElementBy(By.xpath("/html/body/div[5]/div/div/div/div/ul[3]/li[1]/div")).click();

        for(int i=1;i<=num;i++){
            seleniumUtil.click(By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[2]/div/div[2]/button"));
            seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector("tr.vxe-body--row:nth-child("+i+") > td:nth-child(2) > div:nth-child(1) > input:nth-child(1)"));
            if(i==1)
            {
                //第一个商品设置图片
                 js = (JavascriptExecutor) seleniumUtil.driver;
                js.executeScript(" var d1= document.querySelector(\"tr.vxe-body--row:nth-child(1) > td:nth-child(9) > div:nth-child(1)\") ;" +
                        "d1.innerHTML=\"\";"+
                        "var im=document.createElement(\"img\");" +
                        "im.src=\"http://119.3.135.155:90/upload/files/2023-05-22/d2608567ed724a768c1ded04f1a0989cfile.gif\";" +
                        "im.style.width=\"60px\";" +
                        "im.style.margin=\"0px auto\";"+
                        "d1.appendChild(im);");

            }


            seleniumUtil.findElementBy(By.cssSelector("tr.vxe-body--row:nth-child("+i+") > td:nth-child(2) > div:nth-child(1) > input:nth-child(1)")).sendKeys("简单商品编号:"+i);
            seleniumUtil.findElementBy(By.cssSelector("tr.vxe-body--row:nth-child("+i+") > td:nth-child(3) > div:nth-child(1) > input:nth-child(1)")).sendKeys("简单商品名称:"+i);
            seleniumUtil.findElementBy(By.cssSelector("tr.vxe-body--row:nth-child("+i+") > td:nth-child(4) > div:nth-child(1) > input:nth-child(1)")).sendKeys("个"+i);
            seleniumUtil.findElementBy(By.cssSelector("tr.vxe-body--row:nth-child("+i+") > td:nth-child(5) > div:nth-child(1) > input:nth-child(1)")).sendKeys(100*i+"");
            seleniumUtil.findElementBy(By.cssSelector("tr.vxe-body--row:nth-child("+i+") > td:nth-child(6) > div:nth-child(1) > input:nth-child(1)")).sendKeys("车型"+i);
            seleniumUtil.findElementBy(By.cssSelector("tr.vxe-body--row:nth-child("+i+") > td:nth-child(7) > div:nth-child(1) > input:nth-child(1)")).sendKeys("机型"+i);
            seleniumUtil.findElementBy(By.cssSelector("tr.vxe-body--row:nth-child("+i+") > td:nth-child(8) > div:nth-child(1) > input:nth-child(1)")).sendKeys("参数"+i);

            if(i==num){
                //商品删除按钮名称校验
                String button= seleniumUtil.findElementBy(By.cssSelector("tr.vxe-body--row:nth-child("+i+") > td:nth-child(11) > div:nth-child(1) > button:nth-child(1)")).getText();
                Assert.assertEquals(button,"删除");
                seleniumUtil.findElementBy(By.cssSelector("tr.vxe-body--row:nth-child("+i+") > td:nth-child(11) > div:nth-child(1) > button:nth-child(1)")).click();
                seleniumUtil.waitForElementToLoad(timeOut,By.className("ant-modal-content"));
                seleniumUtil.findElementBy(By.cssSelector(".ant-modal-confirm-btns > button:nth-child(2)")).click();
            }
        }
        //点击保存 弹框校验
        seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector("button.ant-btn-round:nth-child(1)"));
        seleniumUtil.findElementBy(By.cssSelector("button.ant-btn-round:nth-child(1)")).click();
        seleniumUtil.waitForElementToLoad(timeOut,By.className("ant-message-notice-content"));

        //三个操作按钮的状态及文本

         js = (JavascriptExecutor) seleniumUtil.driver;
        String flag=js.executeScript("return document.querySelector(\"button.ant-btn-primary:nth-child(1)\").hasAttribute(\"disabled\");").toString();
        Assert.assertEquals("false",flag);

         js = (JavascriptExecutor) seleniumUtil.driver;
         flag=js.executeScript("return document.querySelector(\"button.ant-btn-primary:nth-child(2)\").hasAttribute(\"disabled\");").toString();
        Assert.assertEquals("false",flag);
        String buttonstr=seleniumUtil.findElementBy(By.cssSelector("button.ant-btn:nth-child(2)")).getText();
        Assert.assertEquals(buttonstr,"生成子单");

        js = (JavascriptExecutor) seleniumUtil.driver;
        flag=js.executeScript("return document.querySelector(\"button.ant-btn-primary:nth-child(3)\").hasAttribute(\"disabled\");").toString();
        Assert.assertEquals("false",flag);
        buttonstr=seleniumUtil.findElementBy(By.cssSelector("button.ant-btn:nth-child(3)")).getText();
        Assert.assertEquals(buttonstr,"删 除");


        //回到简单列表
        seleniumUtil.click(By.xpath("/html/body/div[1]/section/section/div[3]/div[2]/div/div[2]/div/ul/li[1]/span/a"));
        seleniumUtil.waitForElementToLoad(timeOut=20,By.cssSelector(".row--current > td:nth-child(9)"));
        String subjectget=seleniumUtil.findElementBy(By.cssSelector(".row--current > td:nth-child(9) > div:nth-child(1) > span:nth-child(1)")).getText();
        Assert.assertEquals(subject,subjectget);
        //校验商品行数
        seleniumUtil.click(By.cssSelector(".row--current > td:nth-child(6) > div:nth-child(1) > a:nth-child(1)"));
        seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector("tr.vxe-body--row:nth-child(1) > td:nth-child(2) > div:nth-child(1) > input:nth-child(1)"));
         js = (JavascriptExecutor) seleniumUtil.driver;
        String rows=js.executeScript("return document.getElementsByClassName(\"vxe-table--body\")[0].rows.length").toString();
        System.out.println("rows"+rows);
        Assert.assertEquals((num-1)+"",rows);
        //校验商品操作
        String button= seleniumUtil.findElementBy(By.cssSelector("tr.vxe-body--row:nth-child(1) > td:nth-child(11) > div:nth-child(1) > button:nth-child(1)")).getText();
        Assert.assertEquals(button,"删除");
//<img style="width: 60px; margin: 0px auto;" src="http://116.62.110.78:90/upload/files/2023-05-22/29c6d0fcd75d46eda7d49ae05dd4fad3file.gif" alt="">

        /**生成子单调试部分**/
/*        //进入简单列表
        seleniumUtil.click(By.xpath("//*[@id=\"nav_chat\"]/div[2]/div/ul/li[1]/span/a"));
        //校验列表字段
        seleniumUtil.waitForElementToLoad(timeOut=20,By.xpath("//*[@id=\"iitoo-layout-main\"]/div[3]/div/div/div[1]/div[2]/div[1]/div[2]/div[1]/div[2]/table"));


        //从列表返回简单详情
        seleniumUtil.click(By.xpath("/html/body/div[1]/section/section/section/div[3]/div/div/div[1]/div[2]/div[1]/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[6]/div/a"));
 //       seleniumUtil.get("http://116.62.110.78:90/#/task/simpleTaskDetail/0?type=5&id=2059&sim=1");
        seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector("tr.vxe-body--row:nth-child(1) > td:nth-child(2) > div:nth-child(1) > input:nth-child(1)"));
      //验证按钮状态


        //点击生成子单
        seleniumUtil.click(By.cssSelector("button.ant-btn:nth-child(2)"));
        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("/html/body/div[1]/section/section/section/div[3]/div/div/div[1]/div/form/div/div[7]/div/div[2]/div/div/div/div/div"));
        //校验窗口字段
        String[] suborderDetail={"","","补充库存",tool.dateAdd(8),"中国 / 北京 / 东城","低级","2023-05-25 14:19:26 孙绍宇新建简单"};
        Thread.sleep(5000);
        for(int i=1;i<=suborderDetail.length;i++){
            By by = null;
            if(i==1)
                 by=By.id("advanced_search_enterpriseUserLogin");
            if(i==2)
                by=By.id("advanced_search_enterpriseUserName");
            if(i==3)
                by=By.xpath("/html/body/div[1]/section/section/section/div[3]/div/div/div[1]/div/form/div/div[4]/div/div[2]/div/div/div/div/div/span[2]");
            if(i==4)
                by=By.id("advanced_search_endTime");
            if(i==5)
                by=By.cssSelector(".ant-cascader > div:nth-child(1) > span:nth-child(2)");
            if(i==6)
                by=By.xpath("/html/body/div[1]/section/section/section/div[3]/div/div/div[1]/div/form/div/div[7]/div/div[2]/div/div/div/div/div/span[2]");
          *//*  if(i==7)
                by=By.id("advanced_search_subject");*//*
            String str;
            if(i==4)
                str=seleniumUtil.getAttributeText(by,"title");
           else if(i==5)
                str=seleniumUtil.findElementBy(by).getAttribute("innerHTML");
            else
                str=seleniumUtil.getText(by);
            System.out.println(by+" :"+str);
            Assert.assertEquals(str,suborderDetail[i-1]);
        }*/

    }
}
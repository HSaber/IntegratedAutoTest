package com.autotest.ui.business;

import com.autotest.ui.base.BasePrepare;
import com.autotest.ui.pageshelper.HomePageHelper;
import com.autotest.ui.pageshelper.LoginPageHelper;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class GeneratSubSimpleOrderByPerson extends BasePrepare{

    @Test
    public void createSubsimpleOrderByPerson() throws Exception{
        int circle=10;
        int pageexpect=1;
        //进入简单列表
        seleniumUtil.click(By.xpath("//*[@id=\"nav_chat\"]/div[3]/div/ul/li[1]/span/a"));
        seleniumUtil.waitForElementToLoad(timeOut=20,By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div[2]/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[11]/div/div/span[1]"));
        //点击生成子单按钮
        seleniumUtil.click(By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div[2]/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[11]/div/div/span[1]"));
        //供应商
        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[2]/div/div[2]/div/div/div/div"));
        seleniumUtil.click(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[2]/div/div[2]/div/div/div/div"));
        seleniumUtil.click(By.cssSelector("div.ant-select-item:nth-child(1) > div:nth-child(1)"));

        //点击日期控件
        seleniumUtil.click(By.className("ant-picker-input"));
        seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector(".ant-picker-content > tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(1) > div:nth-child(1)"));

        //优先级
        seleniumUtil.mouseMoveToElement(By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div/form/div/div[6]/div/div[2]/div/div/div/div/div/span[2]"));
        seleniumUtil.click(By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div/form/div/div[6]/div/div[2]/div/div/div/div/div/span[2]"));
        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("//*[@id=\"htmlRoot\"]/body/div[8]/div/div/div/div[2]/div[1]/div/div/div[2]/div"));
        seleniumUtil.click(By.xpath("//*[@id=\"htmlRoot\"]/body/div[8]/div/div/div/div[2]/div[1]/div/div/div[2]/div"));

        //点击添加
        seleniumUtil.click(By.cssSelector(".ant-btn-sm > span:nth-child(1)"));
        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("//*[@class=\"table-top\"]/div/div[2]/div[1]/div[1]"));
        //点击全选
       int col=Integer.parseInt(seleniumUtil.getAttributeText(By.xpath("//*[@class=\"table-top\"]/div/div[2]/div[1]/div[1]"),"xid"));
       seleniumUtil.click(By.cssSelector("th.col_"+(col+1)+" > div:nth-child(1) > span:nth-child(1) > span:nth-child(1)"));

       //计算页数
        int page=0;
        String total=seleniumUtil.getText(By.cssSelector(".vxe-pager--total"));
        total=total.substring(2);
        total=total.substring(0,total.length()-4);
        int totalcnt=Integer.parseInt(total);
        if(totalcnt%10!=0)
            page=totalcnt/10+1;
        else
            page=totalcnt/10;

        System.out.println("page:"+page);
     //   根据页数确定跳转次数
        By by=By.className("vxe-pager--goto");
        String curstartorder=seleniumUtil.getText(By.cssSelector("tr.vxe-body--row:nth-child(1) > td:nth-child(2) > div:nth-child(1)"));
        int start=1;
       while(start<page){
           seleniumUtil.click(By.cssSelector(".vxe-pager--next-btn"));
           while((start*10+1)!=Integer.parseInt(curstartorder)){
               Thread.sleep(500);
               curstartorder=seleniumUtil.getText(By.cssSelector("tr.vxe-body--row:nth-child(1) > td:nth-child(2) > div:nth-child(1)"));
               System.out.println("curstartorder:"+curstartorder);
           }
           seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector("th.col_"+(col+1)+" > div:nth-child(1) > span:nth-child(1) > span:nth-child(1)"));
           seleniumUtil.click(By.cssSelector("th.col_"+(col+1)+" > div:nth-child(1) > span:nth-child(1) > span:nth-child(1)"));
           start++;
       }
       //点击中间件确定
       seleniumUtil.click(By.cssSelector(".ant-modal-footer > button:nth-child(2)"));
       //点击保存
       seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector("button.ant-btn-round:nth-child(1)"));
       seleniumUtil.click(By.cssSelector("button.ant-btn-round:nth-child(1)"));

        //disable=false;
        Boolean disable=seleniumUtil.isEnabled(seleniumUtil.driver.findElement(By.cssSelector("button.ant-btn:nth-child(2)")));
       while(!disable&&circle>0){
           Thread.sleep(500);
           disable=seleniumUtil.isEnabled(seleniumUtil.driver.findElement(By.cssSelector("button.ant-btn:nth-child(2)")));
           System.out.println("disable:"+disable);
           circle--;
       }

       //点击简单导航
        seleniumUtil.click(By.xpath("//*[@id=\"nav_chat\"]/div[3]/div/ul/li[1]/span/a"));
        seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector(".row--current > td:nth-child(10)"));
        //点击子单统计数
        seleniumUtil.click(By.cssSelector(".row--current > td:nth-child(10) > div:nth-child(1) > span:nth-child(1)"));
        seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector("td.vxe-body--column:nth-child(13) > div:nth-child(1) > button:nth-child(1)"));
        //点击发送按钮
        seleniumUtil.click(By.cssSelector("td.vxe-body--column:nth-child(13) > div:nth-child(1) > button:nth-child(1)"));
        String status=seleniumUtil.getText(By.cssSelector("td.vxe-body--column:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > span:nth-child(1)"));
        circle=10;
        while (status.equals("草稿")&&circle>0){
            circle--;
            Thread.sleep(500);
            status=seleniumUtil.getText(By.cssSelector("td.vxe-body--column:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > span:nth-child(1)"));
        }

  //      seleniumUtil.click(By.cssSelector(".row--current > td:nth-child(10) > div:nth-child(1) > span:nth-child(1)"));

    }

}

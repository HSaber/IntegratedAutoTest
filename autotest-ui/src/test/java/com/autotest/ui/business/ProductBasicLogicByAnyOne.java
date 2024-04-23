package com.autotest.ui.business;

import com.autotest.ui.base.BasePrepare;
import com.autotest.ui.utils.Tools;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;


public class ProductBasicLogicByAnyOne extends BasePrepare {
    static Logger logger = Logger.getLogger(ProductBasicLogicByAnyOne.class.getName());
    Tools tools=new Tools();
    @Test(invocationCount = 5)
    @Parameters("realname")
    public void createProduct(String realname) throws Exception{
        String proStr="-"+tools.generateRandomString(2)+"-"+realname;
        seleniumUtil.click(By.cssSelector(".anticon-shopping > svg:nth-child(1)"));
        seleniumUtil.waitForElementToLoad(timeOut,By.className("ant-collapse-header"));
        seleniumUtil.click(By.cssSelector(".vxe-button:nth-child(2) > .vxe-button--content"));
        //输入零件号
        seleniumUtil.waitForElementToLoad(timeOut,By.id("form_item_productCode"));
        seleniumUtil.type(By.id("form_item_productCode"),"3282160");
        String classstr=seleniumUtil.getAttributeText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div/div/div/div[1]"),"class");
        int waittime=1;

        while(!classstr.equals("ant-spin ant-spin-lg ant-spin-show-text spin-loadding-main")){
            seleniumUtil.pause(1);
            classstr=seleniumUtil.getAttributeText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div/div/div/div[1]"),"class");
            Assert.assertTrue(timeOut>waittime++);
        }

        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("//*[@class=\"rc-virtual-list-holder-inner\"]/div/div"));
        seleniumUtil.click(By.xpath("//*[@class=\"rc-virtual-list-holder-inner\"]/div/div"));
        String title=seleniumUtil.findElementBy(By.cssSelector(".ant-select-allow-clear > div:nth-child(1) > span:nth-child(2)")).getAttribute("title");
        waittime=1;
        while(title.isEmpty()){
            seleniumUtil.pause(1);
            title=seleniumUtil.findElementBy(By.cssSelector(".ant-select-allow-clear > div:nth-child(1) > span:nth-child(2)")).getAttribute("title");
            Assert.assertTrue(timeOut>waittime++);
        }

        seleniumUtil.findElementBy(By.id("form_item_productCode")).clear();

        seleniumUtil.findElementBy(By.id("form_item_productCode")).sendKeys(proStr);
        seleniumUtil.findElementBy(By.id("form_item_name1")).clear();
        seleniumUtil.type(By.id("form_item_name1"),proStr);
        for(int i=2;i<9;i++){
            if(i==5)
                continue;
            seleniumUtil.findElementBy(By.cssSelector(".ant-row:nth-child("+i+") .ant-input")).sendKeys(i+"");
        }
 //       seleniumUtil.click(By.id("rc_select_7"));
//        seleniumUtil.findElementBy(By.cssSelector(".ant-select-item-option-active > .ant-select-item-option-content")).click();
        for(int i=1;i<4;i++){
            seleniumUtil.click(By.className("ant-upload"));
            //Java运行时对象
            Runtime runtime = Runtime.getRuntime();
            try {
                //执行
//                runtime.exec("D:\\test2.exe");
                runtime.exec("D:\\"+i+".exe");
            }catch (IOException e){
                e.printStackTrace();
            }
            Thread.sleep(5000);
        }
        seleniumUtil.click(By.className("vxe-icon-save"));
        seleniumUtil.waitForElementToLoad(timeOut,By.className("ant-collapse-header"));

    }
}

package com.autotest.ui.business;

import com.autotest.ui.base.BasePrepare;
import com.autotest.ui.pages.SpacePage;
import com.autotest.ui.pages.TransactionPage;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import java.util.List;

/**
 * ClassName:Test
 * Package:com.autotest.ui.business
 * Description:
 *
 * @Author huhuan
 * @Create 2024/3/15 10:31
 * @Version 1.0
 */
public class Demo extends BasePrepare {
    static Logger logger = Logger.getLogger(Demo.class.getName());

    @Test
    public void OrganizationTest() throws Exception {

        seleniumUtil.pause(3);
    }

    public void changeSpace(String type) throws Exception {
        WebElement element = seleniumUtil.findElementBy(SpacePage.SP_LIST_SPACE);
        // 找到所有的<li>子元素
        List<WebElement> liElements = element.findElements(By.tagName("li"));
        // 遍历<li>元素
        int len = liElements.size();
        for (int i=1;i<=len;i++) {
            // 执行操作，例如打印每个<li>的文本
            String lipath = SpacePage.STR_SP_LIST_SPACE+"/li["+i+"]";
            WebElement liElement = seleniumUtil.findElementBy(By.xpath(lipath));
            String liclass = liElement.getAttribute("class");
            //当前为加入空间按钮跳过
            if(liclass.contains("add-business-btn"))
                continue;
            String path = SpacePage.STR_SP_LIST_SPACE+"/li["+i+"]/div";
            WebElement divElement = seleniumUtil.findElementBy(By.xpath(path));
            // 获取<div>的属性值，这里假设是获取id属性
            String divclass = divElement.getAttribute("class");
            logger.info("divclass:"+divclass);
            String imgpath = SpacePage.STR_SP_LIST_SPACE+"/li["+i+"]/div/img";
            WebElement imgElement = seleniumUtil.findElementBy(By.xpath(imgpath));
            String imgurl = imgElement.getAttribute("src");
             if("个人".equals(type)&&imgurl.contains("header")) {
                 if(divclass.contains("room-contant-bus-active"))
                     break;
                 else {
                     seleniumUtil.click(By.xpath(SpacePage.STR_SP_LIST_SPACE+"/li["+i+"]/div"));
                     seleniumUtil.pause(1);
                     seleniumUtil.click(TransactionPage.TP_ICON_TRANSACTION);
                     seleniumUtil.pause(1);
                     break;
                 }

            }
             if("法人".equals(type)&&imgurl.contains("base")){
                 Actions actions = new Actions(seleniumUtil.driver);
                 element = seleniumUtil.findElementBy(By.xpath(SpacePage.STR_SP_LIST_SPACE+"/li["+i+"]/div"));
                 actions.moveToElement(element).perform();
                 if(seleniumUtil.isElementExist(By.className("add-business-dropdown")))
                     if(divclass.contains("room-contant-bus-active"))
                         break;
                 else {
                         seleniumUtil.click(By.xpath(SpacePage.STR_SP_LIST_SPACE+"/li["+i+"]/div"));
                         seleniumUtil.pause(1);
                         seleniumUtil.click(TransactionPage.TP_ICON_TRANSACTION);
                         seleniumUtil.pause(1);

                         break;
                     }
             }
            if("员工".equals(type)&&imgurl.contains("base")){
                Actions actions = new Actions(seleniumUtil.driver);
                element = seleniumUtil.findElementBy(By.xpath(SpacePage.STR_SP_LIST_SPACE+"/li["+i+"]/div"));
                actions.moveToElement(element).perform();
                if(!seleniumUtil.isElementExist(By.className("add-business-dropdown")))
                    if(divclass.contains("room-contant-bus-active"))
                        break;
                    else {
                        seleniumUtil.click(By.xpath(SpacePage.STR_SP_LIST_SPACE+"/li["+i+"]/div"));
                        seleniumUtil.pause(1);
                        seleniumUtil.click(TransactionPage.TP_ICON_TRANSACTION);
                        seleniumUtil.pause(1);
                        break;
                    }
            }
        }
    }
}

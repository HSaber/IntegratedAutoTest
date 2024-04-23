package com.autotest.ui.pageshelper;

import com.autotest.ui.pages.HomePage;
import com.autotest.ui.pages.SpacePage;
import com.autotest.ui.utils.SeleniumUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

/**
 * ClassName:SpaceChangeHelper
 * Package:com.autotest.ui.pageshelper
 * Description:
 *
 * @Author huhuan
 * @Create 2024/3/15 9:46
 * @Version 1.0
 */
public class SpaceChangeHelper {
    // 提供本类中日志输出对象
    public static Logger logger = Logger.getLogger(SpaceChangeHelper.class);
    //切至个人空间
    public static void checkandchangeSpace(SeleniumUtil seleniumUtil, String type) {
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
                    seleniumUtil.click(HomePage.HP_BUTTON_TRANSACTION);
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
                        seleniumUtil.click(HomePage.HP_BUTTON_TRANSACTION);
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
                        seleniumUtil.click(HomePage.HP_BUTTON_TRANSACTION);
                        seleniumUtil.pause(1);
                        break;
                    }
            }
        }
    }
}

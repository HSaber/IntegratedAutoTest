package com.autotest.ui.business;

import com.autotest.ui.base.BasePrepare;
import com.autotest.ui.pages.SimpleOrderPerPage;
import com.autotest.ui.pageshelper.SimpleOrderPerPageHelper;
import com.autotest.ui.utils.Tools;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateSimpleOrderByPerson extends BasePrepare {
    static Logger logger = Logger.getLogger(CreateSimpleOrderByPerson.class.getName());

    private Tools tool = new Tools();
    private String subject = null;

    @Test(enabled  = true,priority = 1,parameters = {"pronum", "testurl", "realname"})
    public void createSimpleOrderByPerson(int pronum, String testurl, String realname) throws Exception {
        JavascriptExecutor js = null;
        //获取ul进行判断
        //新建简单
        //进入简单列表
        SimpleOrderPerPageHelper.waitSimpleOrderPageLoad(seleniumUtil,timeOut);
        seleniumUtil.click(SimpleOrderPerPage.SOP_LINK_NAV);
//        //校验列表字段
//
        seleniumUtil.waitForElementToLoad(timeOut = 20, SimpleOrderPerPage.SOP_BUTTON_SO);
        String[] fieldarr = {"#", "状态", "优先级", "用途", "建单时间", "编号", "交货城市", "截止时间", "主题", "子单统计", "操作"};
        for (int i = 1; i <= 11; i++) {
            String str = seleniumUtil.getText(By.xpath(
                    "//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div[3]/div/div[1]/div/div[2]/div[1]/div[1]/table/thead/tr/th[" + i + "]/div"));
            seleniumUtil.isTextCorrect(str, fieldarr[i - 1]);
        }

        seleniumUtil.click(SimpleOrderPerPage.SOP_BUTTON_SO);
        seleniumUtil.waitForElementToLoad(timeOut, By.id("form_item_missionNumber"));
        //校验详情字段
        String[] detailarr = {"13", "补充库存", tool.dateAdd(8), "", "低级", "", "生成子单", "删 除"};
        for (int i = 1; i <= 8; i++) {
            By by = null;
            String str;
            switch (i) {
                case 1:
                    by = By.xpath("//*[@id=\"form_item_missionNumber\"]");
                    break;
//                case 2:
//                    by = By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div/form/div/div[5]/div/div[2]/div/div/div/div/div/span[2]");
//                    str = seleniumUtil.findElementBy(by).getAttribute("innerHTML");
//                    //  Assert.assertEquals(str,detailarr[i-1]);
//                    break;
                case 3:
                    by = By.id("form_item_endTime");
                    str = seleniumUtil.findElementBy(by).getAttribute("title");
                    Assert.assertEquals(str, detailarr[i - 1]);
                    break;
                case 4:
                    by = By.id("form_item_deliveryAddress");
                    //       str=seleniumUtil.findElementBy(by).getText();
                    break;
                case 5:
                    by = By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div/form/div/div[4]/div/div[2]/div/div/div/div/div/span[2]");
                    str = seleniumUtil.findElementBy(by).getAttribute("innerHTML");
                    break;
                case 6:
                    by = By.id("form_item_subject");
                    str = seleniumUtil.findElementBy(by).getText();
                    Assert.assertEquals(str, detailarr[i - 1]);
                    break;
                case 7:
                    js = (JavascriptExecutor) seleniumUtil.driver;
                    String flag = js.executeScript("return document.querySelector(\""+SimpleOrderPerPage.BUTTON_CREATESUBORDER+"\").hasAttribute(\"disabled\");").toString();
                    Assert.assertEquals("true", flag);
                    str = seleniumUtil.findElementBy(SimpleOrderPerPage.SOP_BUTTON_CREATESUBORDER).getText();
                    Assert.assertEquals(str, detailarr[i - 1]);
                    break;
                case 8:
                    js = (JavascriptExecutor) seleniumUtil.driver;
                    flag = js.executeScript("return document.querySelector(\""+SimpleOrderPerPage.BUTTON_DELETE+"\").hasAttribute(\"disabled\");").toString();
                    Assert.assertEquals("true", flag);
                    str = seleniumUtil.findElementBy(SimpleOrderPerPage.SOP_BUTTON_DELETE).getText();
                    Assert.assertEquals(str, detailarr[i - 1]);
                    break;

            }
            if (i == 6) {
                subject = "请勿动" + tool.currentTime() + " " + realname + "新建简单 共" + (pronum - 1) + "个商品";
                seleniumUtil.findElementBy(by).sendKeys(subject);
            }
        }
        //选择交货城市
        seleniumUtil.findElementBy(By.cssSelector(".ant-cascader > div:nth-child(1) > span:nth-child(1)")).click();
        seleniumUtil.mouseMoveToElement(By.id("form_item_deliveryAddress"));

        seleniumUtil.click(By.cssSelector("li.ant-cascader-menu-item-expand:nth-child(1) > div:nth-child(1)"));
        seleniumUtil.waitForElementToLoad(timeOut, By.cssSelector("ul.ant-cascader-menu:nth-child(2) > li:nth-child(1)"));
        seleniumUtil.click(By.cssSelector("ul.ant-cascader-menu:nth-child(2) > li:nth-child(1)"));
        seleniumUtil.waitForElementToLoad(timeOut, By.cssSelector("ul.ant-cascader-menu:nth-child(3) > li:nth-child(1)"));
        seleniumUtil.click(By.cssSelector("ul.ant-cascader-menu:nth-child(3) > li:nth-child(1)"));

        //js解决交货城市
//        WebElement element = seleniumUtil.findElementBy(By.cssSelector(".ant-cascader > div:nth-child(1) > span:nth-child(2)"));
//        js.executeScript("arguments[0].setAttribute('title', '中国 / 北京 / 东城')",element);
//        js.executeScript("var ele=arguments[0]; ele.innerHTML = '中国 / 北京 / 东城';",element);

        System.out.println("=================================分割线==============================================");
        for (int i = 1; i <= pronum; i++) {
            seleniumUtil.click(SimpleOrderPerPage.SOP_BUTTION_ADDPRODUCT);
            seleniumUtil.waitForElementToLoad(timeOut, By.cssSelector("tr.vxe-body--row:nth-child(" + i + ") > td:nth-child(2) > div:nth-child(1) > input:nth-child(1)"));
            if (i == 1) {
                //第一个商品设置图片
                js = (JavascriptExecutor) seleniumUtil.driver;
                js.executeScript(" var d1= document.querySelector(\"tr.vxe-body--row:nth-child(1) > td:nth-child(9) > div:nth-child(1)\") ;" +
                        "d1.innerHTML=\"\";" +
                        "var im=document.createElement(\"img\");" +
                        "im.src=\"" + testurl + "/upload/files/2023-11-02/sy/90cb211c7180496db682f473449d72d0file.jpg\";" +
                        "im.style.width=\"60px\";" +
                        "im.style.margin=\"0px auto\";" +
                        "d1.appendChild(im);");

            }
            //简单商品信息输入
            for(int line=2;line<9;line++){
                String inputstr="";
                switch (line){
                    case 2:
                        inputstr="简单商品编号:" + i;
                        break;
                    case 3:
                        inputstr="简单商品名称:" + i;
                        break;
                    case 4:
                        inputstr=i+"个";
                        break;
                    case 5:
                        inputstr=100 * i + "";
                        break;
                    case 6:
                        inputstr="车型" + i;
                        break;
                    case 7:
                        inputstr="机型" + i;
                        break;
                    case 8:
                        inputstr="参数" + i;
                        break;
                }
                seleniumUtil.findElementBy(By.cssSelector("tr.vxe-body--row:nth-child(" + i + ") > td:nth-child("+line+") > div:nth-child(1) > input:nth-child(1)"))
                        .sendKeys(inputstr);
            }

            if (i == pronum) {
                //商品删除按钮名称校验
                String button = seleniumUtil.findElementBy(By.cssSelector("tr.vxe-body--row:nth-child(" + i + ") > td:nth-child(11) > div:nth-child(1) > button:nth-child(1)")).getText();
                Assert.assertEquals(button, "删除");
                seleniumUtil.findElementBy(By.cssSelector("tr.vxe-body--row:nth-child(" + i + ") > td:nth-child(11) > div:nth-child(1) > button:nth-child(1)")).click();
                seleniumUtil.waitForElementToLoad(timeOut, SimpleOrderPerPage.SOP_POPBUTTION_DELETE);
                seleniumUtil.findElementBy(SimpleOrderPerPage.SOP_POPBUTTION_DELETE).click();
            }
        }
        //点击保存 弹框校验
        seleniumUtil.click(SimpleOrderPerPage.SOP_BUTTON_SAVE);

        //三个操作按钮的状态及文本
        Thread.sleep(1000);

        js = (JavascriptExecutor) seleniumUtil.driver;
        String flag = js.executeScript("return document.querySelector(\"button.ant-btn-primary:nth-child(1)\").hasAttribute(\"disabled\");").toString();
        Assert.assertEquals("false", flag);

        js = (JavascriptExecutor) seleniumUtil.driver;
        flag = js.executeScript("return document.querySelector(\""+SimpleOrderPerPage.BUTTON_CREATESUBORDER+"\").hasAttribute(\"disabled\");").toString();
        Assert.assertEquals("false", flag);
        String buttonstr = seleniumUtil.findElementBy(SimpleOrderPerPage.SOP_BUTTON_CREATESUBORDER).getText();
        Assert.assertEquals(buttonstr, "生成子单");

        js = (JavascriptExecutor) seleniumUtil.driver;
        flag = js.executeScript("return document.querySelector(\""+SimpleOrderPerPage.BUTTON_DELETE+"\").hasAttribute(\"disabled\");").toString();
        Assert.assertEquals("false", flag);
        buttonstr = seleniumUtil.findElementBy(SimpleOrderPerPage.SOP_BUTTON_DELETE).getText();
        Assert.assertEquals(buttonstr, "删 除");


        //回到简单列表
//        seleniumUtil.click(By.xpath("/html/body/div[1]/section/section/div[3]/div[2]/div/div[2]/div/ul/li[1]/span/a"));
        seleniumUtil.click(By.xpath("//*[@id=\"nav_chat\"]/div[3]/div/ul/li[1]/span/a"));
        seleniumUtil.waitForElementToLoad(timeOut = 20, By.cssSelector(".row--current > td:nth-child(9)"));
        String subjectget = seleniumUtil.findElementBy(By.cssSelector(".row--current > td:nth-child(9) > div:nth-child(1) > span:nth-child(1)")).getText();
        Assert.assertEquals(subject, subjectget);
        //校验商品行数
        seleniumUtil.click(By.cssSelector(".row--current > td:nth-child(6) > div:nth-child(1) > a:nth-child(1)"));
        seleniumUtil.waitForElementToLoad(timeOut, By.cssSelector("tr.vxe-body--row:nth-child(1) > td:nth-child(2) > div:nth-child(1) > input:nth-child(1)"));
        js = (JavascriptExecutor) seleniumUtil.driver;
        String rows = js.executeScript("return document.getElementsByClassName(\"vxe-table--body\")[0].rows.length").toString();
        System.out.println("rows" + rows);
        Assert.assertEquals((pronum - 1) + "", rows);
        //校验商品操作
        String button = seleniumUtil.findElementBy(By.cssSelector("tr.vxe-body--row:nth-child(1) > td:nth-child(11) > div:nth-child(1) > button:nth-child(1)")).getText();
        Assert.assertEquals(button, "删除");
//<img style="width: 60px; margin: 0px auto;" src="http://116.62.110.78:90/upload/files/2023-05-22/29c6d0fcd75d46eda7d49ae05dd4fad3file.gif" alt="">


    }

    @Test(enabled = true,priority = 2)
    public void createSubsimpleOrderByPerson() throws Exception {
        int circle=10;
        int pageexpect=1;
        /**
         * 进入简单列表 跳过建单
         */
        seleniumUtil.click(SimpleOrderPerPage.SOP_LINK_NAV);
  //      seleniumUtil.waitForElementToLoad(timeOut,SimpleOrderPerPage.SOP_BUTTON_SO);
        ////*[@id="iitoo-layout-main"]/div[2]/div/div/div[1]/div[3]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[9]/div/span
        seleniumUtil.waitForElementToLoad(timeOut,SimpleOrderPerPage.SOP_TEXT_TITLE);
        while(!seleniumUtil.getText(SimpleOrderPerPage.SOP_TEXT_TITLE).contains("请勿")){
            seleniumUtil.pause(500);
            seleniumUtil.refresh();
            seleniumUtil.waitForElementToLoad(timeOut,SimpleOrderPerPage.SOP_TEXT_TITLE);
        }
        seleniumUtil.click(SimpleOrderPerPage.SOP_TEXT_ID);
        seleniumUtil.waitForElementToLoad(timeOut, By.id("form_item_missionNumber"));

        //详情点击生成子单按钮
        seleniumUtil.click(SimpleOrderPerPage.SOP_BUTTON_CREATESUBORDER);
        //选择供应商
        seleniumUtil.waitForElementToLoad(timeOut,SimpleOrderPerPage.SOPSUB_SELECTOR_SUPPLIER);
        seleniumUtil.click(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[2]/div/div[2]/div/div/div/div"));
        seleniumUtil.click(By.cssSelector("div.ant-select-item-option-active:nth-child(1) > div:nth-child(1)"));

        //点击日期控件
        seleniumUtil.click(By.className("ant-picker-input"));
        seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector(".ant-picker-content > tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(1) > div:nth-child(1)"));

        //优先级????????
//        seleniumUtil.click(By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div/form/div/div[6]/div/div[2]/div/div/div/div/div/span[2]"));
//        seleniumUtil.mouseMoveToElement(By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div/form/div/div[6]/div/div[2]/div/div/div/div/div/span[2]"));
//        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("/html/body/div[4]/div/div/div/div[2]/div/div/div/div[2]/div"));
//        seleniumUtil.click(By.xpath("/html/body/div[4]/div/div/div/div[2]/div/div/div/div[2]/div"));

        //点击添加
        seleniumUtil.click(SimpleOrderPerPage.SOPSUB_BUTTON_ADDPRODUCT);
        seleniumUtil.waitForElementToLoad(timeOut,SimpleOrderPerPage.SOPSUB_LAYER_CHOOSEPRODUCT);
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
        //
     //   String curstartorder=seleniumUtil.getText(By.cssSelector("tr.vxe-body--row:nth-child(1) > td:nth-child(2) > div:nth-child(1)"));
        //获取中间件第一条数据的序号
        By pageorder = By.cssSelector(".tid_"+col+" > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(2)");
        String curstartorder=seleniumUtil.getText(pageorder);
        System.out.println("curstartorder:"+curstartorder);
        int start=1;
        while(start<page){
            seleniumUtil.click(By.cssSelector(".vxe-pager--next-btn"));
            while((start*10+1)!=Integer.parseInt(curstartorder)){
                Thread.sleep(500);
                curstartorder=seleniumUtil.getText(pageorder);
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
        seleniumUtil.click(SimpleOrderPerPage.SOP_LINK_NAV);
        seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector(".row--current > td:nth-child(10)"));
        //点击子单统计数
        seleniumUtil.click(By.cssSelector(".row--current > td:nth-child(10) > div:nth-child(1) > span:nth-child(1)"));
        seleniumUtil.waitForElementToLoad(timeOut,SimpleOrderPerPage.SOPSUB_BUTTON_LIST);
        //点击发送按钮
        seleniumUtil.click(SimpleOrderPerPage.SOPSUB_BUTTON_LIST);
        String status=seleniumUtil.getText(SimpleOrderPerPage.SOPSUB_TEXT_STATE);
        circle=10;
        while (status.equals("草稿")&&circle>0){
            circle--;
            Thread.sleep(500);
            status=seleniumUtil.getText(SimpleOrderPerPage.SOPSUB_TEXT_STATE);
        }

    }



 /*   @Test(priority = 3)
    public void LoginQuit()throws Exception{
        seleniumUtil.mouseMoveToElement(By.cssSelector(".iitoo-header-user-dropdown__header"));
        seleniumUtil.waitForElementToLoad(timeOut+timeOut,By.className("ant-popover-inner-content"));
        seleniumUtil.click(By.cssSelector("li.ant-dropdown-menu-item:nth-child(5) > span:nth-child(1) > span:nth-child(1) > span:nth-child(2)"));
        //退登弹框
        seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector("button.ant-btn:nth-child(2)"));
        seleniumUtil.click(By.cssSelector("button.ant-btn:nth-child(2)"));
        seleniumUtil.waitForElementToLoad(timeOut,By.id("form_item_account"));
    }*/

}

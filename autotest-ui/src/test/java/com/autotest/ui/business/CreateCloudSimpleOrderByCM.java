package com.autotest.ui.business;

import com.autotest.ui.base.BasePrepare;
import com.autotest.ui.pageshelper.SimpleOrderCMPageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * ClassName:CreateCloudSimpleOrderByCM
 * Package:com.autotest.ui.business
 * Description:
 *
 * @Author huhuan
 * @Create 2023/11/30 17:38
 * @Version 1.0
 */
public class CreateCloudSimpleOrderByCM extends BasePrepare {
    static Logger logger = Logger.getLogger(CreateCloudSimpleOrderByCM.class.getName());

    @Test(enabled  = true,priority = 1,parameters = {"pronum"})
    public void createCloudSimpleOrderByCM(int pronum)throws Exception{
        //进入简单列表

        SimpleOrderCMPageHelper.waitCloudSimpleOrderPageLoad(seleniumUtil,timeOut);
//        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("//*[@id=\"nav_chat\"]/div[2]/div/ul/li[2]/span/a"));
        seleniumUtil.click(By.xpath("//*[@id=\"nav_chat\"]/div[2]/div/ul/li[2]/span/a"));
        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("(//div[@class=\"ant-tabs-nav-list\"])[2]/div[1]/div"));
/*

        //点击客户简单
        seleniumUtil.click(By.xpath("(//div[@class=\"ant-tabs-nav-list\"])[2]/div[2]/div"));
        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("(//div[@class=\"ant-tabs-content-holder\"])[2]/div/div[2]/div/div/div/div[2]/div/div[2]/table/tbody/tr/td[2]/div/div/div/div/div/span"));

        //轮训搜索自动化生成的简单数据
        ////*[@id="rc-tabs-2-panel-2"]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[10]/div
        int total= 0;
        int time=500;
        while(total==0){
            Thread.sleep(time);
            String  totalcol=seleniumUtil.getText(By.xpath("//*[@id=\"rc-tabs-2-panel-2\"]/div/div[2]/div/span[3]"));
            total= Integer.parseInt(StringUtils.getDigits(totalcol));
            if(time/100>timeOut)
                Assert.assertTrue(total>=1);
            logger.info("加载客户简单列表中");
        }
        logger.info("total:"+total);
        int line=1;
        for(;line<=(total>10?10:total);line++){
            String subject=seleniumUtil.getText(By.xpath("//*[@id=\"rc-tabs-2-panel-2\"]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr["+line+"]/td[10]/div/span"));
            if(subject.contains("请勿动")){
                //*[@id="rc-tabs-2-panel-2"]/div/div[1]/div/div[2]/div[2]/div/div[2]/table/tbody/tr[1]/td/div/span[1]
                seleniumUtil.waitForElementToLoad(timeOut,By.xpath("//*[@id=\"rc-tabs-2-panel-2\"]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr["+line+"]/td[14]/div/span[1]"));
                String sellerbuttonstyle=seleniumUtil.getAttributeText(By.xpath("//*[@id=\"rc-tabs-2-panel-2\"]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr["+line+"]/td[14]/div/span[1]"),"style");
                Assert.assertEquals(sellerbuttonstyle,"color: grey; cursor: pointer;");
                String cloudbuttonstyle= seleniumUtil.getAttributeText(By.xpath("//*[@id=\"rc-tabs-2-panel-2\"]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr["+line+"]/td[14]/div/span[2]"),"style");
                Assert.assertEquals(cloudbuttonstyle,"color: orange; cursor: pointer; margin-left: 10px;");
                String archivedbuttonstyle=seleniumUtil.getAttributeText(By.xpath("//*[@id=\"rc-tabs-2-panel-2\"]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr["+line+"]/td[14]/div/span[3]"),"style");
                Assert.assertEquals(archivedbuttonstyle,"color: red; cursor: pointer; margin-left: 10px;");
                break;
            }
        }

        //进入第一个简单详情
        seleniumUtil.click(By.xpath("//*[@id=\"rc-tabs-2-panel-2\"]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr["+line+"]/td[14]/div/span[2]"));
        seleniumUtil.waitForElementToLoad(timeOut,By.id("form_item_endTime"));

        //form_item_missionNumber
        String[] customerSimpOrderMainFields=new String[8];
        customerSimpOrderMainFields[0]="null";//seleniumUtil.getText(By.id("form_item_missionNumber"));
        customerSimpOrderMainFields[1]=seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[2]/div/div[2]/div/div/div/div/div/span[2]"));
        customerSimpOrderMainFields[2]=seleniumUtil.getText(By.id("form_item_createName"));
        customerSimpOrderMainFields[3]=seleniumUtil.getText(By.id("form_item_nickName"));
        customerSimpOrderMainFields[4]=seleniumUtil.getText(By.id("form_item_endTime"));
        customerSimpOrderMainFields[5]=seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[6]/div/div[2]/div/div/div/div/div/div/span[2]"));
        int timewait=0;
        while(!customerSimpOrderMainFields[5].contains("中国")){
            seleniumUtil.pause(timewait+=1);
            Assert.assertTrue(timeOut>timewait);
            logger.info("地区信息加载中");
            customerSimpOrderMainFields[5]=seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[6]/div/div[2]/div/div/div/div/div/div/span[2]"));
        }
        customerSimpOrderMainFields[6]=seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[7]/div/div[2]/div/div/div/div/div/span[2]"));
        customerSimpOrderMainFields[7]=seleniumUtil.getText(By.id("form_item_subject"));

        for (String str:customerSimpOrderMainFields) {
            logger.info("客户简单详情字段获取:"+str);
        }

        //判断当前按钮可点击状态
        Boolean enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.cssSelector("button.ant-btn-round:nth-child(1)")));
        Assert.assertTrue(enable);
        enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.cssSelector("button.ant-btn:nth-child(2)")));
        Assert.assertFalse(enable);
        enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.cssSelector("button.ant-btn:nth-child(3)")));
        Assert.assertFalse(enable);

        //点击添加商品
        seleniumUtil.click(By.cssSelector(".ant-btn-sm"));
        seleniumUtil.waitForElementToLoad(timeOut,By.className("ant-modal-title"));

        //判断商品条数
        String totalpro=seleniumUtil.getText(By.cssSelector(".vxe-pager--total"));
        seleniumUtil.isTextCorrect(StringUtils.getDigits(totalpro),pronum+"");
        seleniumUtil.isCheckboxSelected(By.cssSelector("tr.vxe-body--row:nth-child(1) > td:nth-child(1) > div:nth-child(1) > span:nth-child(1) > span:nth-child(1)"));
        //点击确定 跳转创建云简单页面
        seleniumUtil.click(By.cssSelector(".ant-modal-footer > button:nth-child(2)"));

        //获取不定表头id以便定位元素
        String xid=seleniumUtil.getAttributeText(By.xpath("//div[@class=\"simple-task-table-detail\"]/div[1]/div[2]/div[1]/div[1]"),"xid");
        logger.info(xid);
        //检查添加的商品条数与传参是否一致
        WebElement ele=seleniumUtil.findElementBy(By.cssSelector(".tid_"+xid+" > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody:nth-child(2)"));
        List<WebElement> trlist= ele.findElements(By.tagName("tr"));
        seleniumUtil.isTextCorrect(trlist.size()+"",pronum+"");
        //点击保存 校验按钮状态
        seleniumUtil.click(By.cssSelector("button.ant-btn-round:nth-child(1)"));
        //等待保存按钮置灰
        enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.cssSelector("button.ant-btn-round:nth-child(1)")));
        timewait=1;
        while(enable){
            seleniumUtil.pause(timewait+=1);
            Assert.assertTrue(timeOut>timewait);
            enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.cssSelector("button.ant-btn-round:nth-child(1)")));
        }
//        Assert.assertFalse(enable);
        enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.cssSelector("button.ant-btn:nth-child(2)")));
        Assert.assertTrue(enable);
        enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.cssSelector("button.ant-btn:nth-child(3)")));
        Assert.assertTrue(enable);
        seleniumUtil.isTextCorrect(seleniumUtil.getText(By.cssSelector("button.ant-btn:nth-child(3) > span:nth-child(1)")),"删 除");

        //校验数据与客户简单详情一致
        //form_item_missionNumber
        //       seleniumUtil.isTextCorrect(customerSimpOrderMainFields[1],seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[2]/div/div[2]/div/div/div/div/div/span[2]")));
//        customerSimpOrderMainFields[2]=seleniumUtil.getText(By.id("form_item_createName"));
//        customerSimpOrderMainFields[3]=seleniumUtil.getText(By.id("form_item_nickName"));
//        customerSimpOrderMainFields[4]=seleniumUtil.getText(By.id("form_item_endTime"));
        String address=seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[6]/div/div[2]/div/div/div/div/div/div/span[2]"));
        timewait=0;
        while(!customerSimpOrderMainFields[5].contains("中国")){
            seleniumUtil.pause(timewait+=1);
            Assert.assertTrue(timeOut>timewait);
            logger.info("地区信息加载中");
            address=seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[6]/div/div[2]/div/div/div/div/div/div/span[2]"));
        }
        seleniumUtil.isTextCorrect(address,customerSimpOrderMainFields[5]);
        seleniumUtil.isTextCorrect(customerSimpOrderMainFields[6],seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[7]/div/div[2]/div/div/div/div/div/span[2]")));
//      customerSimpOrderMainFields[7]=seleniumUtil.getText(By.id("form_item_subject"));

        //打开云简单列表检查数据与状态
//        SimpleOrderCMPageHelper.waitCloudSimpleOrderPageLoad(seleniumUtil,timeOut);
        seleniumUtil.click(By.xpath("//*[@id=\"nav_chat\"]/div[2]/div/ul/li[2]/span/a"));
        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div[2]/div/div[2]/div/div[1]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[2]"));

        //获取云简单列表条数
        String pagetotal=seleniumUtil.getText(By.cssSelector(".vxe-pager--total"));
        int ptotal=Integer.parseInt( StringUtils.getDigits(pagetotal));
        System.out.println("云简单列表一共："+ptotal+" 条");

        //检查第一条云简单是否为当前新建的数据


        for(int i=2;i<12;i++){
            String verifystr=seleniumUtil.getText(By.cssSelector(".row--current > td:nth-child("+i+")"));
            switch (i){
                case 2:
                    seleniumUtil.isTextCorrect("草稿",verifystr);
                    break;
                case 3:
                    seleniumUtil.isTextCorrect("低级",verifystr);
                    break;
                case 9:
                    seleniumUtil.isTextCorrect("东城",verifystr);
                    break;
                case 10:
                    seleniumUtil.isContains(verifystr,"请勿");
                    break;
                case 11:
                    String attr=seleniumUtil.getAttributeText(By.cssSelector(".row--current > td:nth-child("+i+") > div:nth-child(1) > div:nth-child(1) > span:nth-child(1)"),"style");
                    seleniumUtil.isTextCorrect(attr,"color: orange; cursor: pointer;");
                    attr=seleniumUtil.getAttributeText(By.cssSelector(".row--current > td:nth-child("+i+") > div:nth-child(1) > div:nth-child(1) > span:nth-child(2)"),"style");
                    seleniumUtil.isTextCorrect(attr,"color: red; cursor: pointer; margin-left: 10px;");
                    break;
            }
        }
        //删除云简单
        seleniumUtil.click(By.cssSelector(".row--current > td:nth-child(11) > div:nth-child(1) > div:nth-child(1) > span:nth-child(2)"));
        seleniumUtil.waitForElementToLoad(timeOut,By.cssSelector("button.ant-btn:nth-child(2)"));
        seleniumUtil.click(By.cssSelector("button.ant-btn:nth-child(2)"));
        //校验是否条数-1
        String delpagetotal=seleniumUtil.getText(By.cssSelector(".vxe-pager--total"));
        int delptotal = Integer.parseInt(StringUtils.getDigits(delpagetotal));
        int pausetimes=0;
        while(delptotal!=(ptotal-1)){
            seleniumUtil.pause(1);
            delpagetotal=seleniumUtil.getText(By.cssSelector(".vxe-pager--total"));
            System.out.println("删除后条数"+delpagetotal+"\t 超时为"+timeOut);
            delptotal = Integer.parseInt(StringUtils.getDigits(delpagetotal));
            Assert.assertTrue(pausetimes<timeOut);

            pausetimes++;
        }

        //再次创建云简单
        String orderID=CreateCloudSimpleOrder(pronum);


        //上架云简单
        //打开云简单列表
//        SimpleOrderCMPageHelper.waitCloudSimpleOrderPageLoad(seleniumUtil,timeOut);
        seleniumUtil.click(By.xpath("//*[@id=\"nav_chat\"]/div[2]/div/ul/li[2]/span/a"));
        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div[2]/div/div[2]/div/div[1]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[2]"));
        //检查第一个云简单是否为对应的客户简单发起
        seleniumUtil.click(By.xpath("(//div[@class=\"ant-tabs-nav-list\"])[3]/div[2]/div"));
        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("(//div[@class=\"ant-tabs-content-holder\"])[3]/div/div[2]/div/div[2]/div/div[2]/table/tbody/tr/td[5]/div/a"));
        Assert.assertEquals(orderID,seleniumUtil.getText(By.xpath("(//div[@class=\"ant-tabs-content-holder\"])[3]/div/div[2]/div/div[2]/div/div[2]/table/tbody/tr/td[5]/div/a")));
*/

        //点击上架
        String buttonxpath="";
        String cssselector="table.vxe-table--body:nth-child(2) > tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(1) > div:nth-child(1) > div:nth-child(1)";
        seleniumUtil.click(By.cssSelector(cssselector+" > span:nth-child(1)"));

        //等待下架归档按钮
        Boolean enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.xpath("(//div[@class=\"ant-tabs-content-holder\"])[2]/div/div/div/div/div/div[2]/div[2]/div/div/table/tbody/tr/td[1]/div/div/span[2]")));
        int timewait=1;
        while(!enable){
            seleniumUtil.pause(timewait+=1);
            Assert.assertTrue(timeOut>timewait);
            enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.xpath("(//div[@class=\"ant-tabs-content-holder\"])[2]/div/div/div/div/div/div[2]/div[2]/div/div/table/tbody/tr/td[1]/div/div/span[2]")));
        }
//        Assert.assertFalse(enable);
        String downbutton=seleniumUtil.getText(By.xpath("(//div[@class=\"ant-tabs-content-holder\"])[2]/div/div/div/div/div/div[2]/div[2]/div/div/table/tbody/tr/td[1]/div/div/span[1]"));
        seleniumUtil.isTextCorrect(downbutton,"下架");
        String downbuttonstyle=seleniumUtil.getAttributeText(By.xpath("(//div[@class=\"ant-tabs-content-holder\"])[2]/div/div/div/div/div/div[2]/div[2]/div/div/table/tbody/tr/td[1]/div/div/span[1]"),"style");
        Assert.assertEquals(downbuttonstyle,"color: blue; cursor: pointer;");
        String filebuttonstyle=seleniumUtil.getAttributeText(By.xpath("(//div[@class=\"ant-tabs-content-holder\"])[2]/div/div/div/div/div/div[2]/div[2]/div/div/table/tbody/tr/td[1]/div/div/span[2]"),"style");
        seleniumUtil.isTextCorrect(filebuttonstyle,"color: grey; cursor: pointer; margin-left: 10px;");
        String cloudstatus = seleniumUtil.getText(By.xpath("(//div[@class=\"ant-tabs-content-holder\"])[2]/div/div/div/div/div/div[2]/div/div[2]/table/tbody/tr/td[2]/div/div/div/div/div/span"));
        seleniumUtil.isTextCorrect(cloudstatus,"已发送");
    }

    public String CreateCloudSimpleOrder (int pronum) throws Exception{
        //点击客户简单
        seleniumUtil.click(By.xpath("(//div[@class=\"ant-tabs-nav-list\"])[2]/div[2]/div"));
        seleniumUtil.waitForElementToLoad(timeOut,By.xpath("(//div[@class=\"ant-tabs-content-holder\"])[2]/div/div[2]/div/div/div/div[2]/div/div[2]/table/tbody/tr/td[2]/div/div/div/div/div/span"));

        //轮训搜索自动化生成的简单数据
        ////*[@id="rc-tabs-2-panel-2"]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[10]/div
        int total= 0;
        int time=500;
        while(total==0){
            Thread.sleep(time);
            String  totalcol=seleniumUtil.getText(By.xpath("(//div[@class=\"ant-tabs-content-holder\"])[2]/div/div[2]/div/div[2]/div/span[3]"));
            total= Integer.parseInt(StringUtils.getDigits(totalcol));
            if(time/100>timeOut)
                Assert.assertTrue(total>=1);
            logger.info("加载客户简单列表中");
        }
        logger.info("total:"+total);
        int line=1;
        String linexpath="(//div[@class=\"ant-tabs-content-holder\"])[2]/div/div[2]/div/div/div/div[2]/div/div[2]/table/tbody";
        //linepath=//*[@id="rc-tabs-2-panel-2"]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody
        for(;line<=(total>10?10:total);line++){
            String subject=seleniumUtil.getText(By.xpath(linexpath+"/tr["+line+"]/td[10]/div/span"));
            if(subject.contains("请勿动")){
                //*[@id="rc-tabs-2-panel-2"]/div/div[1]/div/div[2]/div[2]/div/div[2]/table/tbody/tr[1]/td/div/span[1]
                seleniumUtil.waitForElementToLoad(timeOut,By.xpath(linexpath+"/tr["+line+"]/td[14]/div/span[1]"));
                String sellerbuttonstyle=seleniumUtil.getAttributeText(By.xpath(linexpath+"/tr["+line+"]/td[14]/div/span[1]"),"style");
                Assert.assertEquals(sellerbuttonstyle,"color: grey; cursor: pointer;");
                String cloudbuttonstyle= seleniumUtil.getAttributeText(By.xpath(linexpath+"/tr["+line+"]/td[14]/div/span[2]"),"style");
                Assert.assertEquals(cloudbuttonstyle,"color: orange; cursor: pointer; margin-left: 10px;");
                String archivedbuttonstyle=seleniumUtil.getAttributeText(By.xpath(linexpath+"/tr["+line+"]/td[14]/div/span[3]"),"style");
                Assert.assertEquals(archivedbuttonstyle,"color: red; cursor: pointer; margin-left: 10px;");
                break;
            }
        }

        //获取列表第一个客户简单的单号
        String orderID=seleniumUtil.getText(By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div[2]/div/div[2]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[5]/div/a"));
        //进入第一个简单详情
        seleniumUtil.click(By.xpath(linexpath+"/tr["+line+"]/td[14]/div/span[2]"));
        seleniumUtil.waitForElementToLoad(timeOut,By.id("form_item_endTime"));

        //form_item_missionNumber
        String[] customerSimpOrderMainFields=new String[8];
        customerSimpOrderMainFields[0]="null";//seleniumUtil.getText(By.id("form_item_missionNumber"));
        customerSimpOrderMainFields[1]=seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[2]/div/div[2]/div/div/div/div/div/span[2]"));
        customerSimpOrderMainFields[2]=seleniumUtil.getText(By.id("form_item_createName"));
        customerSimpOrderMainFields[3]=seleniumUtil.getText(By.id("form_item_nickName"));
        customerSimpOrderMainFields[4]=seleniumUtil.getText(By.id("form_item_endTime"));
        customerSimpOrderMainFields[5]=seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[6]/div/div[2]/div/div/div/div/div/div/span[2]"));
        int timewait=0;
        while(!customerSimpOrderMainFields[5].contains("中国")){
            seleniumUtil.pause(timewait+=1);
            Assert.assertTrue(timeOut>timewait);
            logger.info("地区信息加载中");
            customerSimpOrderMainFields[5]=seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[6]/div/div[2]/div/div/div/div/div/div/span[2]"));
        }
        customerSimpOrderMainFields[6]=seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[7]/div/div[2]/div/div/div/div/div/span[2]"));
        customerSimpOrderMainFields[7]=seleniumUtil.getText(By.id("form_item_subject"));

        for (String str:customerSimpOrderMainFields) {
            logger.info("客户简单详情字段获取:"+str);
        }

        //判断当前按钮可点击状态
        Boolean enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.cssSelector("button.ant-btn-round:nth-child(1)")));
        Assert.assertTrue(enable);
        enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.cssSelector("button.ant-btn:nth-child(2)")));
        Assert.assertFalse(enable);
        enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.cssSelector("button.ant-btn:nth-child(3)")));
        Assert.assertFalse(enable);

        //点击添加商品
        seleniumUtil.click(By.cssSelector(".ant-btn-sm"));
        seleniumUtil.waitForElementToLoad(timeOut,By.className("ant-modal-title"));

        //判断商品条数
        String totalpro=seleniumUtil.getText(By.cssSelector(".vxe-pager--total"));
        seleniumUtil.isTextCorrect(StringUtils.getDigits(totalpro),pronum+"");
        seleniumUtil.isCheckboxSelected(By.cssSelector("tr.vxe-body--row:nth-child(1) > td:nth-child(1) > div:nth-child(1) > span:nth-child(1) > span:nth-child(1)"));
        //点击确定 跳转创建云简单页面
        seleniumUtil.click(By.cssSelector(".ant-modal-footer > button:nth-child(2)"));

        //获取不定表头id以便定位元素
        String xid=seleniumUtil.getAttributeText(By.xpath("//div[@class=\"simple-task-table-detail\"]/div[1]/div[2]/div[1]/div[1]"),"xid");
        logger.info(xid);
        //检查添加的商品条数与传参是否一致
        WebElement ele=seleniumUtil.findElementBy(By.cssSelector(".tid_"+xid+" > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody:nth-child(2)"));
        List<WebElement> trlist= ele.findElements(By.tagName("tr"));
        seleniumUtil.isTextCorrect(trlist.size()+"",pronum+"");
        //点击保存 校验按钮状态
        seleniumUtil.click(By.cssSelector("button.ant-btn-round:nth-child(1)"));
        //等待保存按钮置灰
        enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.cssSelector("button.ant-btn-round:nth-child(1)")));
        timewait=1;
        while(enable){
            seleniumUtil.pause(timewait+=1);
            Assert.assertTrue(timeOut>timewait);
            enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.cssSelector("button.ant-btn-round:nth-child(1)")));
        }
//        Assert.assertFalse(enable);
        enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.cssSelector("button.ant-btn:nth-child(2)")));
        Assert.assertTrue(enable);
        enable=seleniumUtil.isEnabled(seleniumUtil.findElementBy(By.cssSelector("button.ant-btn:nth-child(3)")));
        Assert.assertTrue(enable);
        seleniumUtil.isTextCorrect(seleniumUtil.getText(By.cssSelector("button.ant-btn:nth-child(3) > span:nth-child(1)")),"删 除");

        //校验数据与客户简单详情一致
        //form_item_missionNumber
        //       seleniumUtil.isTextCorrect(customerSimpOrderMainFields[1],seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[2]/div/div[2]/div/div/div/div/div/span[2]")));
//        customerSimpOrderMainFields[2]=seleniumUtil.getText(By.id("form_item_createName"));
//        customerSimpOrderMainFields[3]=seleniumUtil.getText(By.id("form_item_nickName"));
//        customerSimpOrderMainFields[4]=seleniumUtil.getText(By.id("form_item_endTime"));
        String address=seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[6]/div/div[2]/div/div/div/div/div/div/span[2]"));
        timewait=0;
        while(!customerSimpOrderMainFields[5].contains("中国")){
            seleniumUtil.pause(timewait+=1);
            Assert.assertTrue(timeOut>timewait);
            logger.info("地区信息加载中");
            address=seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[6]/div/div[2]/div/div/div/div/div/div/span[2]"));
        }
        seleniumUtil.isTextCorrect(address,customerSimpOrderMainFields[5]);
        seleniumUtil.isTextCorrect(customerSimpOrderMainFields[6],seleniumUtil.getText(By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[7]/div/div[2]/div/div/div/div/div/span[2]")));
//      customerSimpOrderMainFields[7]=seleniumUtil.getText(By.id("form_item_subject"));
        return orderID;

    }
}

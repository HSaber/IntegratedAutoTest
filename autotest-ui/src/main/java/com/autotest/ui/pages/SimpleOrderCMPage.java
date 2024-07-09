package com.autotest.ui.pages;

import org.openqa.selenium.By;

public class SimpleOrderCMPage {
    /**左侧栏目打开简单页面**/
    public static final By SOC_LINK_NAV=By.xpath("//*[@id=\"nav_chat\"]/div[2]/div/ul/li[2]/span/a");
    /**tab 我的云简单**/
    public static final By SOC_TAB_CLOUDORDER=By.id("rc-tabs-2-tab-1");
    /**tab 客户简单列表**/
    public static final By SOC_TAB_CMSIMORDER = By.xpath("//div[text()=\"客户简单\"]");
    public static final By SOC_TEXT_CMSIMHEADER=By.xpath("//*[@id=\"rc-tabs-2-panel-2\"]/div/div[2]/div/span[3]");

    public static final By SOC_TEXT_LISTTOTAL = By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div[3]/div/div[2]/div/span[3]");
    public static final By SOC_TEXT_LIST = By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div[3]/div/div[1]/div/div[2]/div[1]/div[2]/table");

    public static final String TABLE_SOCLIST = "//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div[3]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody";

    //客户简单 三个按钮
    public static final String BUTTON_SOCPUSH = "//span[text()=\"推送卖单\"][1]";

    public static final String BUTTION_SOCCREATE = "//span[text()=\"新建简单\"][1]";

    public static final String BUTTION_SOCATCHIVE = "//span[text()=\"归档\"][1]";

    public static final By SOC_TEXT_CUSTOMER = By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[2]/div/div[2]/div/div/div/div/div/span[2]");
    public static final By SOC_TEXT_ADDRESS = By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[4]/div/div[2]/div/div/div/div/div/div/span[2]");
    public static final By SOC_TEXT_PRIORTY = By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div/form/div/div[5]/div/div[2]/div/div/div/div/div/span[2]");

    //客户简单详情 按钮
    public static final By SOCDETAIL_BUTTON_SAVE = By.xpath("//span[text()=\"保 存\"]");
    public static final By SOCDETAIL_BUTTON_CLOUDSO = By.xpath("//span[text()=\"新建我的云简单\"]");

    public static final By SOCDETAIL_BUTTON_ARCHIVE= By.cssSelector("button.ant-btn:nth-child(3)");

    //云简单
    public static final  By SOCLOUD_BUTTON_ADD = By.xpath("//span[text()=\"添 加\"]");
    public static final By SOCLOUD_TABLE_LIST = By.className("ant-modal-content");

    public static final  By SOCLOUD_MIDDLE_CONFIRM = By.xpath("//span[text()=\"确 定\"]");

    public static final By SOCLOUDDETAIL_TEXT_ADDRESS = By.cssSelector(".ant-cascader > div:nth-child(1) > span:nth-child(2)");
    public static final By SOCLOUDDETAIL_TEXT_PRIORITY = By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div/form/div/div[5]/div/div[2]/div/div/div/div/div/span[2]");

    public static final By SOCLOUDDETAIL_BUTTON_SAVE = By.cssSelector("button.ant-btn-round:nth-child(1)");
    public static final By SOCLOUDDETAIL_BUTTON_ONOFF= By.cssSelector("button.ant-btn-round:nth-child(2)");
    public static final By SOCLOUDDETAIL_BUTTON_DELETE = By.cssSelector("button.ant-btn-round:nth-child(3)");


    public static final By SOCLOUD_LINK_NAV = By.xpath("//*[@id=\"nav_chat\"]/div[2]/div/ul/li[2]/span/a");

    public static final By SOCLOUD_LIST_FIRST = By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div[3]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]");
    public static final By SOCLOUD_TEXT_ID = By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div[3]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[5]/div/a");
    public static final By SOCLOUD_PAGENUMBER = By.cssSelector(".vxe-pager--total");
    public static final By SOCLOUDLIST_BUTTON_DELETE = By.cssSelector(".row--current > td:nth-child(10) > div:nth-child(1) > div:nth-child(1) > span:nth-child(2)");

    public static final By SOCLOUDLIST_BUTTON_ON = By.xpath("//span[text()=\"上架\"][1]");
    public static final By SOCLOUDLIST_BUTTON_OFF = By.xpath("//span[text()=\"下架\"][1]");
    public static final By SOCLOUDLIST_BUTTON_ARCHIVE = By.xpath("//span[text()=\"归档\"][1]");
    public static final By SOCLOUDLIST_TEXT_STATUS = By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div[3]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[2]/div/div/div/div/div/span");
    //*[@class="list-page"]/div/div[3]/div/div/div/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/span[1]
    //*[@id="iitoo-layout-main"]/div[2]/div/div/div/div[3]/div/div/div/div[2]/div[2]/div/div[2]/table/tbody/tr[1]/td/div/span[1]

    /**
     * 产品经理
     */

    public static final By SOCLOUDLISTPM_TEXT_TITLE = By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div[3]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[8]/div/span");
    public static final By SOCLOUDLISTPM_TEXT_ID = By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div[3]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[4]/div/a");

    public static final By SOCLOUDDETAILPM_BUTTON_TAKEORDER = By.xpath("//button[text()=\"接单\"][1]");
    public static final String BUTTON_TAKEORDER = "//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[2]/div/div[2]/div[1]/div[2]/table/tbody";

    //勾选框由于前一个跳转框占据了 两个checkbox 第二个弹框要从3序号开始

    // (//input[@class='ant-checkbox-input'])[3]

    public static final By SOCLOUDDETAILPM_INPUT_MERGE = By.cssSelector("body > div:nth-child(6) > div > div.ant-modal-wrap > div > div.ant-modal-content > div.ant-modal-footer > label:nth-child(1) > span > input");
    public static final By SOCLOUDDETAILPM_BUTTION_MERGE = By.xpath("//span[text()=\"立即合并\"]");

    public static final By SOCLOUDDETAILPM_INPUT_NOTTAKEN = By.cssSelector("body > div:nth-child(7) > div > div.ant-modal-wrap > div > div.ant-modal-content > div.ant-modal-footer > label:nth-child(3) > span > input");
    public static final By SOCLOUDDETAILPM_BUTTION_NOTTAKEN = By.xpath("//span[text()=\"暂不跳转\"]");

}

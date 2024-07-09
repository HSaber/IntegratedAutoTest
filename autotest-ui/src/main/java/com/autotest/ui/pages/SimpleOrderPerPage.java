package com.autotest.ui.pages;

import org.openqa.selenium.By;

public class SimpleOrderPerPage {
    /**左侧栏目打开简单页面**/
    public static final By SOP_LINK_NAV = By.xpath("//*[@id=\"nav_chat\"]/div[3]/div/ul/li[1]/span/a");

    public static final By SOP_BUTTON_SO = By.cssSelector("#iitoo-layout-main > div.iitoo-layout-content.full > div > div > div.tp > div.btn-box > div.btns > button");
    public static final By SOP_BUTTION_ADDPRODUCT = By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[2]/div[1]/div[2]/button");

    public static final By SOP_BUTTON_SAVE = By.cssSelector("button.ant-btn-round:nth-child(1)");
    public static final By SOP_BUTTON_CREATESUBORDER = By.cssSelector("button.ant-btn-primary:nth-child(2)");

    public static final String BUTTON_CREATESUBORDER = "button.ant-btn-primary:nth-child(2)";

    public static final String BUTTON_DELETE = "button.ant-btn-primary:nth-child(2)";

    public static final By SOP_BUTTON_DELETE = By.cssSelector("button.ant-btn-primary:nth-child(3)");
    public static final By SOP_TEXT_TITLE = By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div[3]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[9]/div/span");

    public static final By SOP_TEXT_ID = By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div[3]/div/div[1]/div/div[2]/div[1]/div[2]/table/tbody/tr[1]/td[6]/div/a");
    //简单商品弹框删除
    public static final By SOP_POPBUTTION_DELETE = By.cssSelector(".ant-modal-confirm-btns > button:nth-child(2)");
    /**
     * 子单页面元素
     */
    public static final By SOPSUB_SELECTOR_SUPPLIER = By.cssSelector("div.ant-col-6:nth-child(2) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1)");

    public static final By SOPSUB_BUTTON_ADDPRODUCT = By.cssSelector("#iitoo-layout-main > div.iitoo-layout-content.full > div > div > div.task-detail-table > div > div.pd10.add > button");

    public static final By SOPSUB_LAYER_CHOOSEPRODUCT = By.xpath("//*[@class=\"table-top\"]/div/div[2]/div[1]/div[1]");

    /**
     * 子单列表
     */

    public static final By SOPSUB_BUTTON_LIST = By.xpath("//*[@id=\"iitoo-layout-main\"]/div[2]/div/div/div[1]/div[2]/div[2]/div[1]/div[2]/table/tbody/tr/td[12]/div/button");
    public static final By SOPSUB_TEXT_STATE = By.xpath("/html/body/div[1]/section/section/section/div[2]/div/div/div[1]/div[2]/div[2]/div[1]/div[2]/table/tbody/tr/td[2]/div/div/div/div/div/span");
}

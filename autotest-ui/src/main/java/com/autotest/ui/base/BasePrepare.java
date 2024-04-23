package com.autotest.ui.base;
/**
 * @Description 测试开始 和 测试结束 的操作
 *
 * */

import com.autotest.ui.pageshelper.HomePageHelper;
import com.autotest.ui.pageshelper.LoginPageHelper;
import com.autotest.ui.pageshelper.SpaceChangeHelper;
import com.autotest.ui.utils.LogConfiguration;
import com.autotest.ui.utils.SeleniumUtil;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BasePrepare {
    //输出本页面日志 初始化
    static Logger logger = Logger.getLogger(BasePrepare.class.getName());
    protected SeleniumUtil seleniumUtil = null;
    // 添加成员变量来获取beforeClass传入的context参数
    protected ITestContext testContext = null;
    protected String webUrl="";
    protected int timeOut = 0;
    protected String account;
    protected String password;
    protected String realname;
    protected String role;



    @BeforeSuite
    /**启动浏览器并打开测试页面*/
    public void startTest(ITestContext context) {
        LogConfiguration.initLog(this.getClass().getSimpleName());
        seleniumUtil = new SeleniumUtil();
        // 这里得到了context值
        this.testContext = context;
        //从testng.xml文件中获取浏览器的属性值
        String browserName = context.getCurrentXmlTest().getParameter("browserName");
        timeOut = Integer.valueOf(context.getCurrentXmlTest().getParameter("timeOut"));
        webUrl = context.getCurrentXmlTest().getParameter("testurl");
        account = context.getCurrentXmlTest().getParameter("account");
        password = context.getCurrentXmlTest().getParameter("password");
        realname = context.getCurrentXmlTest().getParameter("realname");
        role = context.getCurrentXmlTest().getParameter("role");

        try {
            //启动浏览器launchBrowser方法可以自己看看，主要是打开浏览器，输入测试地址，并最大化窗口
            seleniumUtil.launchBrowser(browserName, context,webUrl,timeOut);
            //等待登录页面加载
            LoginPageHelper.waitLoginPageLoad(seleniumUtil, timeOut);
            // 输入登录信息
            LoginPageHelper.typeLoginInfo(seleniumUtil,account, password);
            //等待首页元素显示出来
            HomePageHelper.waitHomePageLoad(seleniumUtil, timeOut);
            //切至个人空间
            //mini-sider
            SpaceChangeHelper.checkandchangeSpace(seleniumUtil,role);

       //     seleniumUtil.click(By.xpath("//*[@id=\"app\"]/section/section/div[2]/div[2]/li[3]/div/img"));
            seleniumUtil.pause(1);
            //检查用户名是不是期望的"jojo" 2024-01-11样式变更
     //       HomePageHelper.checkUserName(seleniumUtil, timeOut, realname);
        } catch (Exception e) {
            logger.error("浏览器不能正常工作，请检查是不是被手动关闭或者其他原因",e);
        }
        //设置一个testng上下文属性，将driver存起来，之后可以使用context随时取到，主要是提供arrow 获取driver对象使用的，因为arrow截图方法需要一个driver对象
        testContext.setAttribute("SELENIUM_DRIVER", seleniumUtil.driver);
    }

    @AfterSuite
    /**结束测试关闭浏览器*/
    public void endTest() {
        if (seleniumUtil.driver != null) {
            //退出浏览器
            seleniumUtil.quit();
        } else {
            logger.error("浏览器driver没有获得对象,退出操作失败");
            Assert.fail("浏览器driver没有获得对象,退出操作失败");
        }
    }
}
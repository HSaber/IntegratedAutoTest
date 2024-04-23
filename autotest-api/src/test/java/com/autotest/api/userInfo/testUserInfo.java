package com.autotest.api.userInfo;

import com.autotest.api.asserts.ResponseChecker;
import com.autotest.api.base.TestBase;
import com.autotest.api.util.MockVariables;
import com.autotest.api.util.OperateFile;
import com.autotest.api.util.TestDataProvider;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class testUserInfo extends TestBase  {
    private static Logger logger = Logger.getLogger(testUserInfo.class);

    private Map map = null;
    private MockVariables basicVaribles = null;
    private OperateFile deal = null;
    @BeforeTest
    public void setUp() throws Exception {
        map = new HashMap();
        basicVaribles = new MockVariables();

        map.putAll(basicVaribles.basicVariables());
        deal = new OperateFile();
    }

    @AfterTest
    public void tearDown() throws Exception {

 /*       try{
            System.out.println("kill chromedriver.exe");
            Runtime.getRuntime().exec("taskkill /f /im chromedriver.exe");
        }catch(Exception e){
            System.out.println("Error!");
        }*/

    }
    @DataProvider(name="testUserInfo")
    public static Object[][] testUserInfo() throws Exception
    {
        String filename = TestDataProvider.class.getResource("/testData/testUserInfo/userinfo.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @Test(dataProvider = "testUserInfo")
    public void testUserInfo(String caseNum, String testCase,String apiType, String requestURL, String requestMethod
            , String testDataType, String testURLData, String testBodyData, String expectData
            , String variableAccess, String variableStore, String status) throws Exception {

        String response = "";
        String[] dealData = {"", ""};
        dealData = deal.dealExcelVatiables(map, caseNum, testCase,apiType, requestURL, requestMethod, testDataType
                , testURLData, testBodyData, expectData, variableAccess, variableStore, status);
        response = dealData[0];
        expectData = dealData[1];
        // System.out.println(expectData);
        ResponseChecker check = ResponseChecker.getInstance();
        boolean flag = check.dataCheck(expectData,response);
        Assert.assertTrue(flag);
    }
}

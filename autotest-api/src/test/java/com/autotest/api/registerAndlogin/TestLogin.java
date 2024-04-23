package com.autotest.api.registerAndlogin;

import com.autotest.api.asserts.ResponseChecker;
import com.autotest.api.base.TestBase;
import com.autotest.api.util.MockVariables;
import com.autotest.api.util.OperateFile;
import com.autotest.api.util.TestDataProvider;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

public class TestLogin  {
    private static Logger logger = Logger.getLogger(TestLogin.class);

    private Map map = null;
    private MockVariables basicVaribles = null;
    private OperateFile deal = null;

    @DataProvider(name="testLogin")
    public static Object[][] testLogin() throws Exception
    {
        String filename = TestDataProvider.class.getResource("/testData/testLogin/login.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @Test(dataProvider = "testLogin")
    public void testLogin(String caseNum, String testCase,String apiType, String requestURL, String requestMethod
            , String testDataType, String testURLData, String testBodyData, String expectData
            , String variableAccess, String variableStore, String status) throws Exception {

        String response = "";
        String[] dealData = {"", ""};
        dealData = deal.dealExcelVatiables(map, caseNum, testCase,apiType, requestURL, requestMethod, testDataType
                , testURLData, testBodyData, expectData, variableAccess, variableStore, status);
        response = dealData[0];
        expectData = dealData[1];
        // System.out.println(expectData);
        switch (caseNum){
            case "19":
                Thread.sleep(5000);
                break;
            default:
                if(!expectData.equals("")){
                    ResponseChecker check = ResponseChecker.getInstance();
                    boolean flag = check.dataCheck(expectData,response);
                    Assert.assertTrue(flag);
                }
        }
    }
}

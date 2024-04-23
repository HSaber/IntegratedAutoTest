package com.autotest.common.util;

import com.autotest.api.base.TestNgConfig;
import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;

public class TestDataProvider {
    private static Logger logger=Logger.getLogger(TestDataProvider.class);

    public static String environment() throws Exception{
        TestNgConfig config=TestNgConfig.getInstance();
        String url=config.getOpenAPIUrl();
        String environment="";
        if(url.contains("dev")){
            environment="/dev";
        }else if(url.contains("staging")){
            environment="/staging";
        }else if(url.contains("https://api.jingsocial.com")){
            environment="/app";
        }

        return environment;
    }

    @DataProvider(name="TestPermission")
    public static Object[][] TestPermission() throws Exception {
        String filename = TestDataProvider.class.getResource("/testData/testPermission/testPermission.xlsx").getFile();
        OperateFile operateFile = new OperateFile();
        Object[][] data = operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="createPost")
    public static Object[][] createPost() throws Exception
    {
        String enviroment=environment();
        String filename =TestDataProvider.class.getResource("/testPost"+enviroment+" /createPost.csv").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getCSVData(filename);
        return data;//获取CSV文件的测试数据
    }

    @DataProvider(name="getUersByUnionids")
    public static Object[][] getUersByUnionids() throws Exception
    {
        String enviroment=environment();
        logger.info(enviroment);
        String filename =TestDataProvider.class.getResource("/testFollower"+enviroment+"/getUsersByUnionids.csv").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getCSVData(filename);
        return data;//获取CSV文件的测试数据
    }

    @DataProvider(name="getUsersbyOpenids")
    public static Object[][] getUsersbyOpenids() throws Exception
    {
        String enviroment=environment();
        logger.info(enviroment);
        String filename =TestDataProvider.class.getResource("/testFollower"+enviroment+"/getUsersbyOpenids.csv").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getCSVData(filename);
        return data;//获取CSV文件的测试数据
    }

    @DataProvider(name="getBatchSegmentMessage")
    public static Object[][] getBatchSegmentMessage() throws Exception
    {
        String enviroment=environment();
        logger.info(enviroment);
        String filename =TestDataProvider.class.getResource("/testSegment"+enviroment+"/getBatchSegmentMessage.csv").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getCSVData(filename);
        return data;//获取CSV文件的测试数据
    }

    @DataProvider(name="getBatchSegmentMax")
    public static Object[][] getBatchSegmentMax() throws Exception
    {
        String enviroment=environment();
        logger.info(enviroment);
        String filename =TestDataProvider.class.getResource("/testSegment"+enviroment+"/getBatchSegmentMax.csv").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getCSVData(filename);
        return data;//获取CSV文件的测试数据
    }


    @DataProvider(name="core2FollowerRBAC")
    public static Object[][] core2RBAC() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testRBAC/core2Follower.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }
    @DataProvider(name="core2RBACContent")
    public static Object[][] core2RBACContent() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testRBAC/core2Content.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="testCustomfields")
    public static Object[][] testCustomfields() throws Exception
    {
//        System.out.println("filename:" + TestDataProvider.class.getResource("").getFile());
        String filename =TestDataProvider.class.getResource("/testData/testCustomfield/customfield.xlsx").getFile();

        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="core1RBAC")
    public static Object[][] core1RBAC() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testRBAC/core1.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="CLCRBAC")
    public static Object[][] CLCRBAC() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testRBAC/CLC.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="Core3RBAC")
    public static Object[][] Core3RBAC() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testRBAC/Core3.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="testTagCategory")
    public static Object[][] TagCategory() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testTagandCategory/tagcategory.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }
    @DataProvider(name="testOrganization")
    public static Object[][] testOrganization() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testOrganization/organization.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }


    @DataProvider(name="testMiniProgram")
    public static Object[][] testMiniProgram() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testMiniProgram/MiniProgram.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }
    @DataProvider(name="testTextPreview")
    public static Object[][] testTextPreview() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testPost/TextPreview/TextPreview.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="testPostMerge")
    public static Object[][] testPostMerge() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testPost/PostMerge.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name = "testPostDelete")
    public static Object[][] testPostDelete() throws Exception
    {
        String filename = TestDataProvider.class.getResource("/testData/testPost/PostDelete.xlsx").getFile();
        OperateFile operateFile = new OperateFile();
        Object [][] data = operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="integrationRBAC")
    public static Object[][] integrationRBAC() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testRBAC/integration.xlsx").getFile();

        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }
    @DataProvider(name="TestSsoApp")
    public static Object[][] TestSsoApp() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testSsoApp/ssoapp.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }
    
    @DataProvider(name="testWebinar")
    public static Object[][] testWebinar() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testWebinar/createwebinar.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }


    @DataProvider(name="integrationDebug")
    public static Object[][] integrationDebug() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testIntegration/test.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="testKeyword")
    public static Object[][] testKeyword() throws Exception {

        String filename = TestDataProvider.class.getResource("/testData/testKeyword/testKeyword.xlsx").getFile();
        OperateFile operateFile = new OperateFile();
        Object[][] data = operateFile.getExcelData(filename);
        return data;
    }

    
    @DataProvider(name="testJingSalesTool")
    public static Object[][] testJingSalesTool() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testJingSalesTool/JingSalesTool.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }


    @DataProvider(name="test48hTextPublishing")
    public static Object[][] test48hTextPublishing() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testPost/48hTextPublishing/48hTextPublishing.xlsx").getFile();
        OperateFile operateFile = new OperateFile();
        Object[][] data = operateFile.getExcelData(filename);
        return data;
    }


    @DataProvider(name="TestQrCategory")
    public static Object[][] testQrCategory() throws Exception {
        String filename = TestDataProvider.class.getResource("/testData/testQrCategory/TestQrCategory.xlsx").getFile();
        OperateFile operateFile = new OperateFile();
        Object[][] data = operateFile.getExcelData(filename);
        return data;
    }


    @DataProvider(name="integrationTag")
    public static Object[][] integrationTag() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testIntegration/Integrationtag.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }


    @DataProvider(name="integrationQrcode")
    public static Object[][] integrationQrcode() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testIntegration/Integrationqrcode.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="testCampaign")
    public static Object[][] testCampaign() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testCampaign/campaign.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="segmentFollowerStatus")
    public static Object[][] segmentFollowerStatus() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testSegmentation/followerstatus.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="testDefaultMessage")
    public static Object[][] testDefaultMessage() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testDefaultMessage/testDefaultMessage.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="integrationWechatWork")
    public static Object[][] integrationWechatWork() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testIntegration/Integration_wechatwork.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="testPostCreate")
    public static Object[][] testPostCreate() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testPost/PostCreate/PostCreate.xlsx").getFile();
        OperateFile operateFile = new OperateFile();
        Object[][] data = operateFile.getExcelData(filename);
        return data;
    }


    @DataProvider(name="testAccountSet")
    public static Object[][] testAccountSet() throws Exception {

        String filename = TestDataProvider.class.getResource("/testData/testAccountSet/testAccountSet.xlsx").getFile();
        OperateFile operateFile = new OperateFile();
        Object[][] data = operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="testEvent")
    public static Object[][] testEvent() throws Exception {

        String filename = TestDataProvider.class.getResource("/testData/testEvent/event.xlsx").getFile();
        OperateFile operateFile = new OperateFile();
        Object[][] data = operateFile.getExcelData(filename);

        return data;
    }


    @DataProvider(name="integrationTemplate")
    public static Object[][] integrationTemplate() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testIntegration/Integrationtemplate.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="integrationCustomfieldDefault")
    public static Object[][] integrationCustomfieldDefault() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testIntegration/Integration_customfield_default.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="integrationCustomfield")
    public static Object[][] integrationCustomfield() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testIntegration/Integration_customfield.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }


    @DataProvider(name="testPersonalizedPost")
    public static Object[][] testPersonalizedPost() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testPost/PersonalizedPost.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }
    

    @DataProvider(name="testAdminSet")
    public static Object[][] testAdminSet() throws Exception {
        String filename = TestDataProvider.class.getResource("/testData/testAdminSet/testAdminSet.xlsx").getFile();
        OperateFile operateFile = new OperateFile();
        Object[][] data = operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="testNavigation")
    public static Object[][] testNavigation() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testNavigation/testNavigation.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }
    @DataProvider(name="test2CNavigation")
    public static Object[][] test2CNavigation() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testNavigation/test2CNavigation.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }
    @DataProvider(name="testDefaultNavigation")
    public static Object[][] testDefaultNavigation() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testNavigation/testDefaultNavigation.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="testFileManagement")
    public static Object[][] testFileManagement() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testFileManagement/filemanagement.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }


    @DataProvider(name="testScoringRule")
    public static Object[][] testScoringRule() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testScoring/testScoringRule.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }
    @DataProvider(name="testPostWorkflow")
    public static Object[][] testPostWorkflow() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testPost/PostWorkflow.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }
    @DataProvider(name="testContentImage")
    public static Object[][] testContentImage() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testPost/ContentImage.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }
    @DataProvider(name="segmentBasics")
    public static Object[][] segmentBasics() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testSegmentation/segmentbasics.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="segmentProfile")
    public static Object[][] segmentProfile() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testSegmentation/profile4.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="segmentActionsTotalABTools")
    public static Object[][] segmentActionsTotalABTools() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testSegmentation/actionTotalABTools.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="segmentActionsMenu")
    public static Object[][] segmentActionsMenu() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testSegmentation/actionMenu.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="segmentActionsMessage")
    public static Object[][] segmentActionsMessage() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testSegmentation/actionMessage.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="segmentActionsQRCode")
    public static Object[][] segmentActionsQRCode() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testSegmentation/actionQRCode.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="segmentActionsPost")
    public static Object[][] segmentActionsPost() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testSegmentation/actionPost.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="segmentActionsConversation")
    public static Object[][] segmentActionsConversation() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testSegmentation/actionConversation.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="segmentActionsPoll")
    public static Object[][] segmentActionsPoll() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testSegmentation/actionPoll.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="segmentActionsJourney")
    public static Object[][] segmentActionsJourney() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testSegmentation/actionJourney.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="segmentActionsTemplate")
    public static Object[][] segmentActionsTemplate() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testSegmentation/actionTemplate.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }


    @DataProvider(name="follower")
    public static Object[][] follower() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testFollower/follower.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);

        return data;
    }

    @DataProvider(name="testCampaignAnalytics")
    public static Object[][] testCampaignAnalytics() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testCampaign/campaignanalytics.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="testNavigationandHomepage")
    public static Object[][] testNavigationandHomepage() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testJingSalesTool/NavigationandHomepage.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="testPrivacyClauseandTheme")
    public static Object[][] testPrivacyClauseandTheme() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testJingSalesTool/PrivacyClauseandTheme.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="testNameGroupCard")
    public static Object[][] testNameGroupCard() throws Exception
    {

        String filename =TestDataProvider.class.getResource("/testData/testJingSalesTool/NameGroupCard.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }



    private static Object[][] provideDate(String filepath) throws Exception {
        String filename = TestDataProvider.class.getResource(filepath).getFile();
        Object[][] data = new OperateFile().getExcelData(filename);
        return data;
    }

    @DataProvider(name="testPostV4Publish")
    public static Object[][] testPostV4Publish() throws Exception
    {
        return provideDate("/testData/testPost/PostV4/PostV4Publish.xlsx");
    }


    @DataProvider(name="testPostV4Preview")
    public static Object[][] testPostV4Preview() throws Exception
    {
        return provideDate("/testData/testPost/PostV4/PostV4Preview.xlsx");
    }

    @DataProvider(name="testPostV4Log")
    public static Object[][] testPostV4Log() throws Exception
    {
        return provideDate("/testData/testPost/PostV4/PostV4Log.xlsx");
    }



    @DataProvider(name="testChatbot")
    public static Object[][] testChatbot() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testChatbot/testChatbot.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }

    @DataProvider(name="testB2BRegression")
    public static Object[][] testB2BRegression() throws Exception
    {
        String filename =TestDataProvider.class.getResource("/testData/testB2B/testB2BRegression.xlsx").getFile();
        OperateFile operateFile=new OperateFile();
        Object[][] data=operateFile.getExcelData(filename);
        return data;
    }


}


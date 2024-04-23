package com.autotest.api.base;

import org.testng.annotations.BeforeSuite;

import java.util.HashMap;

public  class TestBase {
    public static String url;
    public static HashMap<String, String> headerMap;
    public static HashMap<String, String> headerMap2;
    public static HashMap<String, String> headerMap3;
    public static HashMap<String, String> thirdpartyHeaderMap;
    public static HashMap<String, String> integrationHeaderMap;
    public static HashMap<String, String> workwechatMiniHeaderMap;
    public static HashMap<String, String> salestoolHeaderMap;
    public static HashMap<String, String> datapermissionheaderMap;
    public static HashMap<String, String> mscdatapermissionMap;
    public static String thirdpartyToken;
    public static String thirdpartyURL;
    public static HashMap<String, String> thirdpartyOtherMidHeaderMap;
    public static String thirdpartyTokenOtherMid;
    @BeforeSuite
    public void BeforeSuite() throws Exception {
        headerMap2=new HashMap<>();
        headerMap3=new HashMap<>();
        integrationHeaderMap=new HashMap<>();
	    datapermissionheaderMap=new HashMap<>();
        mscdatapermissionMap=new HashMap<>();
	    TestNgConfig config = TestNgConfig.getInstance();
	    try {
            thirdpartyHeaderMap =config.getThirdpartyLoginInfo();
        }catch (Exception e){
            System.out.println("第三方账号登录失败");
        }
	    url = config.getOpenAPIUrl();
        if(url.contains("116"))
            headerMap=config.getSession();
        else {
            headerMap = config.getAuthorization();
            headerMap2.put("authorization", headerMap.get("authorization"));
            headerMap2.put("J-CustomerUUID", config.getCustomerUUID2());
            headerMap3.put("authorization", headerMap.get("authorization"));
            headerMap3.put("J-CustomerUUID", config.getCustomerUUID3());
            datapermissionheaderMap.put("authorization", headerMap.get("authorization"));
            datapermissionheaderMap.put("J-CustomerUUID", config.getCustomerUUID5());
            System.out.println("headerMap" + headerMap2);

            //integration使用
            //integration old host
            thirdpartyURL = config.getThirdpartyHost();
            //integration headermap
            //thirdpartyHeaderMap =config.getThirdpartyLoginInfo();
            //integration token
            thirdpartyToken = thirdpartyHeaderMap.get("Authorization");
            //integration/workwechat账号内部api headermap

            integrationHeaderMap.put("authorization", headerMap.get("authorization"));
            integrationHeaderMap.put("J-CustomerUUID", config.getIntegrationuuid());
            //workwechat miniprogram账号内部api headermap
            workwechatMiniHeaderMap = config.getWorkwechatMiniprogramAuth();
            salestoolHeaderMap = config.getsalestoolLoginInfo();

            thirdpartyOtherMidHeaderMap = config.getThirdpartyOtherMidLoginInfo();
            //integration token
            thirdpartyTokenOtherMid = thirdpartyOtherMidHeaderMap.get("Authorization");
        }


    }
}

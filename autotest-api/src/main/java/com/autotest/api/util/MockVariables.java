package com.autotest.api.util;

import com.autotest.api.base.TestBase;
import com.autotest.api.base.TestNgConfig;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MockVariables {
    private static Logger logger = Logger.getLogger(MockVariables.class);

    public String currentTime() throws InterruptedException {
        String time = "";
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        time = sdf.format(d);
        return time;
    }

    public String dayStart(int i) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - i, 0, 0, 0);
        long tt = calendar.getTime().getTime() / 1000;

        return tt + "";

    }
    /**
     * @param i 当前时间往前往后推，1往后推一天，-1往前推一天
     * @return 2021-12-20
     * @description
     */
        public String dayStartFormat(int i){
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)+i, 23, 59, 59);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(calendar.getTime());
        }


    public String dayEnd() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        long tt = calendar.getTime().getTime() / 1000;

        return tt + "";

    }

    public String monthStart() throws InterruptedException {
        String timeStamp;
        String time = "";
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        timeStamp = c.getTimeInMillis() / 1000 + "";
        return timeStamp;
    }

    public String timeStampExpand(String timeNum) throws Exception {
        Calendar c = Calendar.getInstance();
        String timeStamp = "";
        int Num = Integer.parseInt(timeNum.substring(0, timeNum.length() - 1));
        if (timeNum.endsWith("M"))
            c.add(Calendar.MONTH, Num);
        else if (timeNum.endsWith("D"))
            c.add(Calendar.DAY_OF_MONTH, Num);
        else if (timeNum.endsWith("F"))
            c.add(Calendar.MINUTE, Num);
        else if (timeNum.endsWith("H"))
            c.add(Calendar.HOUR_OF_DAY, Num);
        else if (timeNum.endsWith("S"))
            c.add(Calendar.SECOND, Num);
        timeStamp = c.getTimeInMillis() / 1000 + "";
        return timeStamp;
    }

    /**
      * @Description: 返回当天的日期：yyyymmdd格式
      */
    public String  dateOfToday() throws InterruptedException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        Date dateOfToday = c.getTime();
        return  sdf.format(dateOfToday);
    }
    /**
     * @Description:
     *  &1mock_lastDateOfMonth ,return:当前月份的后1个月的最后一天日期
     *  &-2mock_lastDateOfMonth ,return:当前月份的前2个月的最后一天日期
     *
     * @params: timeNum 当前日期的月份偏移量（1：后1个月，2：后2个月，-3：前3个月）
     * @return: 指定月份的最后一天日期，YYYYMMDD格式
     */
    public String  lastDateOfXMonth(String timeNum) throws InterruptedException{
        int Num = Integer.parseInt(timeNum);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, Num);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        Date lastDateOfPrevMonth = c.getTime();
        return  sdf.format(lastDateOfPrevMonth);
    }
    /**
     * @Description:
     *  &1mock_firstDateOfMonth ,return:当前月份的后1个月的第一天日期
     *  &-2mock_firstDateOfMonth ,return:当前月份的前2个月的第一天日期
     *
     * @params: timeNum 当前日期的月份偏移量（1：后1个月，2：后2个月，-3：前3个月）
     * @return: 指定月份的最后一天日期，YYYYMMDD格式
     */
    public String  firstDateOfXMonth(String timeNum) throws InterruptedException{
        int Num = Integer.parseInt(timeNum);
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH, Num);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return  (format.format(calendar.getTime()));

    }
    /**
     * @Description:
     *  &1mock_lastDateOfMonunixtimestamp,return:当前月份的后1个月的最后一天日期时间戳
     *  &-2mock_lastDateOfMonunixtimestamp,return:当前月份的前2个月的最后一天日期时间戳
     *
     * @params: timeNum 当前日期的月份偏移量（1：后1个月，2：后2个月，-3：前3个月）
     * @return: 指定月份的最后一天日期，时间戳格式
     */
    public String  lastDateOfXMonunixtimestamp(String timeNum) throws InterruptedException{
        int Num = Integer.parseInt(timeNum);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, Num);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return  ( c.getTimeInMillis()/1000+"");

    }
    /**
     * @Description:
     *  &1mock_firstDateOfMonth ,return:当前月份的后1个月的第一天日期时间戳
     *  &-2mock_firstDateOfMonth ,return:当前月份的前2个月的第一天日期时间戳
     *
     * @params: timeNum 当前日期的月份偏移量（1：后1个月，2：后2个月，-3：前3个月）
     * @return: 指定月份的最后一天日期，时间戳格式
     */
    public String firstDateOfXMonunixtimestamp(String timeNum) throws InterruptedException{
        int Num = Integer.parseInt(timeNum);
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH, Num);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

//        System.out.println("上个月第一天："+format.format(calendar.getTime()));
        return  ( calendar.getTimeInMillis()/1000+"" );

    }

    /**
     *
     * @param timeNum
     * @return
     * @throws Exception
     */
    public String datetimeExpand(String timeNum) throws Exception {
        Calendar c = Calendar.getInstance();
        long timeStamp ;
        int Num = Integer.parseInt(timeNum.substring(0, timeNum.length() - 1));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//要转换的时间格式
        if (timeNum.endsWith("M"))
            c.add(Calendar.MONTH, Num);
        else if (timeNum.endsWith("D"))
            c.add(Calendar.DAY_OF_MONTH, Num);
        else if (timeNum.endsWith("F"))
            c.add(Calendar.MINUTE, Num);
        else if (timeNum.endsWith("H"))
            c.add(Calendar.HOUR_OF_DAY, Num);
        else if (timeNum.endsWith("S"))
            c.add(Calendar.SECOND, Num);
        timeStamp = c.getTimeInMillis() / 1000 ;
        Date date=new Date(timeStamp*1000);
        String res=sdf.format(date);
        return res;
    }
    TestNgConfig config=TestNgConfig.getInstance();

    Map map = new HashMap();

    public Map basicVariables() throws Exception{

        TestNgConfig config=TestNgConfig.getInstance();
        Map map=new HashMap();
        String url=config.getOpenAPIUrl();
        if(url.contains("dev")){
            map.put("$mock_env","dev");
        }else if(url.contains("staging")){
            map.put("$mock_env","staging");
        }else if(url.startsWith("https://api.jingsocial.com")){
            map.put("$mock_env","app");
        }else if (url.startsWith("https://mscappapi.jingsocial.com")) {
            map.put("$mock_env","mscapp");
        }else if (url.startsWith("https://mscstagingapi.jingsocial.com")) {
            map.put("$mock_env","mscstaging");
        }

        map.put("$mock_time",currentTime());
        map.put("$mock_host",config.getOpenAPIUrl());
        map.put("$mock_mid",config.getMid());
        map.put("$mock_2mid",config.getMid2());
        //33 59 58
        map.put("$mock_3mid",config.getMid3());
        //807 10181 58
        map.put("$mock_4mid",config.getMid4());
        //574
        map.put("$mock_5mid",config.getMid5());

        map.put("$mock_workwechat_mid",config.getThirdpartyMid());
        long currenttime=System.currentTimeMillis();
        map.put("$mock_unixtimestamp",currenttime/1000L);
        map.put("$mock_delaytime",(currenttime+5*1000)/1000L);
        map.put("$mock_monthstart",monthStart());
        map.put("$mock_daystart",dayStart(0));
        map.put("$mock_yseterdaystart",dayStart(1));
        //startTime:获客活动开始时间，endTime；获客活动结束时间
        map.put("$startTime",dayStart(-1));
        map.put("$endTime",dayStart(-4));
        map.put("$mock_dayend",dayEnd());
        map.put("$mock_lastDateOfMonth",lastDateOfXMonth("0"));
        map.put("$mock_firstDateOfMonth",firstDateOfXMonth("0"));
        map.put("$mock_lastDateOfMonunixtimestamp",lastDateOfXMonunixtimestamp("0"));
        map.put("$mock_firstDateOfMonunixtimestamp",firstDateOfXMonunixtimestamp("0"));
        map.put("$mock_dateOfToday",dateOfToday());

        if(config.getOpenAPIUrl().contains("116")){
            map.put("$mock_authorization", TestBase.headerMap.get("authorization"));
        }
        else{
        // authorization uuid b2b账号 124 68 58, system admin登录
        map.put("$mock_J-CustomerUUID",TestBase.headerMap.get("J-CustomerUUID"));
        //b2c账号 33 59 61
        map.put("$mock_b2c_J-CustomerUUID",config.getCustomerUUID2());
        //33 59 58
        map.put("$mock_3_J-CustomerUUID",config.getCustomerUUID3());

        //222 77 58
        map.put("$mock_4_J-CustomerUUID",config.getIntegrationuuid());

        //33 59 58,apiuser登录
        map.put("$mock_thirdparty_auth",TestBase.thirdpartyHeaderMap.get("Authorization"));
        map.put("$mock_thirdparty_username",config.getThirdpartyUsername());
        map.put("$mock_thirdparty_password",config.getThirdpartyPassword());
        //124 68  58 mscapp101 apiuser登录
        map.put("$mock_thirdparty_auth_othermid",TestBase.thirdpartyOtherMidHeaderMap.get("Authorization"));
        map.put("$mock_thirdparty_username_othermid",config.getThirdpartyUsernameOtherMid());
        map.put("$mock_thirdparty_password_othermid",config.getThirdpartyPasswordOtherMid());

        //企业微信小程序auth
        map.put("$mock_wm_authorization", TestBase.workwechatMiniHeaderMap.get("Authorization"));



        //surveyurl
        map.put("$mock_surveyurl",config.getSurveyurl());

        //integration使用
        map.put("$mock_authURL",config.getOpenAPIUrl());    //app.jingsocial.com
        map.put("$mock_tokenURL",config.getThirdpartyHost());   //api.jingsocial.com
        map.put("$mock_workwechat_uuid",config.getWorkWechaUUID());
        map.put("$mock_inte_openid1",config.getIntegrationOpenid1());
        map.put("$mock_inte_openid2",config.getIntegrationOpenid2());
        map.put("$mock_inte_openid3",config.getIntegrationOpenid3());
        map.put("$mock_inte_openid4",config.getIntegrationOpenid4());

        //callback  dev124  stage68  live58  mscapp101
        String[] arr=config.getSignature(config.getMid());
        map.put("$mock_callbackUrl", config.getrequestUrl(config.getCallbackurl(), config.getMid(),arr[2], arr[0], arr[1],config.getAppID(),config.getOpenid()));
        map.put("$mock_wechatID",config.getWechatID());
        String[] arr2=config.getSignature(config.getMid2());
        //dev33 stage59 live61  mscapp102
        map.put("$mock_2callbackUrl", config.getrequestUrl(config.getCallbackurl(), config.getMid2(),arr2[2], arr2[0], arr2[1],"",""));
        //wechatid: dev33 stage59 live58   mscapp101
        String[] arr3=config.getSignature(config.getMid3());
        map.put("$mock_3callbackUrl", config.getrequestUrl(config.getCallbackurl(), config.getMid3(),arr3[2], arr3[0], arr3[1],"",""));
        //wechatid: dev807 stage 10181  live579  mscapp 104
        String[] arr4=config.getSignature(config.getMid4());
        map.put("$mock_4callbackUrl", config.getrequestUrl(config.getCallbackurl(), config.getMid4(),arr4[2], arr4[0], arr4[1],"",""));
        //workwechat callback
        map.put("$mock_workwechatcallback",config.getWorkWechatCallback());
        map.put("$mock_sCorpID",config.getWorkWechatCorpID());

        //获取环境url
//        map.put("$mock_url",config.getThirdpartyHost());

        //clc jstracking
        map.put("$mock_webcode", config.getCLCWebcode());
        map.put("$mock_webcode_id", config.getCLCWebcodeID());
        map.put("$mock_webcode_name", config.getCLCWebcodeName());
        map.put("$mock_minicode", config.getCLCMinicode());
        map.put("$mock_minicode_id", config.getCLCMinicodeID());
        map.put("$mock_minicode_name", config.getCLCMinicodename());

        // getHashCode
        map.put("$mock_hashcode", config.getHashCode());
        }
        return map;

    }


    public Map mockWechatId() {
         // 微信号，可供图文使用，---中间考虑加一个过滤器--只有执行到某个test的时候执行这段mock
        map.put("$mock_followedWeChatId_0", config.getConfig("followedWeChatId_0"));
        map.put("$mock_followedWeChatId_1", config.getConfig("followedWeChatId_1"));
        map.put("$mock_followedWeChatId_2", config.getConfig("followedWeChatId_2"));
        map.put("$mock_followedWeChatId_3", config.getConfig("followedWeChatId_3"));
        map.put("$mock_followedWeChatId_4", config.getConfig("followedWeChatId_4"));
        map.put("$mock_followedWeChatId_5", config.getConfig("followedWeChatId_5"));

        map.put("$mock_webcode",config.getCLCWebcode());
        map.put("$mock_webcode_id",config.getCLCWebcodeID());
        map.put("$mock_webcode_name",config.getCLCWebcodeName());
        map.put("$mock_minicode",config.getCLCMinicode());
        map.put("$mock_minicode_id",config.getCLCMinicodeID());
        map.put("$mock_minicode_name",config.getCLCMinicodename());

        // getHashCode
        map.put("$mock_hashcode",config.getHashCode());

        //appid
        map.put("$mock_appid",config.getAppID());
        return map;
    }

    public Map mockOpenId() {
        // 微信号，可供图文使用& 30003 图文数据
        map.put("$mock_OpenId_0", config.getConfig("OpenId_0"));
        map.put("$mock_OpenId_1", config.getConfig("OpenId_1"));
        map.put("$mock_OpenId_2",config.getConfig("OpenId_2"));
        map.put("$mock_PostId30003",config.getConfig("PostId30003"));
        map.put("$mock_GroupId",config.getConfig("GroupId"));
        return map;
    }


//    //待补充,希望调用图文接口 获取到各类图文id
//    public Map mockPostId(){
//    }





}

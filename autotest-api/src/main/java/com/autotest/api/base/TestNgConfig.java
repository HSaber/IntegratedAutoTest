package com.autotest.api.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.autotest.api.util.MongoDBUtil;
import com.autotest.api.util.RequestBaseApi;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class TestNgConfig {
    final static Logger Log = Logger.getLogger(TestNgConfig.class);
    private Properties confiProperties;
    private static volatile TestNgConfig instance = null;
    ConfigService configService = null;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    KafkaProducer<String, String> producer;
    MongoCollection<Document> collection;
    Properties props = new Properties();
    String debug ="no";//默认非调试模式
    String kafkakey ="yes";

    public static TestNgConfig getInstance() {
        if (instance == null) {
            synchronized (TestNgConfig.class) {
                if (instance == null) {
                    instance = new TestNgConfig();
                }
            }
        }
        return instance;
    }

    //Response 状态码
    public static int RESPNSE_STATUS_CODE_200 = 200;
    public static int RESPNSE_STATUS_CODE_201 = 201;
    public static int RESPNSE_STATUS_CODE_404 = 404;
    public static int RESPNSE_STATUS_CODE_500 = 500;
    RequestBaseApi requestBaseApi = null;

    public TestNgConfig() {
         String path ="";
         try {
		     if(System.getProperties().getProperty("os.name").contains("Windows")){
//			     path = System.getProperty("user.dir")+"\\src\\main\\resources\\lib\\getnacos.jar";
                 debug="yes";
		     }else {
			     path = System.getProperty("user.dir")+"/src/main/resources/lib/getnacos.jar";
			     //如果是jenkins服务器发请求不走kafka
                 kafkakey="no";
		     }
//	         File file = new File(path);
//	         URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});
//	         Class<?> clazz1 = urlClassLoader.loadClass("test.getnacos");
//	         Method method1 = clazz1.getDeclaredMethod("getConfig", null);
//	         configService = (ConfigService)method1.invoke(instance);
	         Log.info(path);
         } catch (Exception e) {
             e.printStackTrace();
         }

        requestBaseApi = new RequestBaseApi();
//        props.put("bootstrap.servers", this.getConfig("Kafka_Server"));
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        producer = new KafkaProducer<String, String>(props);
//        MongoDatabase mongoDatabase = MongoDBUtil.getConnectAuth(this.getMongoserver(),this.getMongokey());
//        collection = mongoDatabase.getCollection("test");

    }

    /**
     * 获得配置文件中的属性值
     *
     * @param property 属性关键字
     * @author Maggie Xie
     */
    public String getConfig(String property) {
        if (confiProperties == null) {
            synchronized (TestNgConfig.class) {
                if (confiProperties == null) {
                    confiProperties = new Properties();
                    InputStreamReader is = null;
                    try {
                        //是否开启调试模式 会自动判断
                        if(debug.equals("yes")){
                            //开发调试专用
                            String dataId = "dev_config";
                            String group = "DEFAULT_GROUP";
                            //configService = getnacos.getConfig();
                            //String configstr = configService.getConfig(dataId, group,3000);
	                        //InputStream in = new ByteArrayInputStream(configstr.getBytes());
                           // is = new InputStreamReader(in);
                            is = new InputStreamReader(TestNgConfig.class.getResourceAsStream("/filters/config.properties"), "gbk");
                        }else {
                            //生产执行专用
                            Log.info(TestNgConfig.class.getResource("/filters/config.properties").getFile());
                            is = new InputStreamReader(TestNgConfig.class.getResourceAsStream("/filters/config.properties"), "gbk");
                        }
                        confiProperties.load(is);
                    } catch (IOException  e) {
                        e.printStackTrace();
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                Log.error(e.getMessage());
                            }
                        }
                    }
                }
            }
        }
        return confiProperties.getProperty(property);
    }

    /**
     * 根据给定的环境，获取对应环境的api url
     *
     * @author Maggie Xie
     * @return
     */

    //返回kafka生产者用于推送任务
    public KafkaProducer<String, String> getproducer() {
        return this.producer;
    }

    public String getKafkakey() {
        return this.kafkakey;
    }

    public MongoCollection<Document> getMongoConnect() {
        return this.collection;
    }

    public String getMongoserver() {
        return this.getConfig("Mongo_Host");
    }

    public String getMongokey() {
        return this.getConfig("Mongo_key");
    }

    public String getOpenAPIUrl() {
        String apiUrl = this.getConfig("API_HOST");
        return apiUrl;
    }

    public String getMid() {
        String mid = this.getConfig("API_MID");
        return mid;
    }

    public String getMid2() {
        String mid = this.getConfig("API_MID2");
        return mid;
    }

    public String getMid3() {
        String mid = this.getConfig("API_MID3");
        return mid;
    }

    public String getMid4() {
        String mid = this.getConfig("API_MID4");
        return mid;
    }

    public String getMid5() {
        String mid = this.getConfig("API_MID5");
        return mid;
    }

    public String getUserName() {
        String userName = this.getConfig("API_USERNAME");
        return userName;
    }

    public String getPassword() {
        String passWord = this.getConfig("API_PASSWORD");
        return passWord;
    }

    public String getCustomerUUID() {
        String passWord = this.getConfig("API_CUSTOMERUUID");
        return passWord;
    }

    public String getCustomerUUID2() {
        String uuid2 = this.getConfig("API_UUID2");
        return uuid2;
    }

    public String getCustomerUUID3() {
        String uuid3 = this.getConfig("API_UUID3");
        return uuid3;
    }

    //574相关
    public String getCustomerUUID5() {
        String uuid3 = this.getConfig("API_UUID5");
        return uuid3;
    }

    public String getCallbackurl() {
        String url = this.getConfig("API_CALLBACKURL");
        return url;
    }

    public String getAppID() {
        String appid = this.getConfig("API_APPID");
        return appid;
    }

    public String getAppID2() {
        String appid2 = this.getConfig("API_APPID2");
        return appid2;
    }

    public String getWechatID() {
        String wechatid = this.getConfig("API_WECHATID");
        return wechatid;
    }

    public String getWechatID2() {
        String wechatid2 = this.getConfig("API_WECHATID2");
        return wechatid2;
    }

    public String getOpenid() {
        String openid = this.getConfig("API_OPENID");
        return openid;
    }

    public String getSurveyurl() {
        String surveyurl = this.getConfig("API_SURVEYURL");
        return surveyurl;
    }

    public String getThirdpartyHost() {
        String thirdpartyHost = this.getConfig("API_Thirdparty_HOST");
        return thirdpartyHost;
    }

    public String getThirdpartyUsername() {
        String thirdpartyUsername = this.getConfig("API_Thirdparty_username");
        return thirdpartyUsername;
    }

    public String getThirdpartyPassword() {
        String thirdpartyPassword = this.getConfig("API_Thirdparty_password");
        return thirdpartyPassword;
    }

    public String getWorkWechaUUID() {
        String workWechaUUID = this.getConfig("API_WorkWechat_uuid");
        return workWechaUUID;
    }

    public String getThirdpartyMid() {
        String thirdpartyMid = this.getConfig("API_Thirdparty_mid");
        return thirdpartyMid;
    }

    public String getIntegrationOpenid1() {
        String openid1 = this.getConfig("integration_openid1");
        return openid1;
    }

    public String getIntegrationOpenid2() {
        String openid2 = this.getConfig("integration_openid2");
        return openid2;
    }

    public String getIntegrationOpenid3() {
        String openid3 = this.getConfig("integration_openid3");
        return openid3;
    }

    public String getIntegrationOpenid4() {
        String openid4 = this.getConfig("integration_openid4");
        return openid4;
    }

    public String getIntegrationuuid() {
        String integrationuuuid = this.getConfig("API_Thirdparty_uuid");
        return integrationuuuid;
    }

    public String getCLCWebcode() {
        String webcode = this.getConfig("clc_webcode");
        return webcode;
    }

    public String getCLCWebcodeID() {
        String webcodeid = this.getConfig("clc_webcode_id");
        return webcodeid;
    }

    public String getCLCWebcodeName() {
        String webcodename = this.getConfig("clc_webcode_name");
        return webcodename;
    }

    public String getCLCMinicode() {
        String minicode = this.getConfig("clc_minicode");
        return minicode;
    }

    public String getCLCMinicodeID() {
        String minicodeid = this.getConfig("clc_minicode_id");
        return minicodeid;
    }

    public String getCLCMinicodename() {
        String minicodename = this.getConfig("clc_minicode_name");
        return minicodename;
    }


    public String getBindOpenid() {
        String bindOpenid = this.getConfig("BIND_OPENID");
        return bindOpenid;
    }

    public String getLoginMid() {
        String loginMid = this.getConfig("LOGIN_MID");
        return loginMid;
    }

    public String getLoginuuid() {
        String Loginuuid = this.getConfig("LOGIN_UUID");
        return Loginuuid;
    }

    public String getWorkWechatCallback() {
        String url = this.getConfig("API_WorkWechat_CALLBACK");
        return url;
    }

    public String getWorkWechatCorpID() {
        String CorpID = this.getConfig("API_WorkWechat_CorpID");
        return CorpID;
    }

    public String getappPackage() {
        String appPackage = this.getConfig("APP_PACKAGE");
        return appPackage;
    }

    public String getappActivity() {
        String appActivity = this.getConfig("APP_ACTIVITY");
        return appActivity;
    }

    public String getsalestoolopenid() {
        String salestoolopenid = this.getConfig("APP_SALESTOOLOPENID");
        return salestoolopenid;
    }

    public String getsalestoolID() {
        String salestoolID = this.getConfig("APP_SALESTOOLID");
        return salestoolID;
    }

    public String getReorturl() {
        String reportUrl = this.getConfig("REPORT_URL");
        return reportUrl;
    }

    public String getThirdpartyUsername2() {
        String thirdpartyUsername2 = this.getConfig("API_Thirdparty_username2");
        return thirdpartyUsername2;
    }

    public String getThirdpartyPassword2() {
        String thirdpartyPassword2 = this.getConfig("API_Thirdparty_password2");
        return thirdpartyPassword2;
    }

        //扫码登录专用
    public String postlogin(String body, TestNgConfig config, String path, String mid) {

        String url = config.getOpenAPIUrl() + path;
        String req="";

        //调用callbak调整对应参数
        if(body.contains("<xml>")){
            String[] arr5= new String[0];
            try {
                arr5 = config.getSignature(mid);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            url = config.getrequestUrl(config.getCallbackurl(), mid,arr5[2], arr5[0], arr5[1],"","");
        }

        JSONObject json = new JSONObject();
        String testid = RandomStringUtils.randomAlphanumeric(12);
        json.put("testid", testid);
        json.put("url", url);
        json.put("body", body);
        json.put("header", JSONObject.parseObject(JSON.toJSONString(TestBase.thirdpartyHeaderMap)));
        String topic="dev_autologin";

        config.getproducer().send(new ProducerRecord<String, String>(topic, "sendkey", json.toString()));

        int ii= 0;
        while (true){
            ii++;
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String pidstr = FilterfindTest(testid,this.getMongoConnect());
            if(pidstr.length()>1 || ii>70)
            {
                req = pidstr;
                FilterfindDel(testid,this.getMongoConnect());
                break;
            }
        }
        return req;
    }

    //数据权限专用登录
    public String datalogin(TestNgConfig config, String username, String password) {
        String loginstr ="{\"username\":\"" + username +"\",\"password\":\"" + password + "\",\"validCode\":\"\"}";
        String rst="";

        JsonParser jp = new JsonParser();
        //如果token存在就直接返回token
        rst = postlogin(loginstr,config,"/api/user/users/login","");
        String gettoken="";
        Log.info(rst);

        if(jp.parse(rst).getAsJsonObject().getAsJsonObject("data").getAsJsonObject("identity").get("authorization").getAsString().equals("")){
            String qrcodetoken = jp.parse(rst).getAsJsonObject().getAsJsonObject("data").getAsJsonObject("identity").get("qrcodetoken").getAsString();
            //获取openid
            String mysqlopenid = postlogin("{\"mysql_conn\":\"jingsocial\",\"mysql_sql\":\"SELECT * FROM `admin`  where `name`='"+ username +"'\"}",config,"/api/autotest/getdata/mysql","");
            System.out.println(mysqlopenid);
            int num = jp.parse(mysqlopenid).getAsJsonObject().get("data").getAsJsonArray().size();
            String openid = jp.parse(mysqlopenid).getAsJsonObject().get("data").getAsJsonArray().get(num-1).getAsJsonObject().get("openid").getAsString();
            String qr = postlogin("{\"type\":\"login\",\"qrcodetoken\":\""+qrcodetoken+"\"}",config,"/api/user/users/temporaryqrcodes","");
            Log.info(qrcodetoken);
            Log.info(qr);
            String tid=jp.parse(qr).getAsJsonObject().get("data").getAsJsonObject().get("id").getAsString();
            String tuuid=jp.parse(qr).getAsJsonObject().get("data").getAsJsonObject().get("uuid").getAsString();
            String mysqlstr = postlogin("{\"mysql_conn\":\"jingsocial\",\"mysql_sql\":\"SELECT * FROM `qrcode`  where `id`="+ tid +"\"}",config,"/api/autotest/getdata/mysql","");
            String ticketid = jp.parse(mysqlstr).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("ticket").getAsString();
            String qrscenestr = jp.parse(mysqlstr).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("scene_str").getAsString();
            String mid = jp.parse(mysqlstr).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("customer_id").getAsString();

            Date date = new Date();
            String xmlbody="<xml>\n" +
                    "  <ToUserName>"+"jingdigital"+"</ToUserName>\n" +
                    "  <FromUserName>"+openid+"</FromUserName>\n" +
                    "  <CreateTime>" + date.getTime() + "</CreateTime>\n" +
                    "  <MsgType>"+"event"+"</MsgType>\n" +
                    "  <Event>"+"SCAN"+"</Event>\n" +
                    "  <EventKey>"+ qrscenestr + "</EventKey>\n" +
                    "  <Ticket>"+ticketid+"</Ticket>\n" +
                    "</xml>";

            postlogin(xmlbody,config,"/api/user/users/login",mid);

            //消息队列可能比较慢 要等等
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String body2 ="{\"type\":\"login\",\"uuid\":\""+tuuid+"\",\"qrcodetoken\":\""+qrcodetoken+"\"}";
            rst = postlogin(body2,config,"/api/user/users/checkuuid",mid);
            gettoken = jp.parse(rst).getAsJsonObject().get("data").getAsJsonObject().get("authorization").getAsString();

        }
        else{
            System.out.println("直接获取token");
            gettoken = jp.parse(rst).getAsJsonObject().getAsJsonObject("data").getAsJsonObject("identity").get("authorization").getAsString();
        }
        return gettoken;
    }


    public String getlogintoken(TestNgConfig config, String username, String password) {
        String loginstr ="{\"username\":\"" + username +"\",\"password\":\"" + password + "\",\"validCode\":\"\"}";
        String rst="";

        String nacoskey = config.getOpenAPIUrl().replace("https://","")+".user";
        JsonParser jp = new JsonParser();

        //如果token存在就直接返回token
        try {
            if(checktoken(configService,nacoskey) == false){
                rst = postlogin(loginstr,config,"/api/user/users/login","");
            }else {
                rst=StringUtils.substringAfter(configService.getConfig(nacoskey,"DEFAULT_GROUP",3000),"||");
                try {
                    return jp.parse(rst).getAsJsonObject().get("data").getAsJsonObject().get("authorization").getAsString();
                }catch (Exception e){
                    return jp.parse(rst).getAsJsonObject().get("data").getAsJsonObject().get("identity").getAsJsonObject().get("authorization").getAsString();
                }
            }
        } catch (NacosException e) {
            Log.info(rst);
        }

        Log.info(rst);

        String gettoken="";
        //JSONObject js = JSONObject.parseObject(new String(rst));
        //获取不到authorization就抛出异常展示错误信息
        try {
            jp.parse(rst).getAsJsonObject().getAsJsonObject("data").getAsJsonObject("identity").get("authorization").getAsString();
        }catch (Exception e){
	        jp.parse(rst).getAsJsonObject().get("data").getAsJsonObject().get("identity").getAsJsonObject().get("authorization").getAsString();
        }
        Log.info(rst);

        if(jp.parse(rst).getAsJsonObject().getAsJsonObject("data").getAsJsonObject("identity").get("authorization").getAsString().equals("")){
            String qrcodetoken = jp.parse(rst).getAsJsonObject().getAsJsonObject("data").getAsJsonObject("identity").get("qrcodetoken").getAsString();
            //获取openid
            String mysqlopenid = postlogin("{\"mysql_conn\":\"jingsocial\",\"mysql_sql\":\"SELECT * FROM `admin`  where `name`='"+ username +"'\"}",config,"/api/autotest/getdata/mysql","");
            Log.info("mysqlopenid");
            Log.info(mysqlopenid);
            String openid = jp.parse(mysqlopenid).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("openid").getAsString();
            String qr ="";
            if(openid.equals("")){
                mysqlopenid = postlogin("{\"mysql_conn\":\"jingsocial\",\"mysql_sql\":\"SELECT * FROM `admin`  where `openid`<>''\"}",config,"/api/autotest/getdata/mysql","");
                openid = jp.parse(mysqlopenid).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("openid").getAsString();
                qr = postlogin("{\"type\":\"bind\",\"qrcodetoken\":\""+qrcodetoken+"\"}",config,"/api/user/users/temporaryqrcodes","");
            }else{
                qr = postlogin("{\"type\":\"login\",\"qrcodetoken\":\""+qrcodetoken+"\"}",config,"/api/user/users/temporaryqrcodes","");
            }
            Log.info(qrcodetoken);
            Log.info(qr);
            String tid=jp.parse(qr).getAsJsonObject().get("data").getAsJsonObject().get("id").getAsString();
            String tuuid=jp.parse(qr).getAsJsonObject().get("data").getAsJsonObject().get("uuid").getAsString();
            String mysqlstr = postlogin("{\"mysql_conn\":\"jingsocial\",\"mysql_sql\":\"SELECT * FROM `qrcode`  where `id`="+ tid +"\"}",config,"/api/autotest/getdata/mysql","");
            String ticketid = jp.parse(mysqlstr).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("ticket").getAsString();
            String qrscenestr = jp.parse(mysqlstr).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("scene_str").getAsString();
            String mid = jp.parse(mysqlstr).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("customer_id").getAsString();

            Date date = new Date();
            String xmlbody="<xml>\n" +
                    "  <ToUserName>"+"jingdigital"+"</ToUserName>\n" +
                    "  <FromUserName>"+openid+"</FromUserName>\n" +
                    "  <CreateTime>" + date.getTime() + "</CreateTime>\n" +
                    "  <MsgType>"+"event"+"</MsgType>\n" +
                    "  <Event>"+"SCAN"+"</Event>\n" +
                    "  <EventKey>"+ qrscenestr + "</EventKey>\n" +
                    "  <Ticket>"+ticketid+"</Ticket>\n" +
                    "</xml>";

            postlogin(xmlbody,config,"/api/user/users/login",mid);

            //消息队列可能比较慢 要等等
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String body2 ="{\"type\":\"login\",\"uuid\":\""+tuuid+"\",\"qrcodetoken\":\""+qrcodetoken+"\"}";
            //String str = postlogin(body2,config,"/api/user/users/checkuuid",mid);

            try {
                // 当缓存不存在或者缓存过期则重新获取，当缓存存在且不过期但没有token则重新获取
                if(checktoken(configService,nacoskey) == false){
                    rst = postlogin(body2,config,"/api/user/users/checkuuid",mid);
                    configService.publishConfig(nacoskey,"DEFAULT_GROUP",df.format(new Date())+"||"+rst);
                    //加个缓冲时间  写nacos偶发有延迟
                    Thread.sleep(2000);
                }
                rst=StringUtils.substringAfter(configService.getConfig(nacoskey,"DEFAULT_GROUP",3000),"||");

            }catch (Exception e){
                e.printStackTrace();
            }

            gettoken = jp.parse(rst).getAsJsonObject().get("data").getAsJsonObject().get("authorization").getAsString();

        }
        else{
            gettoken = jp.parse(rst).getAsJsonObject().getAsJsonObject("data").getAsJsonObject("identity").get("authorization").getAsString();
        }
        return gettoken;
    }


    public boolean checktoken(ConfigService configService, String key) throws NacosException{
        boolean stat;
        String nacosstr = configService.getConfig(key,"DEFAULT_GROUP",3000);
        String nowStr = df.format(new Date());
        String updateStr = StringUtils.substringBefore(nacosstr,"||");
        String updateStr2 = StringUtils.substringAfter(nacosstr,"||");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date sd1 = null;
        Date sd2 = null;

        try {
            sd1 = df.parse(updateStr);
            sd2 = df.parse(nowStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.info("判断token时间");
        //判断token是否过期
	    if(Math.toIntExact((sd2.getTime() - sd1.getTime())/10000) > 200){
            stat=false;
        }else{
            stat=true;
        }

        if (!updateStr2.contains("{\"code\":0")){
            stat=false;
        }

        Log.info("token状态为"+stat);
        return stat;
    }

    //判断文件日期是否有效  token本地缓存过期在获取  只适用生产因为生产使用固定的用户  数据权限使用
    public boolean checkdate(String name){
        boolean stat;
        File file = new File(".//",name+".txt");
        if (file.exists()) {
            /////////如果文件存在就判断修改时间是不是当天/////////////
            BasicFileAttributes attr = null;
            try {
                Path path =  file.toPath();
                attr = Files.readAttributes(path, BasicFileAttributes.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 创建时间
            Instant instant = attr.lastModifiedTime().toInstant();
            String updateStr = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault()).format(instant);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String nowStr = df.format(new Date());

            Date sd1= null;
            Date sd2= null;
            try {
                sd1 = df.parse(updateStr);
                sd2 = df.parse(nowStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //如果token无效在重新获取
            if(Math.toIntExact((sd2.getTime() - sd1.getTime())/10000) > 8000){
                stat=false;
            }else{
                stat=true;
            }
        }else {
            stat=false;
        }

        Log.info("checkdate");
        Log.info(stat);
        return stat;
    }

    //读取本地token缓存  数据权限使用
    public String readtoken(Map map, String key){
        String filePath = ".\\" + key + ".txt";
        FileInputStream fin = null;
        String strTmp = "";
        String strTmp2 = "";
        BufferedReader buffReader = null;

        try {
            fin = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(fin);
            buffReader = new BufferedReader(reader);
            while((strTmp = buffReader.readLine())!=null){
                if(map != null){
                    map.put("&"+key,strTmp);
                }else {
                    strTmp2=strTmp;
                }
            }
            buffReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strTmp2;
    }


    //创建本地文件存储登录信息  用于减少用户登录次数
    public boolean createFile(String filecontent, String name){
        Boolean bool = false;
        String path = ".\\";
        //文件路径+名称
        String filenameTemp;
        filenameTemp = path+name+".txt";//文件路径+名称+文件类型
        File file = new File(filenameTemp);
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                file.createNewFile();
                bool = true;
            }
            writeFileContent(filenameTemp, filecontent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    //创建本地文件存储登录信息
    public static void writeFileContent(String filepath, String newstr) throws IOException {
        //String filein = newstr+"\r\n";//新写入的行，换行
        String filein = newstr;//新写入的行，换行
        String temp  = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //文件原有内容
//            for(int i=0;(temp =br.readLine())!=null;i++){
//                buffer.append(temp);
//                // 行与行之间的分隔符 相当于“\n”
//                buffer = buffer.append(System.getProperty("line.separator"));
//            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }


    //只允许TestBase调用，其他地方请直接用TestBase里的静态属性
    //否则会超出一个adminuser一天的调用限制
    //新登录方法同时支持 扫码和非扫码登录
    public HashMap<String, String> getAuthorization() {

        HashMap<String, String> authorizationMap = new HashMap<String, String>();
        String passWord = this.getPassword();
        String userName = this.getUserName();
        String defaultCustomerUUID = this.getCustomerUUID();
        TestNgConfig config = TestNgConfig.getInstance();
        String authorization=getlogintoken(config,userName,passWord);

        authorizationMap.put("authorization", authorization);
        authorizationMap.put("J-CustomerUUID", defaultCustomerUUID);
        return authorizationMap;
    }

    //注意，该方法只能在TestBase里调用，其他方法如果需要，请直接调用TestBase里的静态属性
    //否则会超出一个adminuser一天的调用限制
    //integration apiuser
    public HashMap<String, String> getThirdpartyLoginInfo() throws IOException, URISyntaxException {
        HashMap<String, String> authorizationMap = new HashMap<String, String>();
        String passWord = this.getThirdpartyPassword();
        String userName = this.getThirdpartyUsername();
        String passWord2 = this.getThirdpartyPassword2();
        String userName2 = this.getThirdpartyUsername2();
        String url = this.getOpenAPIUrl();
        String payload = null;
        String response = "";
        String authorization = "";
        String defaultCustomerUUID = "";
        String nowStr = df.format(new Date());
        if (passWord2 != null && userName2 != null) {
            payload = "{\"username\":\"" + userName + "\",\"password\":\"" + passWord + "\"}";
            Log.info("Thirdparty login payload= " + payload);
            try {

                String nacoskey = this.getOpenAPIUrl().replace("https://","")+".third";
                Log.info("获取缓存key:"+nacoskey);

                //当缓存不存在或者缓存过期则重新获取，当缓存存在且不过期但没有token则重新获取
                Log.info("开始检查token是否过期");
                if(checktoken(configService,nacoskey) == false){
                    //response = requestBaseApi.doPost(payload, url + "/api/user/users/thirdpartyuserlogin");
                    response = postlogin(payload, TestNgConfig.getInstance(),"/api/user/users/thirdpartyuserlogin","");
                    Log.info("第三方账户登录请求结果:"+response);
                    configService.publishConfig(nacoskey,"DEFAULT_GROUP",nowStr+"||"+response);
	                Thread.sleep(2000);
                }

                //截取token
                response=StringUtils.substringAfter(configService.getConfig(nacoskey,"DEFAULT_GROUP",3000),"||");
	            Log.info("获取缓存结果:"+response);

	            try {
                    final Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
                    authorization = JsonPath.parse(document).read("$.data.authorization").toString();
                    defaultCustomerUUID = JsonPath.parse(document).read("$.data.defaultCustomerUUID").toString();
                } catch (com.jayway.jsonpath.PathNotFoundException e) {
                    Log.info(response);
                    //              Log.error("当前ip不在白名单！");
                }
                authorizationMap.put("Authorization", authorization);
                authorizationMap.put("J-CustomerUUID", defaultCustomerUUID);
                System.out.println("mysql authorizationMap"+authorizationMap);
                Log.info(response);
            } catch (Exception e) {
                //连不上nacos配置就使用另外一个账号直接获取token
                Log.info("nacos connected fail");
                try {
                    payload = "{\"username\":\"" + userName2 + "\",\"password\":\"" + passWord2 + "\"}";
                    Log.info("Thirdparty login payload= " + payload);
                    //response = requestBaseApi.doPost(payload, url + "/api/user/users/thirdpartyuserlogin");
                    response = postlogin(payload, TestNgConfig.getInstance(),"/api/user/users/thirdpartyuserlogin","");
                    Log.info(response);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Log.info(e.getMessage());
                }

                try {
                    final Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
                    authorization = JsonPath.parse(document).read("$.data.authorization").toString();
                    defaultCustomerUUID = JsonPath.parse(document).read("$.data.defaultCustomerUUID").toString();
                } catch (com.jayway.jsonpath.PathNotFoundException e1) {
                    Log.error(response);
                    //Log.error("当前ip不在白名单！");
                }
                authorizationMap.put("Authorization", authorization);
                authorizationMap.put("J-CustomerUUID", defaultCustomerUUID);

                payload = "{" +
                        "    \"mysql_conn\": \"jingsocial\",\n" +
                        "    \"mysql_sql\": \"SELECT b.jwt,expired_time FROM `admin` a LEFT JOIN `jingsocial_jwt`b on a.`id`= b.admin_id WHERE `name`= '" + userName + "' and b.v2_user_type= 3 and b.`status`= 1 and expired_time> now() ORDER BY `create_time` desc\"" +
                        "}";

                try {
                   // response = requestBaseApi.doPost(payload, url + "/api/autotest/getdata/mysql", authorizationMap);
                    response = postlogin(payload, TestNgConfig.getInstance(),"/api/autotest/getdata/mysql","");
                    Log.info(response);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Log.info(e.getMessage());
                }

                JSONObject jsonobj = JSONObject.parseObject(response);
                if (jsonobj.getString("data").equals("[]")) {
                    if (passWord != null && userName != null) {
                        payload = "{\"username\":\"" + userName + "\",\"password\":\"" + passWord + "\"}";
                        Log.info("Thirdparty login payload= " + payload);
                        try {
                            //response = requestBaseApi.doPost(payload, url + "/api/user/users/thirdpartyuserlogin");
                            response = postlogin(payload, TestNgConfig.getInstance(),"/api/user/users/thirdpartyuserlogin","");
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            Log.info(e1.getMessage());
                        }
                        Log.info(url + "/api/user/users/thirdpartyuserlogin");
                        Log.info("Thirdparty login response= " + response);
                        try {
                            final Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
                            authorization = JsonPath.parse(document).read("$.data.authorization").toString();
                            defaultCustomerUUID = JsonPath.parse(document).read("$.data.defaultCustomerUUID").toString();
                        } catch (com.jayway.jsonpath.PathNotFoundException e1) {
                            Log.error("当前ip不在白名单！");
                        }
                        Log.info("Thirdparty login authorization= " + authorization);
                        Log.info("Thirdparty login defaultCustomerUUID= " + defaultCustomerUUID);
                        authorizationMap.put("Authorization", authorization);
                        authorizationMap.put("J-CustomerUUID", defaultCustomerUUID);

                    }
                } else {
                    authorization = jsonobj.getJSONArray("data").getJSONObject(0).getString("jwt");
                    Log.info("Mysql Thirdparty login authorization= " + authorization);
                    Log.info("Thirdparty login defaultCustomerUUID= " + defaultCustomerUUID);
                    authorizationMap.put("Authorization", authorization);
                    authorizationMap.put("J-CustomerUUID", defaultCustomerUUID);
                }

            }
        }

        return authorizationMap;
    }

    //获取workwechat miniprograme专用自动化测试账号headermap
    public HashMap<String, String> getWorkwechatMiniprogramAuth() {
        HashMap<String, String> authorizationMap = new HashMap<String, String>();
        String passWord = "Adminjing123";
        String userName = "huhuan_autotest";
        String url = this.getOpenAPIUrl();

        String defaultCustomerUUID = this.getIntegrationuuid();
        if (passWord != null && userName != null) {
            String payload = "{\"user_jinguuid\":\"" + userName + "\",\"user_password\":\"" + passWord + "\",\"unionid\":\"\",\"whitelist_secret\":\"\"}";

            String response = null;//调用loging api
            try {
                // response = requestBaseApi.doPost(payload, url + "/api/jfuser/openplatforms/miniprogramwhitelistlogin");
                response = postlogin(payload, TestNgConfig.getInstance(),"/api/jfuser/openplatforms/miniprogramwhitelistlogin","");
            } catch (Exception e) {
                e.printStackTrace();
                Log.info(e.getMessage());
            }

            String authorization = "";
            try {
                authorization = JsonPath.parse(response).read("$.data.authorization").toString();
            } catch (com.jayway.jsonpath.PathNotFoundException e) {
                Log.error("登录Miniprogram获取authorization失败");
            }

            authorizationMap.put("Authorization", authorization);
            authorizationMap.put("J-CustomerUUID", defaultCustomerUUID);

        }
        return authorizationMap;
    }

    //获取销售工具jwt信息 sdr material
    public HashMap<String, String> getsalestoolLoginInfo() {
        HashMap<String, String> authorizationMap = new HashMap<String, String>();
        String appid = this.getsalestoolID();
        String openid = this.getsalestoolopenid();
        String url = this.getOpenAPIUrl();


        if (openid != null && appid != null) {
            String payload = "{\"appid\":\"" + appid + "\",\"openid\":\"" + openid + "\"}";
            Log.info("Salestool login payload= " + payload);
            String response = null;//调用loging api
            try {
                // response = requestBaseApi.doPost(payload, url + "/api/user/users/generallogin");
                response = postlogin(payload, TestNgConfig.getInstance(),"/api/user/users/generallogin","");

                Log.info(url + "/api/user/users/generallogin");
                Log.info("Salestool login response= " + response);

            } catch (Exception e) {
                e.printStackTrace();
                Log.info(e.getMessage());
            }

            String authorization = "";
            String defaultCustomerUUID = "";
            try {
                final Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
                authorization = JsonPath.parse(document).read("$.data.jwt").toString();
                defaultCustomerUUID = JsonPath.parse(document).read("$.data.currentMid").toString();
            } catch (com.jayway.jsonpath.PathNotFoundException e) {
                Log.error("当前ip不在白名单！");
            }
            Log.info("Salestool login authorization= " + authorization);
            Log.info("Salestool login defaultCustomerUUID= " + defaultCustomerUUID);
            authorizationMap.put("Authorization", authorization);
            authorizationMap.put("J-CustomerUUID", defaultCustomerUUID);

        }
        return authorizationMap;
    }

    public String[] getSignature(String mid) throws InterruptedException, NoSuchAlgorithmException {
        String noncester = Encrypt(mid, "MD5");
        long time = System.currentTimeMillis() / 1000L;
        String timestamp = time + "";

        String str = Encrypt(mid, "MD5");
        String[] param = {timestamp, noncester, str};
        java.util.Arrays.sort(param);
        str = param[0] + param[1] + param[2];
        String signature = Encrypt(str, "SHA-1");
        String[] arra = {timestamp, noncester, signature};
        return arra;
    }

    public static String Encrypt(String strSrc, String encName) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try {
            if (encName == null || encName.equals("")) {
                encName = "MD5";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest()); //to HexString
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Invalid algorithm.");
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    public static String getrequestUrl(String baseurl, String mid, String signature, String timestamp, String nonce, String appID, String openid) {
        String callbackUrl;
        if (baseurl.contains("component/event"))
            callbackUrl = baseurl + "/" + appID + "?signature=" + signature + "&timestamp=" + timestamp + "&nonce=" + nonce;
        else if (mid.equals("68") || mid.equals("59"))
            callbackUrl = baseurl + "/id/" + mid + "?signature=" + signature + "&timestamp=" + timestamp + "&nonce=" + nonce + "&openid=" + openid;
        else
            callbackUrl = baseurl + "/id/" + mid + "?signature=" + signature + "&timestamp=" + timestamp + "&nonce=" + nonce;
        return callbackUrl;
    }

    public static String getHashCode() {
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";
        String timeStr = System.currentTimeMillis() + "";
        String hashcode = TestNgConfig.Encrypt(userAgent + timeStr, "MD5");
        int start = (int) Math.ceil(Math.random() * 10);
        hashcode = hashcode.substring(start, start + 10);

        return hashcode;
    }

    public String getThirdpartyUsernameOtherMid() {
        String thirdpartyUsername = this.getConfig("API_Thirdparty_username1");
        return thirdpartyUsername;
    }

    public String getThirdpartyPasswordOtherMid() {
        String thirdpartyPassword = this.getConfig("API_Thirdparty_password1");
        return thirdpartyPassword;
    }
    //    integration apiuser 124 68  58 101
    public HashMap<String, String> getThirdpartyOtherMidLoginInfo() {
        HashMap<String, String> authorizationMap = new HashMap<String, String>();
        String passWord = this.getThirdpartyPasswordOtherMid();
        String userName = this.getThirdpartyUsernameOtherMid();
        String url = this.getOpenAPIUrl();


        if (passWord != null && userName != null) {
            String payload = "{\"username\":\"" + userName + "\",\"password\":\"" + passWord + "\"}";
            Log.info("Thirdparty login payload= " + payload);
            String response = null;//调用loging api
            try {
                // response = requestBaseApi.doPost(payload, url + "/api/user/users/thirdpartyuserlogin");
                response = postlogin(payload, TestNgConfig.getInstance(),"/api/user/users/thirdpartyuserlogin","");

                Log.info(url + "/api/user/users/thirdpartyuserlogin");
                Log.info("Thirdparty login response= " + response);

            } catch (Exception e) {
                e.printStackTrace();
                Log.info(e.getMessage());
            }

            String authorization = "";
            String defaultCustomerUUID = "";
            try {
                final Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
                authorization = JsonPath.parse(document).read("$.data.authorization").toString();
                defaultCustomerUUID = JsonPath.parse(document).read("$.data.defaultCustomerUUID").toString();
            } catch (com.jayway.jsonpath.PathNotFoundException e) {
                Log.error("当前ip不在白名单！");
            }
            Log.info("Thirdparty login authorization= " + authorization);
            Log.info("Thirdparty login defaultCustomerUUID= " + defaultCustomerUUID);
            authorizationMap.put("Authorization", authorization);
            authorizationMap.put("J-CustomerUUID", defaultCustomerUUID);

        }
        return authorizationMap;
    }

    public String FilterfindTest(String pid, MongoCollection<Document> collection){
        //指定查询过滤器
        Bson filter = Filters.eq("pid", pid);
        //指定查询过滤器查询
        //FindIterable findIterable = collection.find(filter);
        MongoCursor<Document> cursor = collection.find(filter).skip(0).iterator();
        StringBuilder response = new StringBuilder();

        //MongoCursor cursor = findIterable.iterator();
        while (cursor.hasNext()) {
            response.append(JSONObject.parseObject(cursor.next().toJson()).get("body").toString());
        }
        return response.toString();
    }

    public void FilterfindDel(String pid, MongoCollection<Document> collection){
        //申明删除条件
        Bson filter = Filters.eq("pid",pid);
        //删除与筛选器匹配的单个文档
        collection.deleteOne(filter);
    }

    public HashMap<String, String> getSession() throws Exception{

        HashMap<String, String> sessionMap = new HashMap<String, String>();
        String passWord = this.getPassword();
        String userName = this.getUserName();
        TestNgConfig config = TestNgConfig.getInstance();
        RequestBaseApi requestBaseApi = new RequestBaseApi();
        Map loginMap=new HashMap<>();
        loginMap.put("passWord","Ab123~%40%40");
        loginMap.put("userName","604490051%40qq.com");
        loginMap.put("loginType",1);
        loginMap.put("submit","login");
        loginMap.put("code","");
        loginMap.put("areaCode","");

        String JSESSION=requestBaseApi.doPost(loginMap,config.getOpenAPIUrl());
        System.out.println("JSESSION"+JSESSION);
        sessionMap.put("Cookie", JSESSION);
        return sessionMap;
    }

}
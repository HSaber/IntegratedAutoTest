package com.autotest.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.autotest.api.asserts.ResponseChecker;
import com.autotest.api.base.TestNgConfig;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.autotest.api.base.TestBase.headerMap;

public class PrepareData {
    RequestBaseApi requestBaseApi=new RequestBaseApi();
    TestNgConfig config=TestNgConfig.getInstance();
    private static Logger Log = Logger.getLogger(PrepareData.class);
    /**
     *
     * @param parent_tag_id "0"代表一级tag 即父级标签或传父级标签id
     * @param tagsname   标签名称以逗号隔开
     * @return map格式返回（key:tag名称，value:tagid）
     * @throws Exception
     */
    public Map GetandCreateTag(String parent_tag_id,String tagsname,HashMap headerMap) throws Exception{
        Map tagmap=new HashMap();
        String[] tagsarr=tagsname.split(",");
        String tagspar="";
        for(int tagcount=0;tagcount<tagsarr.length;tagcount++)
            tagspar="\""+tagsarr[tagcount]+"\",\n"+tagspar;

        tagsname=tagspar.substring(1,tagspar.length()-3);
        String tagjson="{\"parent_tag_id\": \""+parent_tag_id+"\",\"tag_names\": [\""+tagsname+"\"]}";
        String tagstr=requestBaseApi.doPost(tagjson,config.getOpenAPIUrl()+"/api/omniuser/omnitag/batchcreate",headerMap);
        Object tagdoc=Configuration.defaultConfiguration().jsonProvider().parse(tagstr);

        JSONArray errortags=JSONObject.parseObject(tagstr).getJSONObject("data").getJSONArray("error_tags");
        JSONArray successtags=JSONObject.parseObject(tagstr).getJSONObject("data").getJSONArray("success_tags");
        if(successtags.size()>0){
            for(int i=0;i<successtags.size();i++){
                String taglabel=JsonPath.parse(tagdoc).read("$.data.success_tags["+i+"].name");
                String tagid=JsonPath.parse(tagdoc).read("$.data.success_tags["+i+"].id").toString();
                tagmap.put(taglabel,tagid); }
        }
        if(errortags.size()>0){
            for(int m=0;m<errortags.size();m++){
                String existtagname=errortags.getJSONObject(m).getString("name");
                String tagidstr=requestBaseApi.doGet(config.getOpenAPIUrl()+"/api/follower/tagcategories/tags/0",
                        headerMap,"?sort=-id&page=1&id=0&query={\"filter\":{\"name\":{\"like\":\""+existtagname+"\"}}}");
                Object tagiddoc=Configuration.defaultConfiguration().jsonProvider().parse(tagidstr);
                JSONArray json = JSONObject.parseObject(tagidstr).getJSONObject("data").getJSONArray("items");
                if(json.size()>0){
                    for(int i=0;i<json.size();i++){
                        String taglabel=JsonPath.parse(tagiddoc).read("$.data.items["+i+"].name");
                        if(taglabel.equals(existtagname)){
                            String tagid=JsonPath.parse(tagiddoc).read("$.data.items["+i+"].id").toString();
                            tagmap.put(taglabel,tagid);
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("tagmap:"+tagmap);
        return tagmap;
    }

    /**
     *
     * @params: postsname, 需要创建的图文名称，String格式：多个postname，以","分割
     * @return: map格式返回 （key:post的名称,value:postid）

     *     调用：Map postmap =prepareData.GetandCreatPost("post1,post2",headerMap);
     *     返回：postmap={post2=263765, post1=263764}
     *
     */
    public Map GetandCreatPost(String postsname, HashMap headerMap) throws IOException, URISyntaxException {
        Map postmap = new HashMap();
        String url = config.getOpenAPIUrl();
        String imageurlpath="";

        //分割postname，存到字符串数组里
        String[] postsarr = postsname.split(",");

        //第一步：上传图文封面图
        String coverurl =requestBaseApi.doFileUpload("/testData/testOmni/testFile/picture.jpg",url+"/api/content/uploads/coverpic",headerMap,"file");

        if (JSONPath.read(coverurl, "$.code").equals(0) && !JSONPath.read(coverurl, "$.data.url").toString().isEmpty()) {
            //图文封面获取成功
            imageurlpath = JSONPath.read(coverurl, "$.data.url").toString();
        } else {
            Log.info("----未获取到封面图片，创建post失败！！！"+coverurl+"-----");
        }

        //创建post，返回 postsarr[i]：post[i].id
        for (int i = 0; i < postsarr.length; i++) {

            String postjson ="{\n" +
                   "     \"name\":\"" + postsarr[i] + "\",\n" +
                    "    \"type\":1,\n" +
                    "    \"category\":\"\",\n" +
                    "    \"open_o2o_for_sales\":0,\n" +
                    "    \"data\":[\n" +
                    "        {\n" +
                    "            \"res_content_uuid\":\"\",\n" +
                    "            \"pic\":\"" + imageurlpath + "\",\n" +
                    "            \"cover\":\"" + imageurlpath + "\",\n" +
                    "            \"tit\":\"AutoTestPost\",\n" +
                    "            \"content\":\"<p>apitest</p>\",\n" +
                    "            \"detail\":\"\",\n" +
                    "            \"isTeaserLengthLimit\":0,\n" +
                    "            \"des\":\"test\",\n" +
                    "            \"need_open_comment\":1,\n" +
                    "            \"only_fans_can_comment\":\"0\",\n" +
                    "            \"linkUrl\":\"\",\n" +
                    "            \"tagIds\":[\n" +
                    "\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"res_uuid\":\"\",\n" +
                    "    \"is_privacy_policy\":0,\n" +
                    "    \"from\":1\n" +
                    "}";

            String poststr = requestBaseApi.doPost(postjson, url + "/api/content/posts", headerMap);

            if (JSONPath.read(poststr, "$.code").equals(0) && JSONPath.read(poststr, "$.data.message.id").toString()!=null) {
                postmap.put(postsarr[i], JSONPath.read(poststr, "$.data.message.id"));
            } else {
                Log.info("----创建post失败！！！"+poststr+"-----");
            }

        }
        return postmap;
    }

    /**
     * @params: grouptype, group的类型：static、dynamic
     * @params: groupname, group的名称，多个name，以","分割
     * @return: map返回格式 （key：group的名称，value：group的id）
     *
     *  调用：Map groupmap =prepareData.GetandCreatPost("group1,group2",headerMap);
     *  返回：groupmap={group2=263765, group1=263764}
     *
     *  调该方法创建的静态组：导入手机号=17621626511的用户组 （写死的）
     *  调该方法创建的动态组：wechat_nickname 不为空的用户组 （写死的）
     *
     */
    public Map GetandCreatGroup(String grouptype, String groupsname, HashMap headerMap) throws URISyntaxException, IOException {
        Map groupmap = new HashMap();
        String url = config.getOpenAPIUrl();
        String[] groupsarr = groupsname.split(",");
        if (grouptype == "static") {
        //静态组

            //上传文件
            String filedata = "type=static_group_import;import=/testData/testOmni/testFile/staticgroupemail.csv";
            String fields[] = filedata.trim().split(";");
            Map parametersMap=new HashMap();
            for (int i = 0; i < fields.length; i++) {
                String store[] = fields[i].trim().split("=");
                try{
                    parametersMap.put(store[0],store[1]);
                }catch (ArrayIndexOutOfBoundsException e){
                    parametersMap.put(store[0],"");
                }
            }
            String uploadgroupStr = requestBaseApi.doPost( parametersMap,url + "/api/omniuser/omniuser/uploadfile", headerMap);
            if (JSONPath.read(uploadgroupStr, "$.code").equals(0) && JSONPath.read(uploadgroupStr, "$.data.id").toString() != null) {
            // 校验上传文件
                String file_uuid = JSONPath.read(uploadgroupStr, "$.data.id").toString();
                groupmap.put("fileid",file_uuid);
                String validate="{\n" +
                        "    \"file_uuid\": \""+file_uuid+"\",\n" +
                        "    \"fields\": [\n" +
                        "        \"Email\",\n" +
                        "        \"Mobile\"\n" +
                        "    ]\n" +
                        "}";
                String validatefile=requestBaseApi.doPost(validate,url+"/api/omniuser/omniuser/validatefile",headerMap);
                if (JSONPath.read(validatefile,"$.data.status").equals("success")){
                    // 创建静态组
                    for (int a = 0; a < groupsarr.length; a++) {
                        String testbodydata = "{\"file_uuid\":\"" + file_uuid + "\",\"user_identity_mappings\":{\"Mobile\":\"mobile\",\"Email\":\"email\"},\"user_field_mappings\":{},\"group_name\":\"" + groupsarr[a] + "\"}";
                        String staticgroupStr = requestBaseApi.doPost(testbodydata, url + "/api/omniuser/omniuser/importuser", headerMap);
                        if (JSONPath.read(staticgroupStr, "$.code").equals(0) && JSONPath.read(staticgroupStr, "$.data.group_id").toString()!= null) {
                            groupmap.put(groupsarr[a], JSONPath.read(staticgroupStr, "$.data.group_id"));
                        } else {
                            Log.info("----创建静态组失败！！！"+staticgroupStr+"-----");
                        }
                    }
                }else {
                    Log.info("----校验上传文件失败！！！"+validatefile+"-----");
                }
            } else {
                Log.info("----静态组上传文件失败！！！"+uploadgroupStr+"-----");
            }
        } else if (grouptype == "dynamic") {
            //动态组
            for (int a = 0; a < groupsarr.length; a++) {
                //创建动态组
                String testdata = "{\"name\":\"" + groupsarr[a] + "\",\"dynamic_rule_data\":{\"subFilters\":[{\"conditions\":[{\"property_type\":\"Text\",\"parameter\":{\"operator\":\"not_empty\"},\"category\":\"customer\",\"property\":\"wechat_nickname\"}],\"logicalOperator\":\"and\"}],\"logicalOperator\":\"and\",\"scene\":\"Segment\"}}";
                String dynamicgroupStr = requestBaseApi.doPost(testdata, url + "/api/omniuser/segment/", headerMap);
                if (JSONPath.read(dynamicgroupStr, "$.code").equals(0) && JSONPath.read(dynamicgroupStr, "$.data.id").toString() != null) {
                    groupmap.put(groupsarr[a], JSONPath.read(dynamicgroupStr, "$.data.id"));
                } else {
                    Log.info("----创建动态组失败！！！"+dynamicgroupStr+"-----");
                }
            }

        } else {
            Log.info("----创建动态组失败！！！   grouptype not static or dynamic-----");
        }
        return groupmap;
    }

    /**
     * @params: categoryname ,无分类填"0"
     * @params: qrcodesname, qrcode的名称。多个name，以","分割
     * @return: map格式返回 （ key：categoryname，value：分类的id
     *                       key：qrcodesname[i].url，value：对应qrcode的qrcode_url
     *                       key：qrcodesname[i].sceneid：对应qrcode的qrcode_sceneid
     *                       key：qrcodesname[i].ticket，value：对应qrcode的qrcode_ticket
     *                       key：qrcodesname[i].id，value：对应qrcode的qrcode_id   ）
     *
     */
    public Map GetandCreateQrcode(String categoryname, String qrcodesname, HashMap headerMap) throws IOException, URISyntaxException {
        Map qrcodemap = new HashMap();
        String url = config.getOpenAPIUrl();
        int categoryid = 0;
        String[] qrcodesarr = qrcodesname.split(",");

        if (categoryname != "0" && categoryname != null) {
            String testbody = "{\"categoryName\":\"" + categoryname + "\",\"tagIds\":[]}";
            String categoryStr = requestBaseApi.doPost(testbody, url + "/api/engagement/qrcodecategories", headerMap);
            if (JSONPath.read(categoryStr, "$.code").equals(0) && JSONPath.read(categoryStr, "$.data.id").toString() != null) {
                qrcodemap.put(categoryname, JSONPath.read(categoryStr, "$.data.id"));
                categoryid = (int) JSONPath.read(categoryStr, "$.data.id");

            } else {
                Log.info("-----创建 QrcodeCategory 失败！！！"+categoryStr+"------");
            }
        } else {
            qrcodemap.put(categoryname, 0);
        }

        for (int i = 0; i < qrcodesarr.length; i++) {

            String qrcodeStr = requestBaseApi.doPost("{\n" +
                    "    \"qrcode_name\": \"" + qrcodesarr[i] + "\",\n" +
                    "    \"qrcode_status\": \"1\",\n" +
                    "    \"qrcode_category_id\": \"" + categoryid + "\",\n" +
                    "    \"tags_id\": [],\n" +
                    "    \"create_source\": \"0\",\n" +
                    "    \"reply_status\": \"1\",\n" +
                    "    \"reply_to\": 0,\n" +
                    "    \"is_once\": 0,\n" +
                    "    \"triggers_info\": [\n" +
                    "        {\n" +
                    "            \"triggers_action\": [\n" +
                    "                {\n" +
                    "                    \"reply_type\": 0,\n" +
                    "                    \"delay\": \"0\",\n" +
                    "                    \"reply_content\": \"测试api auto text\"\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"refer_reply_to\": 0,\n" +
                    "            \"segment_id\":\"0\",\n" +
                    "            \"trigger_id\": \"0\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}", url + "/api/engagement/qrcodes", headerMap);
            if (JSONPath.read(qrcodeStr, "$.code").equals(0) && JSONPath.read(qrcodeStr, "$.data.qrcode_id").toString() != null) {
                qrcodemap.put(qrcodesarr[i] + ".url", JSONPath.read(qrcodeStr, "$.data.qrcode_url").toString());
                qrcodemap.put(qrcodesarr[i] + ".sceneid", JSONPath.read(qrcodeStr, "$.data.scene_id"));
                qrcodemap.put(qrcodesarr[i] + ".ticket", JSONPath.read(qrcodeStr, "$.data.ticket").toString());
                qrcodemap.put(qrcodesarr[i] + ".id", JSONPath.read(qrcodeStr, "$.data.qrcode_id"));
            } else {
                Log.info("-----创建 Qrcode 失败！！！"+qrcodeStr+"------");
            }
        }
        return qrcodemap;
    }

    /**
     * @params: datetype, 回收数据类型：group、post、tag、qrcpde
     * @params: id，数据id：多个id以","分隔
     * @return: void
     */
    public void deletePareDate(String datetype, String ids, HashMap headerMap) throws IOException, URISyntaxException {
        String url = config.getOpenAPIUrl();
        String responseStr = null;
        String expectData = null;

        String[] idsarr = ids.split(",");
        for (int i = 0; i < idsarr.length; i++) {
            if (datetype == "group") {
                //删除组
                responseStr = requestBaseApi.delete("{\"id\":" + idsarr[i] + ",\"is_delete\":1}", url + "/api/omniuser/segment/" + idsarr[i], headerMap);

                expectData = "{\"code\":0,\"data\":{\"status\":1},\"message\":\"success\",\"msg\":\"success\"}";

            } else if (datetype == "post") {
                //删除post
                Log.info("-----" + idsarr[i]);
                responseStr = requestBaseApi.delete(url + "/api/content/posts/" + idsarr[i] + "?id=" + idsarr[i], headerMap);
                expectData = "{\"code\":0,\"data\":" + idsarr[i] + "}";

            } else if (datetype == "tag") {
                //删除tag
                responseStr = requestBaseApi.doPost("{\"ids\":[" + idsarr[i] + "],\"action_type\":1}", url + "/api/omniuser/omnitag/batchdelete", headerMap);

                expectData="{\"code\":0,\"data\":{\"is_delete\":1,\"relation_data\":[],\"delete_ids\":["+idsarr[i]+"],\"message\":\"success\",\"msg\":\"success\"}";


            } else if (datetype == "qrcode") {
                //删除qrcode
                responseStr =requestBaseApi.delete(url + "/api/engagement/qrcodes/" + idsarr[i], headerMap);
                expectData = "{\"code\":0,\"msg\":\"\",\"data\":{\"id\":\""+idsarr[i]+"\",\"usage_setting\":[]}}";


            } else if (datetype == "field") {
                //删除field
                responseStr =requestBaseApi.delete(url + "/api/follower/aggregate/followereavs/" + idsarr[i], headerMap);
                expectData = "{\"code\":0,\"msg\":\"success\",\"data\":{\"id\":\"" + idsarr[i] + "\"}}";

            }
            if (!expectData.isEmpty()) {
                ResponseChecker check = ResponseChecker.getInstance();
                boolean flag = check.dataCheck(expectData,responseStr);
                if ( flag==true){
                    Log.info("---删除" + datetype + "成功！！！---id： " + idsarr[i]);
                }else {
                    Log.info("---删除" + datetype + "失败！！！---id： " + idsarr[i]);
                    Log.info("responseSTR:  "+responseStr);
                    Log.info("expectData:  "+expectData);
                }
            }
        }

    }

    /**
     * @params: length, 随机生成的字符串位数
     * @return: val，随机字符串
     */
    public static String getCharAndNumr(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * @params: categoryname, 需要创建的分类name,无分类传"0"
     * @params: fieldnames, field的名称(label)，多个name，以","分割
     * @params: type, field的type：text、email、int、date、phone
     * @return: map返回格式 （key：firlf的名称(label)，value：field的id）
     *
     * 调该方法创建的field：ine类型默认2位小数、
     *                   date类默认是YYYY.MM.DD (eg. 2016.01.01)格式
     *                   phone类默认是Mobile Number (China)格式 （写死的）
     */

    public Map getandCreateField(String categoryname, String fieldnames, String type, HashMap headerMap) throws URISyntaxException, IOException {
        Map fieldmap = new HashMap();
        String url = config.getOpenAPIUrl();
        String[] fieldarr = fieldnames.split(",");
        String testbodydata = null;
        int categoryid = 0;
        if (categoryname != null && categoryname != "0") {
            String categorybody = "{\"category_name\":\"" + categoryname + "\",\"category_type\":0}";
            String categoryStr = requestBaseApi.doPost(categorybody, url + "/api/follower/followereavcategories", headerMap);
            if (JSONPath.read(categoryStr, "$.code").equals(0) && JSONPath.read(categoryStr, "$.data.id").toString() != null) {
                categoryid = Integer.parseInt(JSONPath.read(categoryStr, "$.data.id").toString());
                fieldmap.put(categoryname, categoryid);

            } else {
                Log.info("----创建field分类失败！！！" + categoryStr);
            }
        } else {
            fieldmap.put(categoryname, 0);
        }

        for (int i = 0; i < fieldarr.length; i++) {

            if (type == "text" || type == "email") {
                testbodydata = "{\"category_id\":" + categoryid + ",\"name\":\"" + fieldarr[i] + "\",\"type\":\"" + type + "\"}";

            } else if (type == "int") {
                testbodydata = "{\"category_id\":" + categoryid + ",\"name\":\"" + fieldarr[i] + "\",\"type\":\"int\",\"decimal\":\"2\"}";

            } else if (type == "date" || type == "phone") {

                testbodydata = "{\"category_id\":" + categoryid + ",\"name\":\"" + fieldarr[i] + "\",\"type\":\"date\",\"date_type\":0}";

            } else {
                Log.info("type 错误！请重新输入！！！你的type是" + type);
            }

            String fieldStr = requestBaseApi.doPost(testbodydata, url + "/api/follower/followereavs", headerMap);
            if (JSONPath.read(fieldStr, "$.code").equals(0) && JSONPath.read(fieldStr, "$.data.id").toString() != null) {

                fieldmap.put(fieldarr[i], JSONPath.read(fieldStr, "$.data.id"));

            } else {
                Log.info("----创建field失败！！！" + fieldStr);
            }
        }

        return fieldmap;
    }

    //上传图片，获取图片地址
    public String getPictureUrl(String filepath,HashMap headerMap) throws URISyntaxException, IOException{
        String result =  requestBaseApi.doFileUpload(filepath,config.getOpenAPIUrl()+"/api/content/groups/picupload",headerMap,"file");
        return JSONObject.parseObject(JSONObject.parseObject(result).getString("data")).getString("message");
    }
    //获取items对象
    public JSONArray items(HashMap headerMap,String uri,String params) throws URISyntaxException, IOException {
        JSONArray items =JSONObject.parseObject(
                JSONObject.parseObject(requestBaseApi.doGet(config.getOpenAPIUrl()+uri,headerMap,params))
                        .getString("data")).getJSONArray("items");;
        return items;
    }

//    创建企业微信活码
    public String getActiveQrcode(HashMap headerMap) throws Exception {
        String activeQrcodeJinguuid = "";
        JSONArray jsonArray = items(headerMap,"/api/workwechat/active_qrcode","?query={\"status\":1}&page=1&perPage=20");
        //items返回空，则创建获取企业微信活码id
        if(jsonArray==null){
            //创建空的企业微信活码
            activeQrcodeJinguuid = JSONObject.parseObject(JSONObject.parseObject(requestBaseApi.doPost("{\n" +
                    "\"basic\": {\n" +
                    "\"name\": \"放在获客活动中的活码\"\n" +
                    "}\n" +
                    "}",config.getOpenAPIUrl()+"/api/workwechat/active_qrcode",headerMap)).getString("data")).getString("jing_uuid");
            //获取企业微信架构
            String teamJingUUid = JSONObject.parseObject(
                    JSONObject.parseObject(requestBaseApi.doGet(config.getOpenAPIUrl()+"/api/workwechat/team/structure",headerMap,""))
                            .getString("data")).getJSONArray("items").getJSONObject(0).getString("jing_uuid");
            //更新企业微信活码，活码变成可以放到获客活动中使用
            requestBaseApi.doPost("{\n" +
                    "\"jing_uuid\": \""+activeQrcodeJinguuid+"\",\n" +
                    "\"rule\": {\n" +
                    "\"matching_method\": 0,\n" +
                    "\"filter_duplicate\": 1\n" +
                    "},\n" +
                    "\"rang_data\": {\n" +
                    "\"original_range_data\": [{\n" +
                    "\"isDept\": 1,\n" +
                    "\"id\": \""+teamJingUUid+"\"\n" +
                    "}],\n" +
                    "\"auto_pass\": 1,\n" +
                    "\"auto_pass_allday\": 1\n" +
                    "},\n" +
                    "\"welcome\": {\n" +
                    "\"welcome_msg_type\": 1\n" +
                    "}\n" +
                    "}",config.getOpenAPIUrl()+"/api/workwechat/active_qrcode",headerMap);
            Log.info("企业微信号活码id"+activeQrcodeJinguuid);
        }else
            activeQrcodeJinguuid =jsonArray.getJSONObject(0).getString("jing_uuid");

        return activeQrcodeJinguuid;
    }
    //获取活动表单id和名称，老表单
    public Map getSurveyV1NameandId(HashMap headerMap) throws URISyntaxException, IOException {
        String surveyName = "";
        String surveyId = "";
        JSONArray items = items(headerMap,"/api/survey/surveys","?page=2&query={\"filter\":{\"used_as_register\":1,\"version\":0}}");
        if(items==null){
            //To do 创建活动表单
        }else {
            surveyId = items.getJSONObject(0).getString("_id");
            surveyName = items.getJSONObject(0).getString("name");

        }
        Map<String,String> surveyMap = new HashMap();
        surveyMap.put("surveyV1Name",surveyName);
        surveyMap.put("surveyV1Id",surveyId);
        return surveyMap;
    }
    //获取短信模板id
    public String getSMSTemplate(HashMap headerMap) throws Exception{
        String template_id = "";
        JSONArray items = items(headerMap,"/api/sms/templates","?page=1&perPage=20&query={\"filter\":{\"status\":2,\"type\":\"code\"}}");
        if(items==null){
            //To do 创建sms template
        }else
            template_id = items.getJSONObject(0).getString("jing_uuid");
        return template_id;
    }
    //获取模板id
    public String getTemplateid(HashMap headerMap) throws Exception{
        String templateid = "";
        JSONArray items = items(headerMap,"/api/content/templates","?sort=-update_time&per-page=20");
        if(items==null){
            //To do 创建模板
        }else
            templateid = items.getJSONObject(0).getString("id");
        return templateid;
    }
    //获取订阅通知模板id和id
    public Map getSubscribeNotificationIdAndTemplate(HashMap headerMap) throws Exception{
        Map<String,String> map = new HashMap<>();
        JSONArray items = items(headerMap,"/api/content/subscribes","?page=1&sort=-id&query={\"filter\":{\"status\":0}}");
        map.put("$subscribesId",items.getJSONObject(0).getString("id"));
        map.put("subscribesTemplateId",items.getJSONObject(0).getString("template_id"));
        return map;
    }
    //获取渠道md5
    public String getOmniChannels(HashMap headerMap) throws Exception{
        String channelId = "";
        JSONArray data = JSONObject.parseObject(requestBaseApi.doGet(config.getOpenAPIUrl()+"/api/omniuser/omnichannels/all",headerMap,"?filter={\"channel\":\"wechat\"}")).getJSONArray("data");
        if(data!=null){
            channelId = data.getJSONObject(0).getString("value");
        }
        System.out.println("公众号md5"+channelId);
        return channelId;
    }
    /**
     * @param hearderMap
     * @return
     * @description 获取发送邮件和收件邮件的地址
     */

    public Map getEmailAddress(HashMap hearderMap) throws Exception{
        Map<String,String>  edmMap = new HashMap<>();
        edmMap.put("$sendEmailAddress",items(hearderMap,"/api/edm/email","?query={\"filter\":{\"type\":\"sender\"}}").getJSONObject(0).getString("jing_uuid"));
        edmMap.put("$replyEmailAddress",items(hearderMap,"/api/edm/email","?query={\"filter\":{\"type\":\"replier\"}}").getJSONObject(0).getString("jing_uuid"));
        return edmMap;

    }
    /**
     * @param
     * @return
     * @description  创建邮件模板并返回模板id，用于通知类
     */

    public String createAndGetEDMTemplate(HashMap headerMap,String entityName,String entityTpye,String entityJing_uuid,String notificationType) throws Exception{
//        System.out.println("邮件发送地址："+getEmailAddress(headerMap).get("$sendEmailAddress"));
//        System.out.println("回复邮件地址:"+getEmailAddress(headerMap).get("$replyEmailAddress"));
//        System.out.println("创建edm模板"+requestBaseApi.doPost("{\n" +
//                "\"subject\": \"test\",\n" +
//                "\"content\": \"<p>"+entityTpye+"-"+notificationType+"邮件通知</p>\",\n" +
//                "\"send_email_address_id\": \""+getEmailAddress(headerMap).get("$sendEmailAddress")+"\",\n" +
//                "\"reply_email_address_id\": \""+getEmailAddress(headerMap).get("$replyEmailAddress")+"\",\n" +
//                "\"name\": \""+entityName+" "+notificationType+"\",\n" +
//                "\"is_unique\": true,\n" +
//                "\"source\": \""+entityTpye+"\",\n" +
//                "\"source_value\": \""+entityJing_uuid+"\"\n" +
//                "}",config.getOpenAPIUrl()+"/api/edm/templates",headerMap));
        return JSONObject.parseObject(JSONObject.parseObject(requestBaseApi.doPost("{\n" +
                "\"subject\": \"test\",\n" +
                "\"content\": \"<p>"+entityTpye+"-"+notificationType+"邮件通知</p>\",\n" +
                "\"send_email_address_id\": \""+getEmailAddress(headerMap).get("$sendEmailAddress")+"\",\n" +
                "\"reply_email_address_id\": \""+getEmailAddress(headerMap).get("$replyEmailAddress")+"\",\n" +
                "\"name\": \""+entityName+" "+notificationType+"\",\n" +
                "\"is_unique\": true,\n" +
                "\"source\": \""+entityTpye+"\",\n" +
                "\"source_value\": \""+entityJing_uuid+"\"\n" +
                "}",config.getOpenAPIUrl()+"/api/edm/templates",headerMap)).getString("data")).getString("jing_uuid");
    }
    //获取短信签名,通知类签名
    public String getSMSSign(HashMap headerMap) throws Exception{
        return items(headerMap,"/api/sms/sign","?page=1&query={\"filter\":{\"status\":2,\"type\":\"notice\"}}").getJSONObject(0).getString("jing_uuid");
    }
    public String createAndGetSMSTemplate(HashMap headerMap,String entityName,String entityTpye,String entityJing_uuid,String notificationType)throws Exception{
//        System.out.println("短信签名："+getSMSSign(headerMap));
//        System.out.println("创建的短信模板"+requestBaseApi.doPost("{\n" +
//                "\"name\": \""+entityName+" "+notificationType+"\",\n" +
//                "\"sign_jing_uuid\": \""+getSMSSign(headerMap)+"\",\n" +
//                "\"source\": \""+entityTpye+"\",\n" +
//                "\"source_value\": \""+entityJing_uuid+"\",\n" +
//                "\"content_type\": \"custom\",\n" +
//                "\"content_extend\": \"<div>提交报名信息</div>\",\n" +
//                "\"content\": \"提交报名信息\",\n" +
//                "\"notify_telephone\": \"\",\n" +
//                "\"is_unique\": true\n" +
//                "}",config.getOpenAPIUrl()+"/api/sms/templates",headerMap));
        return JSONObject.parseObject(JSONObject.parseObject(requestBaseApi.doPost("{\n" +
                "\"name\": \""+entityName+" "+notificationType+"\",\n" +
                "\"sign_jing_uuid\": \""+getSMSSign(headerMap)+"\",\n" +
                "\"source\": \""+entityTpye+"\",\n" +
                "\"source_value\": \""+entityJing_uuid+"\",\n" +
                "\"content_type\": \"custom\",\n" +
                "\"content_extend\": \"<div>提交报名信息</div>\",\n" +
                "\"content\": \"提交报名信息\",\n" +
                "\"notify_telephone\": \"\",\n" +
                "\"is_unique\": true\n" +
                "}",config.getOpenAPIUrl()+"/api/sms/templates",headerMap)).getString("data")).getString("jing_uuid");
    }
    /*
     * @description 获取部门id，现在只用到了根部门的id，只获取根部门id，后续有需要加
     */

    public String getDeptIds() throws URISyntaxException, IOException {
        String deptId = "";
        JSONArray  items = JSONObject.parseObject(JSONObject.parseObject(requestBaseApi.doGet(config.getOpenAPIUrl()+"/api/workwechat/team/structure",headerMap,"")).getString("data")).getJSONArray("items").getJSONObject(0).getJSONArray("children");
        for (int i=0;i<items.size();i++){
            String label = items.getJSONObject(i).getString("label");
            if(label.equals("自动化测试专用部门-勿删")){
                deptId = items.getJSONObject(i).getString("jing_uuid");
                break;
            }
        }
        return  deptId;
    }

    /**
     * 功能描述: 通过appid查询到对应的id
     *
     * @params: appid, 查询的appid
     * @return: map返回格式 （key：appid，value：appid对应的id）
     *
     */
    public Map getMiniByAppid (String appid, HashMap headerMap) throws URISyntaxException, IOException {
        Map miniMap = new HashMap();
        String url = config.getOpenAPIUrl();
        String ministr=requestBaseApi.doGet(url+"/api/content/miniprograms/getlist",headerMap,"?per-page=100");
        JSONArray miniitem=JSONObject.parseObject(ministr).getJSONObject("data").getJSONArray("items");
        if (JSONPath.read(ministr,"$.code").equals(0)){
            for (int i=0;i< miniitem.size();i++){
                String appidstr=JsonPath.parse(ministr).read("$.data.items["+i+"].program_appid").toString();
                if (appidstr.equals(appid)){
                    String name=JsonPath.parse(ministr).read("$.data.items["+i+"].name").toString();
                    String id=JsonPath.parse(ministr).read("$.data.items["+i+"].id").toString();
                    miniMap.put(appid,id);
                    break;
                }else {
                    miniMap.put(appid,"null");
                }
            }
        }
        return miniMap;
    }
    //获取活动表单id和名称，新表单
    public Map getSurveyV2NameandId(HashMap headerMap) throws URISyntaxException, IOException {
        String surveyName = "";
        String surveyId = "";
        JSONArray items = items(headerMap,"/api/survey/surveys","?page=2&query={\"filter\":{\"used_as_register\":1,\"version\":1}}");
        if(items==null){
            //To do 创建活动表单
        }else {
            surveyId = items.getJSONObject(0).getString("_id");
            surveyName = items.getJSONObject(0).getString("name");

        }
        Map<String,String> surveyMap = new HashMap();
        surveyMap.put("surveyV2Name",surveyName);
        surveyMap.put("surveyV2Id",surveyId);
        return surveyMap;
    }

    public Map getFileNameAndId(HashMap headerMap) throws Exception {
        String fileName = "";
        String fileId = "";
        JSONArray items = items(headerMap,"/api/material/enabledList","");
        if(items==null){
            //上传PDF
           String uploadFile =  requestBaseApi.doFileUpload("/testData/testLeadsgeneration/让测试不再成为持续交付的瓶颈-2021.pdf",config.getOpenAPIUrl()+"/api/material/upload",headerMap,"file");
           Assert.assertEquals(JSONObject.parseObject(uploadFile).getString("code"),0,"文件上传失败");
           String filePath = JSONObject.parseObject(JSONObject.parseObject(uploadFile).getString("code")).getString("path");
           //创建文件
            String createFile = requestBaseApi.doPost("{\n" +
                    "\"name\": \"自动化上传的文件\",\n" +
                    "\"path\": \""+filePath+"\",\n" +
                    "\"tags\": [],\n" +
                    "\"can_share\": 1,\n" +
                    "\"channel_id\": \""+getOmniChannels(headerMap)+"\",\n" +
                    "\"started_at\": 0,\n" +
                    "\"ended_at\": 0,\n" +
                    "\"owner_department_id\": \"\",\n" +
                    "\"is_privacy_policy\": 0\n" +
                    "}",config.getOpenAPIUrl()+"/api/material/material",headerMap);
            Assert.assertEquals(JSONObject.parseObject(createFile).getString("code"),0,"文件创建失败："+JSONObject.parseObject(createFile).getJSONArray("message"));
        }else {
            fileName = items.getJSONObject(0).getString("name");
            fileId = items.getJSONObject(0).getString("jing_uuid");
        }
        Map<String,String> fileMap = new HashMap();
        fileMap.put("fileName",fileName);
        fileMap.put("fileId",fileId);
        return fileMap;
    }

    /**
     * @param feature  rbac 1.0 1.9的feature名称
     * @param featureCode  rbac 2.0的featurecode名称
     * @return true or false
     * @description  判断权限
     */

    public boolean hasFeature(String feature,String featureCode) throws URISyntaxException, IOException {
        boolean hasFeature = false;
        String result = requestBaseApi.doGet(config.getOpenAPIUrl()+"/api/omniuser/rbacv2/customers/getRoleFeatureCodeList",headerMap,"");
        int rbac_version = JSONObject.parseObject(JSONObject.parseObject(result).getString("data")).getInteger("rbac_version");
        String feature_code = JSONObject.parseObject(JSONObject.parseObject(result).getString("data")).getString("feature_codes");
        String[] feature_codes = feature_code.substring(2,feature_code.length()-2).split("\",\"");
        switch (rbac_version){
            case 1:
                for(int i=0;i<feature_codes.length;i++){
                    if(feature.equals(feature_codes[i])){
                        hasFeature = true;
                        break;
                    }else
                        hasFeature = false;
                }
                break;
            case 2:
                for(int i=0;i<feature_codes.length;i++){
                    if(featureCode.equals(feature_codes[i])){
                        hasFeature = true;
                        break;
                    }else
                        hasFeature = false;
                }
        }
        return hasFeature;
    }

    /**
     * @param codes  custom field 的code,传参eg:new String[]{"\"mobile\"","\"email\""}
     * @return custom field的id
     * @description  根据custom field 的code获取对应的custom field id
     */

   public String[] getcustomfieldmessagebycodes(String[] codes) throws URISyntaxException, IOException {
       String[] customfieldId = new String[codes.length];
       String resultStr = requestBaseApi.doPost("{\n" +
               "\"codes\": "+Arrays.toString(codes)+"\n" +
               "}",config.getOpenAPIUrl()+"/api/follower/followereavs/getcustomfieldmessagebycodes",headerMap);
       Object result = JSONObject.parseObject(resultStr);
       if(JSONObject.parseObject(resultStr).getString("code").equals("0")){
           for(int i=0;i<customfieldId.length;i++){
               customfieldId[i] = JsonPath.read(result,"$.data.datas."+codes[i].replaceAll("\"","")+".id");
           }
       }else{
           Log.info("获取custom field接口请求报错:"+JSONObject.parseObject(resultStr).getString("msg"));
       }
       return customfieldId;
   }

   public String getsurveySignature(String timestamp,String surveyid,String channal_user_id){
       TinyTools tool=new TinyTools();
       String signature;
       signature=timestamp+surveyid+"359a94ee7a3a35dee508af942072f9df"+channal_user_id;
       signature = tool.Encrypt(signature, "MD5");
       return signature;
   }

    }

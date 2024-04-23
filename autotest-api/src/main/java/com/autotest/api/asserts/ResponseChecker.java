package com.autotest.api.asserts;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ResponseChecker {

    private static ResponseChecker instance = null;
    private static Logger logger=Logger.getLogger(ResponseChecker.class);

    public ResponseChecker(){
    }

    //当有多个线程并行调用 getInstance() 的时候，就会创建多个实例
    public static ResponseChecker getInstance() {
        if (instance == null) {
            instance = new ResponseChecker();
        }
        return instance;
    }




    /**
     * @author guoyan
     * @param code HttpResponse返回状态码
     * @return 返回boolean，断言状态
     */
   //code部分的断言在com.jingsocial.api.base的RequestBaseApi里已经有了
    /* public boolean codeCheck(int code){
        if(response == null){
            return false;
        }
        try {
            Assert.assertEquals(code, response.getStatusLine().getStatusCode());
            return true;
        }catch (Error e){
            e.printStackTrace();
            return false;
        }
    }*/

   /**
     * @author guoyan
     * @param exceptString 期望的数据
     * @return 返回boolean，断言状态
     */
    public boolean dataCheck(String exceptString,String response) throws IOException {

        if(exceptString.equals("")){
            logger.info("期望值为空，跳过自动校验，请确认！");
            return true;
        }

        if(response.equals("")){
            logger.error("实际响应值为空，跳过自动校验，请确认！");
            return false;
        }

        Map<String,Object> actualMap = parse(response, null);
        Map<String,Object> exceptMap = parse(exceptString, null);

        if(exceptMap.isEmpty()){
            logger.error("期望值格式可能有误，解析为空，请检查！");
            return false;
        }

        //System.out.println("期望值map:"+exceptMap);
       // System.out.println("实际响应map:"+actualMap);
        boolean flag = true;
        for(Map.Entry<String, Object> item : exceptMap.entrySet()){
           // logger.info(item.getKey() + " 【expected】: " + item.getValue() + "，and【actual】：" + actualMap.get(item.getKey()));
            String actualvalue="";
            try{
                actualvalue=actualMap.get(item.getKey()).toString();
            }catch(NullPointerException e){
                flag = false;
                logger.error("找不到期望检查的key: "+item.getKey());
                Reporter.log("找不到期望检查的key: "+item.getKey());
                continue;
            }

            try {
                Assert.assertEquals(item.getValue().toString(),actualvalue );
            }catch (Error e){
                flag = false;
                logger.error(item.getKey() + " 【expected】: " + item.getValue() + "，and【actual】：" + actualvalue);
                Reporter.log(item.getKey() + " 【expected】: " + item.getValue() + "，and【actual】：" + actualvalue);
            }
        }

       // Assert.assertTrue(flag);//最后false就中断本次测试
        return flag;
    }

    /**
     * @author guoyan
     * @param json 待解析的json
     * @param parent 父key
     * @return 返回一个map，key按json的路径层次存储
     */

    //{"poster":"{\"post_image\":\"\\/upload\\/images\\/164007328762237.png\",\"qrcode\":{\"size\":430,\"x\":166,\"y\":496}}"}
    //上面这种也能转义成poster.post_image的形式


    public static Map<String, Object> parse(Object json, String parent) {
        String jsonStr = String.valueOf(json);
       // if (json == null || jsonStr.equals("")) {
        if (json == null) {
            System.err.println("parse exception, json is null.");
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        //&&(!jsonStr.contains("{}{}"))
        if (jsonStr.startsWith("{")) {//jsonObject
            try{

                JSONObject obj = JSON.parseObject(jsonStr);
                Set<Map.Entry<String, Object>> items = obj.entrySet();
                if (items == null || items.size() < 1) {
                    System.err.println("parse exception, json object is null.");
                    return map;
                }

                parent = parent == null || parent.equals("") ? "" : parent + ".";
                for (Map.Entry<String, Object> item : items) {
                    //System.out.println(parent + item.getKey() + ":" + item.getValue());
                    Map<String, Object> temp = parse(item.getValue(), parent + item.getKey());
                    if (temp != null && temp.size() > 0) {
                        map.putAll(temp);
                    }
                }
            }catch (Exception e){
                return map;
            }
    //&&(!jsonStr.contains("[][]"))&&jsonStr.contains(":")
        } else if ((jsonStr.startsWith("["))) {//jsonArray
            try {

                JSONArray array = JSON.parseArray(jsonStr);
                if (array == null || array.size() < 1) {
                    System.err.println("parse exception, json array is null.");
                    return map;
                }
                int index = 0;
                String tempParent = "";
                for (Object child : array.toArray()) {
                    tempParent = parent == null || "".equals(parent) ? "" : parent + "[" + index + "]";
                    Map<String, Object> temp = parse(child, tempParent);
                    if (temp != null && temp.size() > 0) {
                        map.putAll(temp);
                    }
                    index++;
                }
            }catch (Exception e){
                return map;
            }

        } else {//unknown or item
            if (parent != null) {//item
                map.put(parent, json);
            }else{//unknown
                map.put("", json);
            }
        }
        return map;
    }
    /**判断String字符串是否为Json格式**/
    public static boolean isJson(String json) {
        if (StringUtils.isBlank(json)) {
            logger.error("待解析的json字符串为空！");
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            logger.error("待解析的json字符串不符合json格式！");
            return false;

        }






    }
}

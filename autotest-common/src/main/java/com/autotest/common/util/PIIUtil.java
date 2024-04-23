package com.autotest.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.autotest.api.base.TestNgConfig;

import java.util.HashMap;
import java.util.Map;

public class PIIUtil {
    RequestBaseApi requestBaseApi=new RequestBaseApi();
    TestNgConfig config=TestNgConfig.getInstance();
    String response="";

    public Boolean CheckMidPiiFeature(String mid, HashMap headerMap)
    {
        try {
            response=requestBaseApi.doGet(config.getOpenAPIUrl()+"/api/user/tenants/"+mid,headerMap,"");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(response.contains("Data Security Setting|数据安全设置"))
            return true;
        else
        return false;
    }

    public String CapthaValueThroughPIISetting(String value,String code,HashMap headerMap){
    String str="";
       Boolean Check= CheckMidPiiFeature(config.getMid(),headerMap);
       if(Check){
           Map captchashow= captchaMethod(code,headerMap);
           if(captchashow.containsKey("show_format")){
               if(captchashow.get("show_format").equals("origin"))
                   return value;
               if(captchashow.get("show_format").equals("captcha"))
               {
                   String captcha="";
                   String captchaformat=captchashow.get("captcha_format").toString();
                   if(captchaformat.equals("all")){
                       for(int num=0;num<value.length();num++){
                           captcha=captcha+"*";
                       }
                       value=captcha;
                       return value;
                   }
                   else{
                       int captchalen=Integer.parseInt(captchashow.get("captcha_length").toString());
                       for(int num=0;num<captchalen;num++){
                           captcha=captcha+"*";
                       }
                       switch(captchaformat){
                           case "prefix":
                               if(value.length()>=captchalen)
                                   value=captcha+value.substring(captchalen,value.length());
                               else
                                   value=captcha.substring(0,value.length());
                               break;

                           case "middle":
                               if(value.length()>=captchalen){
                                   int start;
                                   if((value.length()-captchalen)%2!=0)
                                   start=(value.length()-captchalen)/2+1;
                                   else
                                       start=(value.length()-captchalen)/2;

                                   value=value.substring(0,start)+captcha+value.substring(captchalen+start,value.length());
                               } else
                                   value=captcha.substring(0,value.length());
                               break;

                           case "endfix":
                               if(value.length()>=captchalen){
                                   value=value.substring(0,value.length()-captchalen)+captcha;
                               } else
                                   value=captcha.substring(0,value.length());
                               break;
                       }
                   }
                   return value;
               }
           }
               else
                   return value;
       }
       return str;

    }

    public Map captchaMethod(String code,HashMap headerMap){
        Map codeshowmap=new HashMap();
        JSONObject jsonobj=null;
        try {
            response=requestBaseApi.doGet(config.getOpenAPIUrl()+"/api/omniuser/pii/rulelist?page=1",headerMap,"");
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONArray jsonarr= JSONObject.parseObject(response).getJSONObject("data").getJSONArray("items");
        for(int i=0;i<jsonarr.size();i++){
            if(jsonarr.getJSONObject(i).getString("field_cache_code").equals(code)){
                jsonobj=jsonarr.getJSONObject(i).getJSONObject("pii_show_config");
                codeshowmap.put("show_format",jsonobj.getString("show_format"));
                if(jsonobj.containsKey("captcha_format"))
                codeshowmap.put("captcha_format",jsonobj.getString("captcha_format"));
                if(jsonobj.containsKey("captcha_length"))
                codeshowmap.put("captcha_length",jsonobj.getString("captcha_length"));
                break;
            }
        }
        return codeshowmap;
    }
}

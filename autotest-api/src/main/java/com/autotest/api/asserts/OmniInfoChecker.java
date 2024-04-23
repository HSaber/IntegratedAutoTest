package com.autotest.api.asserts;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import com.autotest.api.base.TestNgConfig;
import com.autotest.api.util.RequestBaseApi;
import net.minidev.json.JSONValue;

import java.util.HashMap;
import java.util.Map;

public class OmniInfoChecker {
    RequestBaseApi requestBaseApi=new RequestBaseApi();
    TestNgConfig config=TestNgConfig.getInstance();

    /**
     *
     * @param omniuserid 全渠道用户id
     * @param tagids  标签id 逗号隔开 一次性查询多个值
     * @param wait 等待时长(秒)
     * @param headerMap 请求头信息
     * @return map格式返回（key:tagid，value:tagid count）
     * @throws Exception
     */
    public Map CheckTagCount(String omniuserid, String tagids, int wait,HashMap headerMap)throws Exception{
        Thread.sleep(wait*1000);
        Map tagusermap=new HashMap();
        String[] tagidarr=tagids.split(",");
        String useridjson="{\"id\": \""+omniuserid+"\"}";
        String tagsresponse=requestBaseApi.doPost(useridjson,config.getOpenAPIUrl()+"/api/omniuser/profile/user/"+omniuserid,headerMap);
        JSONArray tagsjsonarr=JSONObject.parseObject(tagsresponse).getJSONObject("data").getJSONObject("items").getJSONArray("Tags");
        int i;
        for(int j=0;j<tagidarr.length;j++){
            if(tagsjsonarr.size()==0)
                tagusermap.put(tagidarr[j],0);
            else {
                for (i = 0; i < tagsjsonarr.size(); i++) {
                    String tagidres=tagsjsonarr.getJSONObject(i).getString("id");
                    if (tagidarr[j].equals(tagidres)) {
                        tagusermap.put(tagidarr[j],tagsjsonarr.getJSONObject(i).getString("count"));
                        break;
                    }
                }
                if(!tagusermap.containsKey(tagidarr[j]))
                    tagusermap.put(tagidarr[j],0);
            }

        }
        return tagusermap;
    }

    /**
     *
     * @param omniuserid 全渠道用户id
     * @param customfieldids  资料id,逗号隔开，允许查询多条记录
     * @param wait 等待时长(秒)
     * @param headerMap 请求头信息
     * @return map格式返回（key:fieldid，value:字段值）
     * @throws Exception
     */
    public Map CheckCustomFieldValue(String omniuserid,String customfieldids,int wait,HashMap headerMap) throws Exception{
        String thirdfields;
        Boolean thirdflag=false;
        JSONArray thirdfieldobj=null;
        Thread.sleep(wait*1000);
        Map customfieldmap=new HashMap();
        String[] fieldarr=customfieldids.split(",");
        String useridjson="{\"id\": \""+omniuserid+"\"}";
        String fieldresponse=requestBaseApi.doPost(useridjson,config.getOpenAPIUrl()+"/api/omniuser/profile/user/"+omniuserid,headerMap);
        Object fielddoc=JSONValue.parse(fieldresponse);
        String fields=JsonPath.parse(fielddoc).read("$.data.items.Fields").toString();
        if(JSONObject.parseObject(fieldresponse).getJSONObject("data").getJSONObject("items").containsKey("thirdparty_fields"))
        {
            thirdfields=JsonPath.parse(fielddoc).read("$.data.items.thirdparty_fields").toString();
            thirdfieldobj=JSONArray.parseArray(thirdfields);
        thirdflag=true;
        }
        JSONObject fieldsobj=JSONObject.parseObject(fields);
        HashMap map = JSONObject.parseObject(fields, HashMap.class);
        HashMap keymap=null;
        int i,j,n;
        String fieldid,fieldvalue;
        for(int m=0;m<fieldarr.length;m++){
            for (Object key : map.keySet()) {
                if(key.toString().equals("Basic Fields")){
                    String bcfields=JsonPath.parse(fielddoc).read("$.data.items.Fields.['"+key.toString()+"'][0]").toString();
                    JSONObject bsfieldsobj=JSONObject.parseObject(bcfields);
                    keymap=JSONObject.parseObject(bcfields,HashMap.class);
                    for (Object bskey : keymap.keySet()) {
                        JSONObject corefield=bsfieldsobj.getJSONObject(bskey.toString());
                        fieldid=corefield.getString("id");
                        if(fieldarr[m].equals(fieldid)){
                            customfieldmap.put(fieldarr[m],corefield.getString("value"));
                            break;
                        }
                    }
                }
                JSONArray singlefieldcat=fieldsobj.getJSONArray(key.toString());
                for(i=0;i<singlefieldcat.size();i++){
                     fieldid=singlefieldcat.getJSONObject(i).getString("id");
                     fieldvalue=singlefieldcat.getJSONObject(i).getString("value");
                    if(fieldarr[m].equals(fieldid)){
                        customfieldmap.put(fieldarr[m],fieldvalue);
                        break;
                    }
                }
                if(customfieldmap.containsKey(fieldarr[m]))
                    break;
            }
            if(!customfieldmap.containsKey(fieldarr[m])&&thirdflag){
                for (n=0;n<thirdfieldobj.size();n++) {
                    JSONArray singlefieldcat=thirdfieldobj.getJSONObject(n).getJSONArray("data");
                    for(j=0;j<singlefieldcat.size();j++){
                        String thirdfieldid=singlefieldcat.getJSONObject(j).getString("id");
                        String thirdfieldvalue=singlefieldcat.getJSONObject(j).getString("value");
                        if(fieldarr[m].equals(thirdfieldid)){
                            customfieldmap.put(fieldarr[m],thirdfieldvalue);
                            break;
                        }
                    }
                    if(customfieldmap.containsKey(fieldarr[m]))
                        break;
                }
            }
            if(!customfieldmap.containsKey(fieldarr[m]))
                customfieldmap.put(fieldarr[m],"");
        }
        return customfieldmap;
    }
}

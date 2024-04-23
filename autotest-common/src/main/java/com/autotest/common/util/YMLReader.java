package com.autotest.common.util;

import com.alibaba.fastjson.JSON;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YMLReader {
    public static void main(String[] args) {

        YMLReader.getYMLData("/testData/testCustomfield/1111.yml");
    }

    public static Object[][] getYMLData(String path){
        Map<String,Object> obj =null;
        try{
            Yaml yaml = new Yaml();
            InputStream resourceAsStream = YMLReader.class.getResourceAsStream(path);
            obj = (Map) yaml.load(resourceAsStream);

        }catch(Exception e){
            e.printStackTrace();
        }

        ArrayList<Object> testCases=(ArrayList<Object>)obj.get("test");
        Object[][] results = new Object[testCases.size()][];
        for (int i = 0; i < testCases.size(); i++) {
            Map<String,Object> testcase=(Map<String,Object>)testCases.get(i);
            Object[] caseField=new Object[6];
            caseField[0]=testcase.get("caseNum")+"";
            caseField[1]=testcase.get("caseTitle")+"";
            caseField[2]=testcase.get("request");
            caseField[3]=testcase.get("validate");
            caseField[4]=testcase.get("varialbles");
            caseField[5]=(boolean)testcase.get("status")? "yes":"no";

            results[i]=caseField;

        }

        return results;
    }

    public static Map<String,String> execute(OperateFile deal, Map variablesMap, String caseNum, String caseTitle,
                                             Map request, Map validate, Map varialbles,
                                             String status)throws Exception{

        Map url = (Map) request.get("url");
        String host=url.get("host")==null?"":url.get("host")+"";
        String path = url.get("path") + "";
        String query =url.get("query")==null? "": url.get("query") + "";

        String requestparamType = request.get("paramType") + "";
        String header=request.get("headers")==null?"":request.get("headers")+"";
        String requestBody ="";
        requestBody =request.get("body")==null ? "": request.get("body") + "";
        if(request.get("body")==null){
            requestBody="";
        }else{
            if(requestparamType.toLowerCase().contains("json")){
                requestBody=JSON.toJSONString((Map)request.get("body"));
            }else{
                requestBody=request.get("body") + "";
            }

        }
        String requestMethod=request.get("method")+"";

        String varAccess= varialbles.get("replace")==null? "" : varialbles.get("replace")+"";
        String varStore=varialbles.get("store")==null ?"": varialbles.get("store")+"";
        String expectJson=validate.get("jsonequal")==null?"":JSON.toJSONString((Map)validate.get("jsonequal"));

       String[] dealresult= deal.dealExcelVatiables(variablesMap, caseNum+"", caseTitle,host,header,
                path, requestMethod,  requestparamType,
                query, requestBody, expectJson,
                varAccess, varStore, status);

        Map<String,String> postProcess=new HashMap<String,String>();
        postProcess.put("response",dealresult[0]);
        postProcess.put("expectJson",dealresult[1]);
        postProcess.put("requestBody",dealresult[2]);

        return postProcess;
    }
}


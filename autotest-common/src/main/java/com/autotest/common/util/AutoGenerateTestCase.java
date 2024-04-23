package com.autotest.common.util;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class AutoGenerateTestCase {
    private static Logger logger = Logger.getLogger(AutoGenerateTestCase.class);


    public static void main(String args[]) throws Exception{
        String filePath="/testData/test.xlsx";
        String json="{\n" +
                "    \"api\":[\n" +
                "        {\n" +
                "            \"url\":\"api/user/admins/demo/{id}/{openid}?page={page}&sort={sort}\",\n" +
                "            \"type\":\"post\",\n" +
                "            \"title\":\"create admin 创建admin\",\n" +
                "            \"group\":\"admin\",\n" +
                "            \"parameter\":{\n" +
                "                \"fields\":{\n" +
                " \"Params\":[\n" +
                "                        {\n" +
                "                            \"group\":\"varible\",\n" +
                "                            \"type\":\"integer\",\n" +
                "                            \"mock_type\":\"tagid\",\n" +
                "                            \"optional\":false,\n" +
                "                            \"field\":\"id\",\n" +
                "                            \"description\":\"tagid\"\n" +
                "                        }, {\n" +
                "                            \"group\":\"varible\",\n" +
                "                            \"type\":\"string\",\n" +
                "                            \"mock_type\":\"openid\",\n" +
                "                            \"optional\":false,\n" +
                "                            \"field\":\"openid\",\n" +
                "                            \"description\":\"openid\"\n" +
                "                        }, {\n" +
                "                            \"group\":\"varible\",\n" +
                "                            \"type\":\"integer\",\n" +
                "                            \"mock_type\":\"fieldid\",\n" +
                "                            \"optional\":false,\n" +
                "                            \"field\":\"fieldid\",\n" +
                "                            \"description\":\"fieldid\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"group\":\"Path\",\n" +
                "                            \"type\":\"integer\",\n" +
                "                            \"mock_type\":\"tagid\",\n" +
                "                            \"optional\":false,\n" +
                "                            \"field\":\"page\",\n" +
                "                            \"description\":\"page number\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"group\":\"Path\",\n" +
                "                            \"type\":\"string\",\n" +
                "                            \"mock_type\":\"enum\",\n" +
                "                            \"allowedValues\":[\n" +
                "                                \"male\",\n" +
                "                                \"female\"\n" +
                "                            ],\n" +
                "                            \"optional\":false,\n" +
                "                            \"field\":\"sort\",\n" +
                "                            \"description\":\"sort field\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Body\":[\n" +
                "                        {\n" +
                "                            \"type\":\"string\",\n" +
                "                            \"optional\":false,\n" +
                "                            \"field\":\"name\",\n" +
                "                            \"size\":\"1~10\",\n" +
                "                            \"pattern\":\"[a-z0-9]{8,64}\",\n" +
                "                            \"description\":\"用户名称 | required | [a-z0-9]{8,64}\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"type\":\"boolean\",\n" +
                "                            \"optional\":false,\n" +
                "                            \"field\":\"subscribe\",\n" +
                "                            \"defaultValue\":true,\n" +
                "                            \"description\":\"是否关注 | required\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"type\":\"array\",\n" +
                "                            \"optional\":false,\n" +
                "                            \"field\":\"tagid1\",\n" +
                "                            \"mock_type\":\"tagid\",\n" +
                "                            \"defaultValue\":true,\n" +
                "                            \"description\":\"是否关注 | required\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"type\":\"int\",\n" +
                "                            \"optional\":false,\n" +
                "                            \"field\":\"tagid\",\n" +
                "                            \"mock_type\":\"tagid\",\n" +
                "                            \"defaultValue\":true,\n" +
                "                            \"description\":\"是否关注 | required\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        AutoGenerateTestCase auto=new AutoGenerateTestCase();
        auto.generateTestCaseExcel(json,filePath);
    }

   public void generateTestCaseExcel(String json,String filePath) throws Exception{

        List<Map<String,String>> mapList=generateTestCase(json);
       String pathBase=System.getProperty("user.dir")+"/src/main/resources/";
        File file=new File(pathBase+filePath);
        if(file.exists()){
            updateAutoTestExcel(mapList,filePath);
        }else{
            createAutoTestExcel(mapList,filePath);
        }

    }

    public  List<Map<String,String>> generateTestCase(String json) throws Exception{
        List<Map<String, String>> mapList = new ArrayList<>();
        int apiNum = JsonPath.parse(json).read("$.api.length()");
        //遍历每个api文档
        for(int i=0;i<apiNum;i++){
            //获取requestURL
            String requestURL="/"+JsonPath.parse(json).read("$.api["+i+"].url");
            //获取requestType
            String requestType=JsonPath.parse(json).read("$.api["+i+"].type");
            if(requestType.equals("get")){
                requestType="GET";
            }else if(requestType.equals("post")){
                requestType="POST";
            }else if(requestType.equals("put")){
                requestType="PUT";
            }else if(requestType.equals("delete")){
                requestType="DELETE";
            }

            if(requestURL.contains("?")&&!requestURL.contains("/{")){
                requestURL=requestURL.substring(0,requestURL.indexOf("?"));

            }else if(requestURL.contains("/{")){
                requestURL=requestURL.substring(0,requestURL.indexOf("/{"));

            }

            String testcaseBase=JsonPath.parse(json).read("$.api["+i+"].group")+"--"+JsonPath.parse(json).read("$.api["+i+"].title")+"--";
            //验证body里的字段时，url里必须填的有效值
            String testdataURLBase="",testdataURLBaseUrl="",testdataURLBasePath="";
            //testcase描述(url部分)
            ArrayList<String> testCase = new ArrayList<>();
            //所有异常的url部分测试数据
            ArrayList<String> testdataURL = new ArrayList<>();
            //添加可能用到的Variables(access)变量声明
            ArrayList<String> pathVatiables = new ArrayList<>();
            //处理path参数必填项
            try{
                int pathNum=JsonPath.parse(json).read("$.api["+i+"].parameter.fields.Params.length()");
                //只存必填项有效的信息fieldName=有效值
                    ArrayList<String> urlMustInfo = new ArrayList<>();
                ArrayList<String> pathMustInfo = new ArrayList<>();
                ArrayList<String> urlFieldMust = new ArrayList<>();
                ArrayList<String> pathFieldMust1 = new ArrayList<>();
                    for(int p=0;p<pathNum;p++){
                        boolean optional=JsonPath.parse(json).read("$.api["+i+"].parameter.fields.Params["+p+"].optional");
                        //如果是必填字段
                        if(!optional){
                            String fieldName=JsonPath.parse(json).read("$.api["+i+"].parameter.fields.Params["+p+"].field");
                            String fieldType=JsonPath.parse(json).read("$.api["+i+"].parameter.fields.Params["+p+"].type");
                            String fieldGroup=JsonPath.parse(json).read("$.api["+i+"].parameter.fields.Params["+p+"].group");

                            //参数是跟在?后面，以key=value形式传值
                            if(fieldGroup.equals("Path")){
                                urlFieldMust.add(fieldName);
                                if(fieldType.equals("string")){
                                    urlMustInfo.add(fieldName+"="+fieldName+"_AutoGenerate_$mock_autotime");
                                    pathVatiables.add("$mock_autotime");

                                }else if(fieldType.equals("integer")||fieldType.equals("int")){
                                    urlMustInfo.add(fieldName+"="+"1");
                                }else if(fieldType.equals("boolean")){
                                    urlMustInfo.add(fieldName+"="+"true");
                                }
                            }else{
                                //path里的参数，应该都是有mock_type字段的，且都是必填的
                                pathFieldMust1.add(fieldName);
                                try{
                                String mockType=JsonPath.parse(json).read("$.api["+i+"].parameter.fields.Params["+p+"].mock_type");

                                switch (mockType){
                                    case "tagid":
                                        pathMustInfo.add("$mock_tagid");
                                        pathVatiables.add("$mock_tagid");
                                        break;
                                    case "openid":
                                        pathMustInfo.add("$mock_openid");
                                        pathVatiables.add("$mock_openid");
                                        break;
                                    case "fieldid":
                                        pathMustInfo.add("$mock_fieldid");
                                        pathVatiables.add("$mock_fieldid");
                                        break;
                                }
                                }catch (Exception e){
                                    logger.error("该url字段没有mock_type属性！");
                                }

                            }

                        }

                    }
                    int pathMustInfoSize=pathMustInfo.size();
                    if(pathMustInfoSize>0){
                    testdataURLBasePath="/"+pathMustInfo.get(0);
                    for(int pa=1;pa<pathMustInfoSize;pa++) {
                        testdataURLBasePath = testdataURLBasePath + "/" + pathMustInfo.get(pa);

                    }
                }

                int mustInfoSize=urlMustInfo.size();
                if(mustInfoSize>0){
                    testdataURLBaseUrl="?"+urlMustInfo.get(0);
                    for(int m=1;m<mustInfoSize;m++){
                        testdataURLBaseUrl=testdataURLBaseUrl+"&"+urlMustInfo.get(m);

                    }

                }

                    if(pathMustInfoSize>0){

                        for(int pa=0;pa<pathMustInfoSize;pa++) {
                            String musttemp ="";
                            ArrayList<String> removeMust = new ArrayList<>();
                            removeMust.addAll(pathMustInfo);
                            removeMust.set(pa,"");
                            for (int r = 0; r < removeMust.size(); r++) {
                                    musttemp = musttemp + "/" + removeMust.get(r);
                                }
                                testCase.add(testcaseBase + "验证path必填项(不填)--" + pathFieldMust1.get(pa));
                                testdataURL.add(musttemp+testdataURLBaseUrl);

                        }
                }


                    if(mustInfoSize>0){
                        for(int m=0;m<mustInfoSize;m++){
                            ArrayList<String> removeMust=new ArrayList<>();
                            removeMust.addAll(urlMustInfo);
                            removeMust.remove(m);
                            if(removeMust.size()>0){
                                String musttemp="?"+removeMust.get(0);
                                for(int r=1;r<removeMust.size();r++){
                                    musttemp=musttemp+"&"+removeMust.get(r);
                                }
                                testCase.add(testcaseBase+"验证path必填项(为空)--"+urlFieldMust.get(m));
                                testdataURL.add(testdataURLBasePath+musttemp+"&"+urlFieldMust.get(m)+"= ");
                                testCase.add(testcaseBase+"验证path必填项(不填)--"+urlFieldMust.get(m));
                                testdataURL.add(testdataURLBasePath+musttemp);
                            }else{
                                testCase.add(testcaseBase+"验证path必填项(不填)--"+urlFieldMust.get(0));
                                testCase.add(testcaseBase+"验证path必填项(为空)--"+urlFieldMust.get(0));
                                testdataURL.add(testdataURLBasePath+"");
                                testdataURL.add(testdataURLBasePath+"?"+urlFieldMust.get(0)+"= ");
                            }


                        }
                    }

                testdataURLBase=testdataURLBasePath+testdataURLBaseUrl;





            }catch(Exception e){
                logger.info("该api没有url参数！");
            }


            //处理body部分参数
            ArrayList<String> testdataBody = new ArrayList<>();
            ArrayList<String> testCaseBody = new ArrayList<>();
            ArrayList<String> bodyVaribles=new ArrayList<>();
            String testdataBodyBase="";
            try{
                int bodyNum=JsonPath.parse(json).read("$.api["+i+"].parameter.fields.Body.length()");
                Map<String,Object> bodyTestData=new HashMap<>();

                for(int b=0;b<bodyNum;b++){
                    boolean bmust=JsonPath.parse(json).read("$.api["+i+"].parameter.fields.Body["+b+"].optional");
                    if(!bmust){
                        String fieldName=JsonPath.parse(json).read("$.api["+i+"].parameter.fields.Body["+b+"].field");
                        String fieldType=JsonPath.parse(json).read("$.api["+i+"].parameter.fields.Body["+b+"].type");
                        try{
                            String mockType=JsonPath.parse(json).read("$.api["+i+"].parameter.fields.Body["+b+"].mock_type");
                            switch (mockType){
                                case "tagid":
                                    if(fieldType.equals("array")){
                                        ArrayList<String> fieldValue=new ArrayList<>();
                                        fieldValue.add("$mock_tagid");
                                        bodyTestData.put(fieldName,fieldValue);
                                        bodyVaribles.add("$mock_tagid");
                                    }else{
                                        bodyTestData.put(fieldName,"$mock_tagid");
                                        bodyVaribles.add("$mock_tagid");

                                    }
                                    break;
                                case "enum":
                                    String enum1=JsonPath.parse(json).read("$.api["+i+"].parameter.fields.Body["+b+"].allowedValues[0]");
                                    bodyTestData.put(fieldName,enum1);

                            }
                        }catch (Exception e){
                            logger.info("该body字段没有mock_type属性");
                            if(fieldType.equals("string")){
                                bodyTestData.put(fieldName,fieldName+"_AutoGenerate_$mock_autotime");
                                bodyVaribles.add("$mock_autotime");
                            }else if(fieldType.equals("boolean")){
                                bodyTestData.put(fieldName,true);
                            }

                        }
                    }else{
                        //body非必填项部分
                    }
                }

                JSONObject jsonObj = new JSONObject(bodyTestData);
                testdataBodyBase=jsonObj.toString();

                for(Iterator<Map.Entry<String,Object>> it=bodyTestData.entrySet().iterator();it.hasNext();){
                    Map<String,Object> temp=new HashMap<>();
                    Map<String,Object> tempClear=new HashMap<>();
                    temp.putAll(bodyTestData);
                    tempClear.putAll(bodyTestData);
                    Map.Entry<String,Object> item=it.next();
                    String key=item.getKey();
                    temp.remove(key);
                    tempClear.put(key,"");
                    testdataBody.add((new JSONObject(temp)).toString());
                    testCaseBody.add(testcaseBase+"验证body必填字段(不填)--"+key);
                    testdataBody.add((new JSONObject(tempClear)).toString());
                    testCaseBody.add(testcaseBase+"验证body必填字段(为空)--"+key);
                }


            }catch(Exception e){
                logger.info("该api没有body参数！");
            }

            //处理请求传参类型
            String testdataType="";
            if(requestType.equals("GET")){
                testdataType="Query Params";
            }else if(requestType.equals("POST")){
                testdataType="path&json";
            }

            //处理variables(access)
            ArrayList<String> variablesAcess = new ArrayList<>();



            for(int listnum=0;listnum<testdataURL.size();listnum++){
                Map<String,String> map=new HashMap<>();
                //"NO.","Variables(store)","Active"在写excel的时候处理
                map.put("Test Case",testCase.get(listnum));
                map.put("Request URL",requestURL);
                map.put("Request Type",requestType);
                map.put("Test Data Type",testdataType);
                map.put("Test Data(url)",testdataURL.get(listnum));
                map.put("Test Data(body)",testdataBodyBase);
                map.put("Expected Result","url缺必填参数");
                map.put("Variables(access)",StringUtils.join(pathVatiables,","));
                mapList.add(map);
            }

            for(int listnum=0;listnum<testdataBody.size();listnum++){
                Map<String,String> map=new HashMap<>();
                //"NO.","Variables(store)","Active"在写excel的时候处理
                map.put("Test Case",testCaseBody.get(listnum));
                map.put("Request URL",requestURL);
                map.put("Request Type",requestType);
                map.put("Test Data Type",testdataType);
                map.put("Test Data(url)",testdataURLBase);
                map.put("Test Data(body)",testdataBody.get(listnum));
                map.put("Expected Result","body缺必填参数");
                map.put("Variables(access)",StringUtils.join(bodyVaribles,","));
                mapList.add(map);
            }
        }

        return mapList;

    }

    public void createAutoTestExcel(List<Map<String,String>> mapList,String filePath) throws IOException{
        //自动生成测试用例，创建excel
        String[] title={"NO.","Test Case","Request URL","Request Type","Test Data Type","Test Data(url)","Test Data(body)","Expected Result","Variables(access)","Variables(store)","Active"};
        int[] width={8,40,25,12,14,25,25,25,25,14,8};
        Workbook wb=new XSSFWorkbook();
        CellStyle style=wb.createCellStyle();
        Font font=wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        Sheet sheet=wb.createSheet();
        sheet.setDefaultRowHeightInPoints(15);

        Row row=sheet.createRow(0);
        for(int t=0;t<title.length;t++){
            Cell cell=row.createCell(t);
            cell.setCellValue(title[t]);
            cell.setCellStyle(style);
            sheet.setColumnWidth(t, width[t]*256);
        }
        int td=1;
        for (Map<String, String> map : mapList) {
            Row row1=sheet.createRow(td++);
            Cell cell0=row1.createCell(0);
            cell0.setCellValue(td-1);
            Cell cell10=row1.createCell(10);
            cell10.setCellValue("Yes");
            for(int c=1;c<9;c++){
                String mapKey = row.getCell(c).toString().trim();
                Cell cell=row1.createCell(c);
                cell.setCellValue(map.get(mapKey));
            }


        }

        try
        {
            String pathBase=System.getProperty("user.dir")+"/src/main/resources/";

            FileOutputStream out = new FileOutputStream(pathBase+filePath);
            wb.write(out);
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateAutoTestExcel(List<Map<String,String>> mapList,String filePath) throws IOException {

        String pathBase=System.getProperty("user.dir")+"/src/main/resources/";
        Workbook wb=new XSSFWorkbook(new FileInputStream(pathBase+filePath));
        CellStyle style=wb.createCellStyle();
        Font font=wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        Sheet sheet=wb.getSheetAt(0);

        Row titleRow=sheet.getRow(0);
        int rowNum=sheet.getLastRowNum()+1;

        for (Map<String, String> map : mapList) {
            Row row1=sheet.createRow(rowNum++);
            Cell cell0=row1.createCell(0);
            cell0.setCellValue(rowNum-1);
            Cell cell10=row1.createCell(10);
            cell10.setCellValue("Yes");
            for(int c=1;c<9;c++){
                String mapKey = titleRow.getCell(c).toString().trim();
                Cell cell=row1.createCell(c);
                cell.setCellValue(map.get(mapKey));
            }


        }

        try
        {
                FileOutputStream out = new FileOutputStream(pathBase+filePath);
            wb.write(out);
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}

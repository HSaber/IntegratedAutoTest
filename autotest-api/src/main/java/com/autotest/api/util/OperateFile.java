package com.autotest.api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.autotest.api.base.TestBase;
import com.autotest.api.base.TestNgConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;
import org.testng.SkipException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


public class OperateFile extends TestBase {
    private static Logger logger = Logger.getLogger(OperateFile.class);
    public RequestBaseApi requestBase;
    //response,expectData,testBodyData
    String[] response={"", "",""};
    String inteURL;
    HashMap<String, String> inteHeaderMap;
    //获取功能配置或者方法
    TestNgConfig config = TestNgConfig.getInstance();

    public static Object[][] getCSVData(String FileName) throws IOException {

        String record;
        List<Object[]> records = new ArrayList<Object[]>();
       /* CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(FileName), "UTF-8"), ',',CSVWriter.NO_QUOTE_CHARACTER);
        reader.readNext();   //用readnext读取之后就不存在于stream中了
        List<String[]> records=reader.readAll();*/
        //设定UTF-8字符集，使用带缓冲区的字符输入流BufferedReader读取文件内容
        BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(FileName), "UTF-8"));
        //忽略读取CSV文件的标题行（第一行）
        file.readLine();
        //遍历读取文件中除第一行外的其他所有内容并存储在名为records的ArrayList中，每一行records中存储的对象为一个String数组
        while ((record = file.readLine()) != null) {
            record = record.replaceAll("\"(?!\"+)", "");
            //  System.out.println(record);
            record = record.replaceAll("\"\"\"", "\"\"");
            //  System.out.println(record);

            //只对引号外面的逗号进行分割，对引号内的不分割
            String fields[] = record.trim().split(" ,(?!\"+)", -1);

            records.add(fields);
        }
        //关闭文件对象
        file.close();
        //reader.close();
        //将存储测试数据的List转换为一个Object的二维数组
        Object[][] results = new Object[records.size()][];
        //设置二位数组每行的值，每行是一个Object对象
        for (int i = 0; i < records.size(); i++) {
            results[i] = records.get(i);

        }
        return results;
    }


    public static Object[][] getExcelData(String filePath) throws IOException {

        List<Object[]> records = new ArrayList<Object[]>();

        String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            //获取工作薄
            Workbook wb = null;
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook(is);
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else {
                return null;
            }

            //读取第一个工作页sheet
            Sheet sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            Row row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();

            for (int i = 1; i < rownum; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    String fields[] = new String[colnum];
                    for (int j = 0; j < colnum; j++) {

                        Cell cell = row.getCell(j);
                        try {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            fields[j] = cell.getStringCellValue();
                        } catch (NullPointerException e) {
                            fields[j] = "";
                        }
                    }
                    records.add(fields);
                } else {
                    break;
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Object[][] results = new Object[records.size()][];
        //设置二位数组每行的值，每行是一个Object对象
        for (int i = 0; i < records.size(); i++) {
            results[i] = records.get(i);

        }
        return results;

    }

    public static String getJsonData(String filepath) throws Exception {
        StringBuilder result = new StringBuilder();
        try{
            File f= new File(filepath);
            InputStreamReader read = new InputStreamReader(new FileInputStream(f),"gbk");
            BufferedReader br = new BufferedReader(new BufferedReader(read));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }



    //不同的头信息 处理数据
    public String[] dealExcelVatiablesWithDifferentHeader(Map variablesMap, HashMap<String, String> variablesHeaderMap , String caseNum, String testCase,
                                                          String requestURL, String requestMethod, String testDataType,
                                                          String testURLData, String testBodyData, String expectData,
                                                          String variableAccess, String variableStore, String status) throws Exception {
        RequestBaseApi requestBase = new RequestBaseApi();

        String[] response = {"", "",""};
        
        if (status.equals("Yes")) {
            logger.info("正在测试" + caseNum + ". " + testCase);
            //从txt读取json信息
            if (testBodyData.contains(".txt")&&testBodyData.contains("jsonanalysis_")) {
                String filepath = OperateFile.class.getResource("/testData" + testBodyData).getFile();
                testBodyData = getJsonData(filepath);
            }

            if (!variableAccess.isEmpty()) {
                String fields[] = variableAccess.trim().split(",");

                for (int i = 0; i < fields.length; i++) {
                    String value="";
                    if(fields[i].equals("$mock_autotime")){
                        value= System.currentTimeMillis()+"";
                    }else if (!fields[i].equals("$mock_unixtimestamp")&&fields[i].endsWith("mock_unixtimestamp")) {

                        String timeNum=fields[i].substring(1,fields[i].indexOf("m"));
                        MockVariables mockVariables = new MockVariables();
                        value=mockVariables.timeStampExpand(timeNum);
                    }
                    else{
                        value = "" + variablesMap.get(fields[i]);
                    }
                    if(fields[i].startsWith("$"))
                        fields[i]= Matcher.quoteReplacement("$")+fields[i].substring(1);
                    requestURL=requestURL.replaceAll(fields[i], value);
                    testURLData = testURLData.replaceAll(fields[i], value);
                    testBodyData = testBodyData.replaceAll(fields[i], value.trim());
                    expectData = expectData.replaceAll(fields[i], value);
                }
            }
            System.out.println("testBodyData"+testBodyData);
            response[1] = expectData;
            response[2] = testBodyData;

            if((requestURL.contains("callback.jingsocial.com")||requestURL.contains("workwechat.jingsocial.com/callback/customer/"))&& testDataType.equals("xml"))
                response[0] = requestBase.doXmlPost(testBodyData,requestURL);
            else if (requestMethod.contains("GET") && testDataType.equals("Query Params")) {
                response[0] = requestBase.doGet(url + requestURL, variablesHeaderMap, testURLData);
            } else if (requestMethod.contains("GET") && testDataType.equals("Multi Query Params")) {
                String fields[] = testURLData.trim().split(";");
                Map parametersMap=new HashMap();
                for (int i = 0; i < fields.length; i++) {
                    String store[] = fields[i].trim().split("=");
                    parametersMap.put(store[0],store[1]);
                }
                response[0] = requestBase.doGet(url + requestURL, variablesHeaderMap, parametersMap);
            } else if (requestMethod.contains("GET") && testDataType.equals("json")) {
                response[0] = requestBase.get(testBodyData,url + requestURL + testURLData, variablesHeaderMap);
            }else if (requestMethod.contains("POST") && testDataType.equals("json")) {
                response[0] = requestBase.doPost(testBodyData, url + requestURL, variablesHeaderMap);
            }else if (requestMethod.contains("POST") && testDataType.equals("file")) {
                response[0] = requestBase.doFileUpload(testBodyData,url + requestURL+testURLData, variablesHeaderMap,testDataType);
            }else if (requestMethod.contains("POST") && testDataType.equals("path&formdata")){
                String fields[] = testBodyData.trim().split(";");
                Map parametersMap=new HashMap();
                for (int i = 0; i < fields.length; i++) {
                    String store[] = fields[i].trim().split("=");
                    try{
                        parametersMap.put(store[0],store[1]);
                    }catch (ArrayIndexOutOfBoundsException e){
                        parametersMap.put(store[0],"");
                    }

                }
                response[0] = requestBase.doPost(parametersMap, url+requestURL+testURLData, variablesHeaderMap);

            } else if (requestMethod.contains("POST") && testDataType.equals("path&json")){
                response[0] = requestBase.doPost(testBodyData, url+requestURL+testURLData, variablesHeaderMap);
            } else if (requestMethod.contains("PUT") && testDataType.equals("path&json")) {
                response[0] = requestBase.put(url + requestURL + testURLData, testBodyData, variablesHeaderMap);
            } else if (requestMethod.contains("PUT") && testDataType.equals("path")) {
                response[0] = requestBase.put(url + requestURL + testURLData, variablesHeaderMap);
            } else if (requestMethod.contains("DELETE") && testDataType.equals("path")) {
                response[0] = requestBase.delete(url + requestURL + testURLData, variablesHeaderMap);
            } else if (requestMethod.contains("DELETE") && testDataType.equals("path&json")) {
                response[0] = requestBase.delete(testBodyData,url + requestURL + testURLData, variablesHeaderMap);
            }
            //&times在html页面会被转义成x 所以将&符号转成&amp;
            if(requestURL.contains("callback.jingsocial.com")||requestURL.contains("workwechat.jingsocial.com/callback/customer/")) {
                requestURL=requestURL.replaceAll("&times", "&amp;times");
                Reporter.log("请求地址:" + requestURL);
                logger.info("请求的url是："+requestURL);
            } else{
                Reporter.log("请求地址:" + url + requestURL + testURLData);
                logger.info("请求的url是："+url+requestURL+testURLData);
            }
            //xml格式的数据 输出在html页面上会被解析 加上xmp进行标识则可显示完整的xml结构
            if (testBodyData.toLowerCase().contains("<xml>")) {
                testBodyData = "<xmp>" + testBodyData + "</xmp>";
            }

            logger.info("发送请求前mock变量列表："+variablesMap);
            logger.info("请求的body参数是： "+testBodyData);
            logger.info("请求期望的结果是： "+expectData);
            logger.info("请求实际响应结果是： "+response[0]);

            Reporter.log("请求参数:" + testBodyData);
            Reporter.log("期望结果:" + expectData);
            Reporter.log("实际结果:" + response[0]);

            if (!variableStore.isEmpty()) {
                String fields[] = variableStore.trim().split(",");
                String storevalue;
                for (int i = 0; i < fields.length; i++) {
                    String store[] = fields[i].trim().split("=");

                    System.out.println("$." + store[1]);
                    if (store[1].startsWith("key")) {
                        int num = Integer.parseInt(store[1].substring(store[1].indexOf("y") + 1, store[1].indexOf(".")));
                        store[1] = store[1].substring(store[1].indexOf(".") + 1);
                        storevalue = JsonPath.parse(response[0]).read("$." + store[1]) + "";
                        storevalue = storevalue.substring(storevalue.indexOf("{") + 1, storevalue.indexOf("}"));
                        String[] keyArr = storevalue.trim().split(",");
                        storevalue = keyArr[num].substring(0, keyArr[num].indexOf("="));
                    }else if(store[1].endsWith("_func_behind#####")){
                        store[1]=store[1].replaceAll("_func_behind#####","");
                        try{
                            storevalue=JsonPath.parse(response[0]).read("$." + store[1]) + "";
                            storevalue=storevalue.replaceAll(".*?#####","");
                        }catch ( com.jayway.jsonpath.PathNotFoundException e){
                            storevalue="";
                        }

                    }else{
                        storevalue = JsonPath.parse(response[0]).read("$." + store[1]) + "";
                    }
                    variablesMap.put(store[0], storevalue);
                }
                logger.info("请求后mock变量list是： "+ variablesMap);
            }
        } else {
            //logger.info("主动跳过测试用例，NO."+caseNum);
            throw new SkipException("主动跳过测试用例,NO." + caseNum);
        }
        return response;
    }





   //替换用例中的变量
    public String[] replaceRequestVariable(Map variablesMap, String header, String testDataType, String requestURL, String testURLData,
                                           String testBodyData, String expectData, String variableAccess, String variableStore) throws Exception {
        //testBodyData中，从txt读取json信息(json类型的参数请求才会处理)
        if (testBodyData.contains(".txt")&&testDataType.contains("json")) {
            String filepath = OperateFile.class.getResource("/testData" + testBodyData).getFile();
            testBodyData = getJsonData(filepath);
        }
        //替换用例中的请求变量
        //&13fileid,&13fieldUnionid,&13fieldname,&13fieldother1,&13fieldother2
        if (!variableAccess.isEmpty()) {
            String fields[] = variableAccess.trim().split(",");

            for (int i = 0; i < fields.length; i++) {
                String value="";
                //ms级别的时间戳格式,区别于mock初始化变量里的时间(同一个excel时间一致)，这里支持一个excel里，不同用例取不同时间
                if(fields[i].equals("$mock_autotime")){
                    value= System.currentTimeMillis()+"";
                }
                else if (!fields[i].equals("$mock_unixtimestamp")&&fields[i].endsWith("mock_unixtimestamp")) {
                    //&1Mmock_unixtimestamp,&2Dmock_unixtimestamp,&-10Smock_unixtimestamp,
                    // &-30Fmock_unixtimestamp,&3Hmock_unixtimestamp
                    //以当前时间为基准
                    //1M=往后1个月
                    //2D=往后2天
                    //-10S=往前10秒
                    //-30F=往前30分钟
                    //3H=往后3小时
                    String timeNum=fields[i].substring(1,fields[i].indexOf("m"));
                    MockVariables mockVariables = new MockVariables();
                    value=mockVariables.timeStampExpand(timeNum);
                }else if (!fields[i].equals("$mock_lastDateOfMonth")&&fields[i].endsWith("mock_lastDateOfMonth")){
                    try {
                        String timeNum=fields[i].substring(1,fields[i].indexOf("m"));
                        MockVariables mockVariables = new MockVariables();
                        value=mockVariables.lastDateOfXMonth(timeNum);
                    }catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }else if (!fields[i].equals("$mock_firstDateOfMonth")&&fields[i].endsWith("mock_firstDateOfMonth")){
                    try {
                        String timeNum=fields[i].substring(1,fields[i].indexOf("m"));
                        MockVariables mockVariables = new MockVariables();
                        value=mockVariables.firstDateOfXMonth(timeNum);
                    }catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }else if (!fields[i].equals("$mock_firstDateOfMonunixtimestamp")&&fields[i].endsWith("mock_firstDateOfMonunixtimestamp")){
                    try {
                        String timeNum=fields[i].substring(1,fields[i].indexOf("m"));
                        MockVariables mockVariables = new MockVariables();
                        value=mockVariables.firstDateOfXMonunixtimestamp(timeNum);
                    }catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }else if (!fields[i].equals("$mock_lastDateOfMonunixtimestamp")&&fields[i].endsWith("mock_lastDateOfMonunixtimestamp")){
                    try {
                        String timeNum=fields[i].substring(1,fields[i].indexOf("m"));
                        MockVariables mockVariables = new MockVariables();
                        value=mockVariables.lastDateOfXMonunixtimestamp(timeNum);
                    }catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }else if (!fields[i].equals("$mock_datetime")&&fields[i].endsWith("mock_datetime")) {
                    //以当前时间为基准
                    //1M=往后1个月
                    //2D=往后2天
                    //-10S=往前10秒
                    //-30F=往前30分钟
                    //3H=往后3小时
                    String timeNum=fields[i].substring(1,fields[i].indexOf("m"));
                    MockVariables mockVariables = new MockVariables();
                    value=mockVariables.datetimeExpand(timeNum);
                }
                else{
                    value = "" + variablesMap.get(fields[i]);
                }
                if(fields[i].startsWith("$"))
                    fields[i]= Matcher.quoteReplacement("$")+fields[i].substring(1);
                requestURL=requestURL.replaceAll(fields[i], value);
                testURLData = testURLData.replaceAll(fields[i], value);
                testBodyData = testBodyData.replaceAll(fields[i], value.trim());
                expectData = expectData.replaceAll(fields[i], value);
                if(variableStore.contains("=")){
                    variableStore = variableStore.replaceAll(fields[i], value);
                }
                if(header.contains("=")){
                    header = header.replaceAll(fields[i], value);
                }
            }
        }
        //System.out.println("testBodyData"+testBodyData);
        response[1] = expectData;
        response[2] = testBodyData;

        String[] replaceData={requestURL,testURLData,testBodyData,expectData,variableStore,header};

        return replaceData;
    }

    //处理不同apitype
    public void dealAPIType(String apiType, String requestURL) throws Exception {
        switch (apiType){
            case "internal_normal":
                inteURL=url+requestURL;
                inteHeaderMap.putAll(headerMap);
                break;
            case "datapermission":
                inteURL=url+requestURL;
                inteHeaderMap.putAll(datapermissionheaderMap);
                System.out.println(inteHeaderMap.toString());
                break;
            case "internal_workwechat":
                inteURL=url+requestURL;
                inteHeaderMap.putAll(integrationHeaderMap);
                break;
            case "internal_otheraccount":
                inteURL=url+requestURL;
                inteHeaderMap.putAll(headerMap2);
                break;
            case "internal_otheraccount1":
                inteURL=url+requestURL;
                inteHeaderMap.putAll(headerMap3);
                break;
            case "internal_workwechat_miniprogram":
                inteURL=url+requestURL;
                inteHeaderMap.putAll(workwechatMiniHeaderMap);
                break;
            case "integration_oldhost_token_workwechat":
                inteURL=thirdpartyURL+requestURL;
                break;
            case "integration_newhost_token_workwechat":
                inteURL=url+requestURL;
                break;
            case "integration_auth_workwechat":
                inteURL=url+requestURL;
                inteHeaderMap.putAll(thirdpartyHeaderMap);
                break;
            case "jingsales_normal":
                inteURL=url+requestURL;
                inteHeaderMap.putAll(salestoolHeaderMap);
	        case "API_Thirdparty_username1":
		        inteURL=url+requestURL;
		        inteHeaderMap.putAll(thirdpartyOtherMidHeaderMap);
                break;

            default:
                inteURL=url+requestURL;
                inteHeaderMap.putAll(headerMap);
                System.out.println(inteHeaderMap.toString());

        }


    }

    //处理不同apitype和自定义header
    public void dealAPIType(String apiType, String requestURL, String header){
        Map customHeaderMap=new HashMap();
        if(header.contains("=")) {
            String fields[] = header.trim().split(";");
            for (int i = 0; i < fields.length; i++) {
                String store[] = fields[i].trim().split("=");
                customHeaderMap.put(store[0], store[1]);
            }
        }
        switch (apiType){

            case "integration_oldhost":
                inteURL=thirdpartyURL+requestURL;
                break;
            default:
                inteURL=url+requestURL;


        }

        inteHeaderMap.putAll(customHeaderMap);
        logger.info("请求的header:"+inteHeaderMap);

    }

    //发起请求
    public void dealRequest(String requestURL, String requestMethod, String testDataType,
                            String testURLData, String testBodyData)throws Exception {

        if((requestURL.contains("callback.jingsocial.com")||requestURL.contains("workwechat.jingsocial.com/callback/customer/"))&& testDataType.equals("xml"))
            response[0] = requestBase.doXmlPost(testBodyData,requestURL);
        else if(requestURL.contains("callback.jingsocial.com"))
            response[0] = requestBase.doPost(testBodyData,requestURL , inteHeaderMap);
        else if (requestMethod.toUpperCase().equals("POST") && testDataType.equals("xml")) {
            response[0] = requestBase.doPostXML(testBodyData, inteURL, inteHeaderMap);
        }else if (requestMethod.toUpperCase().equals("GET") && testDataType.equals("Query Params")) {
            response[0] = requestBase.doGet(inteURL, inteHeaderMap, testURLData);
        } else if (requestMethod.toUpperCase().equals("GET") && testDataType.equals("Multi Query Params")) {
            String fields[] = testURLData.trim().split(";");
            Map parametersMap=new HashMap();
            for (int i = 0; i < fields.length; i++) {
                String store[] = fields[i].trim().split("=");
                parametersMap.put(store[0],store[1]);
            }
            response[0] = requestBase.doGet(inteURL, inteHeaderMap, parametersMap);
        } else if (requestMethod.toUpperCase().equals("GET") && testDataType.equals("json")) {
            response[0] = requestBase.get(testBodyData,inteURL + testURLData, inteHeaderMap);
        }else if (requestMethod.toUpperCase().equals("POST") && testDataType.equals("json")) {
            response[0] = requestBase.doPost(testBodyData, inteURL, inteHeaderMap);
        }else if (requestMethod.contains("POST") && testDataType.contains("file")) {
            response[0] = requestBase.doFileUpload(testBodyData,inteURL+testURLData, inteHeaderMap,testDataType);
        }else if (requestMethod.contains("POST") && testDataType.equals("path&formdata")){
            String fields[] = testBodyData.trim().split(";");
            Map parametersMap=new HashMap();
            for (int i = 0; i < fields.length; i++) {
                String store[] = fields[i].trim().split("=");
                try{
                    parametersMap.put(store[0],store[1]);
                }catch (ArrayIndexOutOfBoundsException e){
                    parametersMap.put(store[0],"");
                }

            }
            response[0] = requestBase.doPost(parametersMap, inteURL+testURLData, inteHeaderMap);

        } else if (requestMethod.toUpperCase().equals("POST") && testDataType.equals("path&json")){
            response[0] = requestBase.doPost(testBodyData, inteURL+testURLData, inteHeaderMap);
        } else if (requestMethod.toUpperCase().equals("PUT") && testDataType.equals("path&json")) {
            response[0] = requestBase.put(inteURL + testURLData, testBodyData, inteHeaderMap);
        } else if (requestMethod.toUpperCase().equals("PUT") && testDataType.equals("path")) {
            response[0] = requestBase.put(inteURL + testURLData, inteHeaderMap);
        } else if (requestMethod.toUpperCase().equals("DELETE") && testDataType.equals("path")) {
            response[0] = requestBase.delete(inteURL + testURLData, inteHeaderMap);
        } else if (requestMethod.toUpperCase().equals("DELETE") && testDataType.equals("path&json")) {
            response[0] = requestBase.delete(testBodyData,inteURL + testURLData, inteHeaderMap);
        }

    }

    //存储变量
    public void storeVariables(Map variablesMap, String variableStore){
        //从请求响应中提取值作为变量，供excel其他用例使用
        //&13fileid=data.id,&13fieldUnionid=data.fields[0]
        if (!variableStore.isEmpty()) {
            String fields[] = variableStore.trim().split(",");
            String storevalue;
            Object document=Configuration.defaultConfiguration().jsonProvider().parse(response[0]);
            for (int i = 0; i < fields.length; i++) {
                String store[] = fields[i].trim().split("=");

                System.out.println("$." + store[1]);

                //&5key=key0.data.items，特殊用处，不能通用
                if (store[1].startsWith("key")) {
                    //0
                    int num = Integer.parseInt(store[1].substring(store[1].indexOf("y") + 1, store[1].indexOf(".")));
                    //data.items
                    store[1] = store[1].substring(store[1].indexOf(".") + 1);
                    storevalue = JsonPath.parse(document).read("$." + store[1]) + "";
                    storevalue = storevalue.substring(storevalue.indexOf("{") + 1, storevalue.indexOf("}"));
                    String[] keyArr = storevalue.trim().split(",");
                    storevalue = keyArr[num].substring(0, keyArr[num].indexOf("="));
                }else if(store[1].endsWith("_func_behind#####")){
                    //segmentation里的,提取#####前面的值
                    store[1]=store[1].replaceAll("_func_behind#####","");
                    try{
                        storevalue=JsonPath.parse(document).read("$." + store[1]) + "";
                        storevalue=storevalue.replaceAll(".*?#####","");
                    }catch ( com.jayway.jsonpath.PathNotFoundException e){
                        storevalue="";
                    }
               //存数据的时候，支持自定义
                }else if(store[1].startsWith("custom@")){
                    storevalue = store[1].replaceAll("custom@","");
                }
                else{
                    System.out.println(document.toString());
                    storevalue = JsonPath.parse(document).read("$." + store[1]) + "";
                }
                variablesMap.put(store[0], storevalue);
            }
            logger.info("请求后mock变量list是： "+ variablesMap);
        }
    }


    //处理有变量的表格（带apitype字段）,后面新的接口尽量都用这个,处理不同的header
    // (需要一个excel里同时访问多个账号也可以用)
    public String[] dealExcelVatiables(Map variablesMap, String caseNum, String testCase, String apiType,
                                       String requestURL, String requestMethod, String testDataType,
                                       String testURLData, String testBodyData, String expectData,
                                       String variableAccess, String variableStore, String status) throws Exception {

        // 初始化变量
        requestBase = new RequestBaseApi();
        //response,expectData,testBodyData
        for (int i = 0; i < response.length; i++) {
            response[i]="";

        }
        inteURL="";
        inteHeaderMap=new HashMap<String, String>();


        //Yes的用例才会执行
        if (status.toLowerCase().equals("yes")) {

            logger.info("正在测试" + caseNum + ". " + testCase);

            //请求前替换请求变量
            String header="";
            String[] replaceData=replaceRequestVariable(variablesMap,header,testDataType,requestURL,testURLData,testBodyData,expectData,variableAccess,variableStore);
            //String[] replaceData={requestURL,testURLData,testBodyData,expectData};
            requestURL=replaceData[0];
            testURLData=replaceData[1];
            testBodyData=replaceData[2];
            expectData=replaceData[3];
            variableStore=replaceData[4];

            //处理apitype
            dealAPIType(apiType,requestURL);


            if(config.getKafkakey().equals("no")){
                dealRequest(requestURL,requestMethod,testDataType,testURLData,testBodyData);
            }else {

                JSONObject json = new JSONObject();
                String testid = RandomStringUtils.randomAlphanumeric(16);
                json.put("testid", testid);
                json.put("requestURL", requestURL);
                json.put("requestMethod", requestMethod);
                json.put("testDataType", testDataType);
                json.put("testURLData", testURLData);
                json.put("testBodyData", testBodyData);
                json.put("inteURL", inteURL);
                json.put("inteHeaderMap", JSONObject.parseObject(JSON.toJSONString(inteHeaderMap)));
                String topic="dev_autohttpsend";
                config.getproducer().send(new ProducerRecord<String, String>(topic, "sendkey", json.toString()));

                int ii= 0;
                while (true){
                    ii++;
                    Thread.sleep(300);
                    String pidstr = config.FilterfindTest(testid,config.getMongoConnect());
                    if(pidstr.length()>1 || ii>70)
                    {
                        response[0] = pidstr;
                        config.FilterfindDel(testid,config.getMongoConnect());
                        break;
                    }
                }
            }

            //发起请求
            //dealRequest(requestURL,requestMethod,testDataType,testURLData,testBodyData);
            //报告和log
            //&times在html页面会被转义成x 所以将&符号转成&amp;
            if(requestURL.contains("callback.jingsocial.com")||requestURL.contains("workwechat.jingsocial.com/callback/customer/")) {
                requestURL=requestURL.replaceAll("&times", "&amp;times");
                Reporter.log("请求地址:" + requestURL);
                logger.info("请求的url是："+requestURL);
            } else{
                Reporter.log("请求地址:" + url + requestURL + testURLData);
                logger.info("请求的url是："+url+requestURL+testURLData);
            }
            //xml格式的数据 输出在html页面上会被解析 加上xmp进行标识则可显示完整的xml结构
            if (testBodyData.toLowerCase().contains("<xml>")) {
                testBodyData = "<xmp>" + testBodyData + "</xmp>";
            }

            logger.info("发送请求前mock变量列表："+variablesMap);
            logger.info("请求的body参数是： "+testBodyData);
            logger.info("请求期望的结果是： "+expectData);
            logger.info("请求实际响应结果是： "+response[0]);

            Reporter.log("请求参数:" + testBodyData);
            Reporter.log("期望结果:" + expectData);
            Reporter.log("实际结果:" + response[0]);


            //存储表格中自定义的变量
            storeVariables(variablesMap,variableStore);

        } else {
            //logger.info("主动跳过测试用例，NO."+caseNum);
            throw new SkipException("主动跳过测试用例,NO." + caseNum);
        }
        return response;
    }

    //excel里有apiType和header两列，自定义header，代码里不再做默认处理，目前为了解决user权限模块的测试
    public String[] dealExcelVatiables(Map variablesMap, String caseNum, String testCase, String apiType, String header,
                                       String requestURL, String requestMethod, String testDataType,
                                       String testURLData, String testBodyData, String expectData,
                                       String variableAccess, String variableStore, String status) throws Exception {


        // 初始化变量
        requestBase = new RequestBaseApi();
        //response,expectData,testBodyData
        for (int i = 0; i < response.length; i++) {
            response[i]="";

        }
        inteURL="";
        inteHeaderMap=new HashMap<String, String>();

        //Yes的用例才会执行
        if (status.toLowerCase().equals("yes")) {

            logger.info("正在测试" + caseNum + ". " + testCase);

            //请求前替换请求变量
            String[] replaceData=replaceRequestVariable(variablesMap,header,testDataType,requestURL,testURLData,testBodyData,expectData,variableAccess,variableStore);
            //String[] replaceData={requestURL,testURLData,testBodyData,expectData};
            requestURL=replaceData[0];
            testURLData=replaceData[1];
            testBodyData=replaceData[2];
            expectData=replaceData[3];
            variableStore=replaceData[4];
            header=replaceData[5];

            //处理apitype
            dealAPIType(apiType,requestURL,header);



	        if(config.getKafkakey().equals("no")){
                dealRequest(requestURL,requestMethod,testDataType,testURLData,testBodyData);
            }else {

                JSONObject json = new JSONObject();
                String testid = RandomStringUtils.randomAlphanumeric(16);
                json.put("testid", testid);
                json.put("requestURL", requestURL);
                json.put("requestMethod", requestMethod);
                json.put("testDataType", testDataType);
                json.put("testURLData", testURLData);
                json.put("testBodyData", testBodyData);
                json.put("inteURL", inteURL);
                json.put("inteHeaderMap", JSONObject.parseObject(JSON.toJSONString(inteHeaderMap)));

                String topic="dev_autohttpsend";
                config.getproducer().send(new ProducerRecord<String, String>(topic, "sendkey", json.toString()));
                int ii= 0;
                while (true){
                    ii++;
                    //超时时间9秒
                    Thread.sleep(300);
                    String pidstr = config.FilterfindTest(testid,config.getMongoConnect());
                    if(pidstr.length()>1 || ii>70)
                    {
                        response[0] = pidstr;
                        config.FilterfindDel(testid,config.getMongoConnect());
                        break;
                    }
                }
            }

            //发起请求
            //dealRequest(requestURL,requestMethod,testDataType,testURLData,testBodyData);

            //报告和log
            //&times在html页面会被转义成x 所以将&符号转成&amp;
            if(requestURL.contains("callback.jingsocial.com")||requestURL.contains("workwechat.jingsocial.com/callback/customer/")) {
                requestURL=requestURL.replaceAll("&times", "&amp;times");
                Reporter.log("请求地址:" + requestURL);
                logger.info("请求的url是："+requestURL);
            } else{
                Reporter.log("请求地址:" + url + requestURL + testURLData);
                logger.info("请求的url是："+url+requestURL+testURLData);
            }
            //xml格式的数据 输出在html页面上会被解析 加上xmp进行标识则可显示完整的xml结构
            if (testBodyData.toLowerCase().contains("<xml>")) {
                testBodyData = "<xmp>" + testBodyData + "</xmp>";
            }

            logger.info("发送请求前mock变量列表："+variablesMap);
            logger.info("请求的body参数是： "+testBodyData);
            logger.info("请求期望的结果是： "+expectData);
            logger.info("请求实际响应结果是： "+response[0]);
            Reporter.log("请求参数:" + testBodyData);
            Reporter.log("期望结果:" + expectData);
            Reporter.log("实际结果:" + response[0]);

            //存储表格中自定义的变量
            storeVariables(variablesMap,variableStore);

        } else {
            //logger.info("主动跳过测试用例，NO."+caseNum);
            throw new SkipException("主动跳过测试用例,NO." + caseNum);
        }

        return response;
    }
}

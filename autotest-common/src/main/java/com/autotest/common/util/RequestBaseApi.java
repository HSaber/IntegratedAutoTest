package com.autotest.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

public class RequestBaseApi {
	private static volatile RequestBaseApi instance = null;
	final static Logger Log = Logger.getLogger(RequestBaseApi.class);
    //创建一个可关闭的HttpClient对象
    public static CloseableHttpClient httpclient =null;
    public static RequestConfig requestConfig = null;
    public static HttpClientContext context = null;
    public static CookieStore cookieStore = null;
    public RequestBaseApi(){
        httpclient= HttpClients.createDefault();
    }

	public static RequestBaseApi getInstance() {
		if (instance == null) {
			synchronized (RequestBaseApi.class) {
				if (instance == null) {
					instance = new RequestBaseApi();
				}
			}
		}
		return instance;
	}
    /**
    * post请求
    * @author maggie Xie
    * @param payload 请求参数（Json 格式）
    * @param url 请求url
    * */
    public String doPost(String payload,String url) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(url).build();
        //创建一个HttpPost的请求对象
        HttpPost httppost = new HttpPost(uri);
        //准备请求头信息
        HashMap<String,String> headermap = new HashMap<String,String>();
        headermap.put("Content-Type", "application/json; charset=utf-8");
        //设置payload
        httppost.setEntity(new StringEntity(payload,Charset.forName("UTF-8")));
        //加载请求头到httppost对象
        for(Map.Entry<String, String> entry : headermap.entrySet()) {
            httppost.addHeader(entry.getKey(), entry.getValue());
        }
        Log.info("发送post请求");
        //发送post请求
        CloseableHttpResponse httpResponse = httpclient.execute(httppost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
//        Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        if(responseString==null||responseString.equals("")){
            return "succcess";
        }
        JSONObject responseJson = JSON.parseObject(responseString);
        return responseJson.toJSONString();

    }
   /* post请求
    * @author maggie Xie
    * @param payload 请求参数（Json 格式）
    * @param url 请求url
    * */
    public String doPost(String payload,String url,HashMap<String,String> headerMap) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(url).build();
        //创建一个HttpPost的请求对象
        HttpPost httppost = new HttpPost(uri);
        //设置payload
        httppost.setEntity(new StringEntity(payload,Charset.forName("UTF-8")));
        //加载请求头到httppost对象
        headerMap.put("Content-Type", "application/json; charset=utf-8");
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httppost.addHeader(entry.getKey(), entry.getValue());
        }

        Log.info("发送post请求");
        //发送post请求
        CloseableHttpResponse httpResponse = httpclient.execute(httppost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        Log.info("statusCode= "+statusCode);
/*        if(statusCode==201){
            Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_201,"status code is not 201");
        }else if(statusCode==200){
            Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
        }else if(statusCode==500){
            Log.info("responseString= "+EntityUtils.toString(httpResponse.getEntity()));
            Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
        }*/
        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        if(responseString==null||responseString.equals("")){
            return "succcess";
        }
        JSONObject responseJson = JSON.parseObject(responseString);
        return responseJson.toJSONString();

    }
    /* post请求
     * @author maggie Xie
     * @param payload 请求参数（Json 格式）
     * @param url 请求url
     * */
    public String doPost2(String payload,String url,HashMap<String,String> headerMap) throws URISyntaxException, IOException {

        URI uri = new URIBuilder(url).build();
        //创建一个HttpPost的请求对象
        HttpPost httppost = new HttpPost(uri);
        //设置payload
        httppost.setEntity(new StringEntity(payload,Charset.forName("UTF-8")));
        //加载请求头到httppost对象
        if(headerMap!=null) {
            headerMap.put("Content-Type", "text/html; charset=utf-8");
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httppost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Log.info("发送post请求");
        //发送post请求
        CloseableHttpResponse httpResponse = httpclient.execute(httppost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        Log.info("statusCode= "+statusCode);
/*        if(statusCode==201){
            Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_201,"status code is not 201");
        }else if(statusCode==200){
            Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
        }else if(statusCode==500){
            Log.info("responseString= "+EntityUtils.toString(httpResponse.getEntity()));
            Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
        }*/
        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        if(responseString==null||responseString.equals("")){
            return "succcess";
        }
        JSONObject responseJson = JSON.parseObject(responseString);
        return responseJson.toJSONString();

    }
    /**
     * post请求
     * @author maggie Xie
     * @param payload 请求参数（Json 格式）
     * @param url 请求url
     * */
    public String doXmlPost(String payload,String url) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(url).build();
        //创建一个HttpPost的请求对象
        HttpPost httppost = new HttpPost(uri);
        //准备请求头信息
        HashMap<String,String> headermap = new HashMap<String,String>();
        headermap.put("Content-Type", "text/xml");
        //text/xml

        //设置payload
        httppost.setEntity(new StringEntity(payload,"UTF-8"));
        //加载请求头到httppost对象
        for(Map.Entry<String, String> entry : headermap.entrySet()) {
            httppost.addHeader(entry.getKey(), entry.getValue());
        }
        Log.info("发送post请求-xml");
        //发送post请求
        CloseableHttpResponse httpResponse = httpclient.execute(httppost);
        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        if(responseString==null||responseString.equals("")){
            return "success";
        }
        //JSONObject responseJson = JSON.parseObject(responseString);
        return responseString;

    }

    public String doPostXML(String payload,String url,HashMap<String,String> headerMap) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(url).build();
        //创建一个HttpPost的请求对象
        HttpPost httppost = new HttpPost(uri);
        //准备请求头信息
        headerMap.put("Content-Type", "text/xml");
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httppost.addHeader(entry.getKey(), entry.getValue());
            }

        //设置payload,text/xml
        httppost.setEntity(new StringEntity(payload,"UTF-8"));

        Log.info("发送post请求-xml");
        //发送post请求
        CloseableHttpResponse httpResponse = httpclient.execute(httppost);
        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        if(responseString==null||responseString.equals("")){
            return "success";
        }
        //JSONObject responseJson = JSON.parseObject(responseString);
        return responseString;

    }

    //参数是文件
    public String doFileUpload(String filepath,String url,HashMap<String,String> headerMap,String testDataType) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(url).build();
        //创建一个HttpPost的请求对象
        HttpPost httppost = new HttpPost(uri);
        filepath=RequestBaseApi.class.getResource(filepath).getFile();
        File file = new File(filepath);
        String name=file.getName();
         String contentype="";
       if(name.contains("gif")||name.contains("GIF")){
            contentype="image/gif";
        }else if(name.contains("jpg")||name.contains("jpeg")||name.contains("JPG")||name.contains("JPEG")){
            contentype="image/jpeg";
        }else if(name.contains("png")||name.contains("PNG")){
            contentype="image/png";
        }else if(name.contains("bmp")||name.contains("BMP")){
            contentype="application/x-bmp";
        }else if(name.contains(".html")){
           contentype="text/html";
       }
           else {
            contentype="application/octet-stream";
        }
        ContentBody fileBody = new FileBody(file,contentype);
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart(testDataType,fileBody);
        httppost.setEntity(reqEntity);

        //准备请求头信息

        //加载请求头到httppost对象
        headerMap.remove("Content-Type");
        if (headerMap!=null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httppost.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Log.info("发送post请求");
        //发送post请求
        CloseableHttpResponse httpResponse = httpclient.execute(httppost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
//        Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        if(responseString==null||responseString.equals("")){
            return "success";
        }
        JSONObject responseJson = JSON.parseObject(responseString);
        return responseJson.toJSONString();

    }



    //参数是一组key-value
    public String doPost1(Map parameters,String url,HashMap<String,String> headerMap) throws URISyntaxException, IOException {

        URI uri = new URIBuilder(url).build();
        //创建一个HttpPost的请求对象
        HttpPost httppost = new HttpPost(uri);

        //加载请求头到httppost对象
        if(headerMap !=null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httppost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        //设置参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Iterator iter = parameters.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String value = String.valueOf(parameters.get(name));
            params.add(new BasicNameValuePair(name, value));

            //System.out.println(name +"-"+value);
        }
        httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));

        Log.info("发送post请求");
        //发送post请求
        CloseableHttpResponse httpResponse = httpclient.execute(httppost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        Log.info("statusCode= "+statusCode);
     /*   if(statusCode==201){
            Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_201,"status code is not 201");
        }else if(statusCode==200){
            Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
        }else if(statusCode==500){
            Log.info("responseString= "+EntityUtils.toString(httpResponse.getEntity()));
            Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
        }*/
        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        if(responseString==null||responseString.equals("")){
            return "succcess";
        }
        JSONObject responseJson = JSON.parseObject(responseString);
        return responseJson.toJSONString();

    }

    //参数是一组key-value(包含file上传)
    public String doPost(Map parameters,String url,HashMap<String,String> headerMap) throws URISyntaxException, IOException {
        System.out.println(url);
        URI uri = new URIBuilder(url).build();
        //创建一个HttpPost的请求对象
        HttpPost httppost = new HttpPost(uri);

        //加载请求头到httppost对象
        headerMap.remove("Content-Type");
        if(headerMap !=null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httppost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        //设置参数

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        String contentype="";
        for (Iterator iter = parameters.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            String value = String.valueOf(parameters.get(key));
            //if(key.contains("file")||key.equals("import")){
            if(value.startsWith("/testData/")){
                try{
                    value=RequestBaseApi.class.getResource(value).getFile();
                    File file = new File(value);
                    String name=file.getName();
                    if(name.toLowerCase().contains("gif")){
                        contentype="image/gif";
                    }else if(name.toLowerCase().contains("jpg")||name.toLowerCase().contains("jpeg")){
                        contentype="image/jpeg";
                    }else if(name.toLowerCase().contains("png")){
                        contentype="image/png";
                    }else if(name.toLowerCase().contains("bmp")){
                        contentype="application/x-bmp";
                    }else {
                        contentype="application/octet-stream";
                    }
                    ContentBody fileBody = new FileBody(file,contentype);
                    builder.addPart(key,fileBody);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
            else{
                ContentBody stringBody = new StringBody(value,Charset.forName("UTF-8"));
                builder.addPart(key,stringBody);
                // builder.addTextBody(key, value, contentType);
            }
        }
        httppost.setEntity(builder.build());

        Log.info("发送post请求");
        //发送post请求
        CloseableHttpResponse httpResponse = httpclient.execute(httppost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        Log.info("statusCode= "+statusCode);

        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        if(responseString==null||responseString.equals("")){
            return "succcess";
        }
        JSONObject responseJson = JSON.parseObject(responseString);
        return responseJson.toJSONString();

    }
    static {
        init();
    }

    private static void init() {
        context = HttpClientContext.create();
        cookieStore = new BasicCookieStore();
        // 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）
        requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)
                .setConnectionRequestTimeout(60000).build();
        // 设置默认跳转以及存储cookie
        httpclient = HttpClientBuilder.create().setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore).build();
    }

    public String doPost( Map parameters,String url) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(url).build();
        //创建一个HttpPost的请求对象
        HttpPost httppost = new HttpPost(uri);

        //设置payload
        //设置参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Iterator iter = parameters.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String value = String.valueOf(parameters.get(name));
            params.add(new BasicNameValuePair(name, value));

            //System.out.println(name +"-"+value);
        }
        httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
        Log.info("发送post请求");
        //发送post请求
        CloseableHttpResponse httpResponse = httpclient.execute(httppost,context);

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        Log.info("statusCode= "+statusCode);
        printCookies();
 //       printResponse(httpResponse);

        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        if(responseString==null||responseString.equals("")){
            return "succcess";
        }
        return responseString;
    }

    /**
     * get请求,只有url参数
     * @author maggie Xie
     * @param url 请求url
     * */
    public String doGet(String url) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(url).build();
        //创建一个HttpPost的请求对象
        HttpGet httpget = new HttpGet(url);
        Log.info("发送get请求");
        //发送get请求
        CloseableHttpResponse httpResponse = httpclient.execute(httpget);
        //获得响应状态码
        int statusCode = httpResponse.getStatusLine().getStatusCode();
//        Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        JSONObject responseJson = JSON.parseObject(responseString);
        return responseJson.toJSONString();
    }
    /**
     * get请求,需要header参数
     * @author maggie Xie
     * @param url 请求url
     * @param headermap 请求参数
     * */
    public String doGet(String url,HashMap<String,String> headermap,Map<String,String> params) throws URISyntaxException, IOException {
        //设置参数
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
         String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
        String key = keys.get(i);
        String value = params.get(key);
        value = URLEncoder.encode(value, "UTF-8");
       if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
       prestr = prestr + key + "=" + value;
         } else {
         prestr = prestr + key + "=" + value + "&";
       }
     }
        System.out.println(prestr);
        //创建一个HttpGet的请求对象
        HttpGet httpget = new HttpGet(url + prestr);
        //加载请求头到httpget对象
        if (headermap!=null) {
            for (Map.Entry<String, String> entry : headermap.entrySet()) {
                httpget.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Log.info("发送get请求");
        //发送get请求
        CloseableHttpResponse httpResponse = httpclient.execute(httpget);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
//        Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        JSONObject responseJson = JSON.parseObject(responseString);
        return responseJson.toJSONString();
    }

    public String doGet(String url,HashMap<String,String> headermap,String params) throws URISyntaxException, IOException {
        //params=URLEncoder.encode(params);
        URL url1 = new URL(url+params);
        URI uri = new URI(url1.getProtocol(),url1.getHost(), url1.getPath(), url1.getQuery(), null);
        String replaceUrl=uri.toString();
        replaceUrl=replaceUrl.replaceAll("%20&%20","%20%26%20");
        uri=URI.create(replaceUrl);
        //创建一个HttpGet的请求对象
        HttpGet httpget = new HttpGet(uri);
        //加载请求头到httpget对象
        if(headermap!=null) {
            for (Map.Entry<String, String> entry : headermap.entrySet()) {
                httpget.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Log.info("发送get请求");
        //发送get请求
        CloseableHttpResponse httpResponse = httpclient.execute(httpget);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
//        Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");

        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        JSONObject responseJson = JSON.parseObject(responseString);
        return responseJson.toJSONString();
    }

    /*
       get请求，可传body
     */
    public String get(String payload,String url,HashMap<String,String> headerMap) throws IOException {
        MyHttpGet httpGet = new MyHttpGet(url);
        //设置payload
        httpGet.setEntity(new StringEntity(payload,Charset.forName("UTF-8")));
        //准备请求头信息
        headerMap.put("Content-Type", "application/json; charset=utf-8");
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            httpGet.addHeader(entry.getKey(), entry.getValue());
        }

        Log.info("发送get请求");
        CloseableHttpResponse httpResponse = httpclient.execute(httpGet);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        //     Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        JSONObject responseJson = JSON.parseObject(responseString);
        return responseJson.toJSONString();
    }
    /**
     * put请求
     * @author maggie Xie
     * @param url 请求url
     * @param payload 请求参数
     * @param headerMap 请求header参数
     * */
    public String put(String url, String payload, HashMap<String,String> headerMap) throws IOException {
        HttpPut httpput = new HttpPut(url);
        httpput.setEntity(new StringEntity(payload,Charset.forName("UTF-8")));

        headerMap.put("Content-Type", "application/json; charset=utf-8");
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpput.addHeader(entry.getKey(), entry.getValue());
        }

        Log.info("发送put请求");
        //发送put请求
        CloseableHttpResponse httpResponse = httpclient.execute(httpput);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
/*
       if (statusCode==200){
           Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
       }else if (statusCode==201){
           Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_201,"status code is not 201");
       }
*/

        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        JSONObject responseJson = JSON.parseObject(responseString);
        return responseJson.toJSONString();
    }

    public String put(String url, HashMap<String,String> headerMap) throws IOException {
        HttpPut httpput = new HttpPut(url);
        if(headerMap!=null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpput.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Log.info("发送put请求");
        //发送put请求
        CloseableHttpResponse httpResponse = httpclient.execute(httpput);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        System.out.println("statusCode:"+statusCode);
/*
       if (statusCode==200){
           Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
       }else if (statusCode==201){
           Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_201,"status code is not 201");
       }
*/
        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        JSONObject responseJson = JSON.parseObject(responseString);
        return responseJson.toJSONString();
    }

    /**
     * Delete请求
     * @author maggie Xie
     * @param url 请求url
     * */
    public String delete(String url,HashMap<String,String> headerMap) throws IOException {
        HttpDelete httpDelete = new HttpDelete(url);
        if(headerMap!=null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpDelete.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Log.info("发送delete请求");
        CloseableHttpResponse httpResponse = httpclient.execute(httpDelete);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
//        Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        JSONObject responseJson = JSON.parseObject(responseString);
        return responseJson.toJSONString();
    }

    public String delete(String payload,String url,HashMap<String,String> headerMap) throws IOException {
        MyHttpDelete httpDelete=new MyHttpDelete(url);
        //设置payload
        httpDelete.setEntity(new StringEntity(payload,Charset.forName("UTF-8")));
        //准备请求头信息
        headerMap.put("Content-Type", "application/json; charset=utf-8");
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpDelete.addHeader(entry.getKey(), entry.getValue());
        }

        Log.info("发送delete请求");
        CloseableHttpResponse httpResponse = httpclient.execute(httpDelete);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
 //     Assert.assertEquals(statusCode, TestNgConfig.RESPNSE_STATUS_CODE_200,"status code is not 200");
        String responseString =  EntityUtils.toString(httpResponse.getEntity());
        JSONObject responseJson = JSON.parseObject(responseString);
        return responseJson.toJSONString();
    }

    /**
     * 把结果console出来
     *
     * @param httpResponse
     * @throws ParseException
     * @throws IOException
     */
    public static void printResponse(HttpResponse httpResponse) throws ParseException, IOException {
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        System.out.println("status:" + httpResponse.getStatusLine());
        System.out.println("headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        while (iterator.hasNext()) {
            System.out.println("\t" + iterator.next());
        }
    }
    /**
     * 把当前cookie从控制台输出出来
     *
     */
    public static void printCookies() {
        cookieStore = context.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());
        }
    }

}

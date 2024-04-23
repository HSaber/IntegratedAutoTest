package com.autotest.common.util;

import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;



public class TinyTools {
    public static final String[] email_suffix="@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net,@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn".split(",");
    public static String base = "abcdefghijklmnopqrstuvwxyz0123456789";
    /*检查元素是否存在*/
    public  boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            System.out.println("加载ing");
            return false;
        }
    }
    /*规定时间内等待元素*/
    public  void waitElement(int times, WebDriver driver, By by) throws Exception {
        for (; times > 0; times--) {
            if (isElementPresent(driver, by))
                break;
            Thread.sleep(1000);
        }
        Assert.assertTrue(times > 0);
    }
    /**
     * 截图类，使用当前日期做截图名字
     * @param ScreenshotName 截图名字
     *
     * */
    public  static void ScreenshotAsDate(String ScreenshotName,AndroidDriver driver ) throws Exception
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmm");//设置日期格式
        String TimeData = df.format(new Date());
        File file=driver.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file,new File("test-output\\images"+TimeData+"\\"+ScreenshotName+".png"));
        System.out.println("截图成功，请在"+System.getProperty("user.dir")+"/test-output/images"+TimeData+"/下查看");
    }

    /*开始录制视频
     * @param name 视频名称
     * */
    public Process startRecordVideo(String videoname){
        System.out.println("开始录制视频");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");//设置日期格式
        String TimeData = df.format(new Date());
        String runadb="adb shell screenrecord /sdcard/"+videoname+".mp4";
        System.out.println("执行命令:"+runadb);
        Process runprocess=null;
        try{
            runprocess=Runtime.getRuntime().exec(runadb);
        }catch (IOException e){
            e.printStackTrace();
        }
        return runprocess;
    }

    /*结束视频录制*/
    public void stopRecordVideo(Process process,String videoname) throws Exception{
        process.destroy();
        //等待视频生成
        Thread.sleep(3000);
        String buff = "";
        //生成的路径以及文件名
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String TimeData = df.format(new Date());
        String OUTPUT_FOLDER = "test-output/Video"+TimeData;
        File reportDir= new File(OUTPUT_FOLDER);
        if(!reportDir.exists()&& !reportDir .isDirectory()){
            reportDir.mkdir();
        }
        String moveVideo="adb pull /sdcard/"+videoname+".mp4 "+System.getProperty("user.dir")+"/"+OUTPUT_FOLDER+"/"+videoname+".mp4";//new File("videos\\");
        System.out.println("执行命令:"+moveVideo);
        try{
            Process p=Runtime.getRuntime().exec(moveVideo);//移动数据内容
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    p.getInputStream(), "utf-8"));
            while ((buff = br.readLine()) != null) {
                System.out.println(buff);
            }
            br.close();
            System.out.println("移动视频成功，请在"+System.getProperty("user.dir")+"/"+OUTPUT_FOLDER+"/下查看");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /*随机生成邮箱地址*/
    public  int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }
    public  String getEmail(int lMin,int lMax) {
        int length=getNum(lMin,lMax);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = (int)(Math.random()*base.length());
            sb.append(base.charAt(number));
        }
        sb.append(email_suffix[(int)(Math.random()*email_suffix.length)]);
        return sb.toString();
    }

    /*随机生成手机号*/
    public  String[] telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
    public  String getTel() {
        int index=getNum(0,telFirst.length-1);
        String first=telFirst[index];
        String second=String.valueOf(getNum(1,888)+10000).substring(1);
        String third=String.valueOf(getNum(1,9100)+10000).substring(1);
        return first+second+third;
    }

    /**
     * 生成随机字符串
     * n ： 需要的长度
     * @return
     */
    public   String getRandomStr( int n )
    {
        String val = "";
        Random random = new Random();
        for ( int i = 0; i < n; i++ )
        {
            String str = random.nextInt( 2 ) % 2 == 0 ? "num" : "char";
            if ( "char".equalsIgnoreCase( str ) )
            { // 产生字母
                int nextInt = random.nextInt( 2 ) % 2 == 0 ? 65 : 97;
                // System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
                val += (char) ( nextInt + random.nextInt( 26 ) );
            }
            else if ( "num".equalsIgnoreCase( str ) )
            { // 产生数字
                val += String.valueOf( random.nextInt( 10 ) );
            }
        }
        return val;
    }
    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     * @param URL  url地址
     * @return  url请求参数部分
     */
    public static Map<String, String> urlSplit(String URL){
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit=null;
        String strUrlParam=TruncateUrlPage(URL);
        if(strUrlParam==null){
            return mapRequest;
        }
        arrSplit=strUrlParam.split("[&]");
        for(String strSplit:arrSplit){
            String[] arrSplitEqual=null;
            arrSplitEqual= strSplit.split("[=]");
            //解析出键值
            if(arrSplitEqual.length>1){
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            }else{
                if(arrSplitEqual[0]!=""){
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }
    /**
     * 去掉url中的路径，留下请求参数部分
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL){
        String strAllParam=null;
        String[] arrSplit=null;
        //strURL=strURL.trim().toLowerCase();
        arrSplit=strURL.split("[?]");
        if(strURL.length()>1){
            if(arrSplit.length>1){
                for (int i=1;i<arrSplit.length;i++){
                    strAllParam = arrSplit[i];
                }
            }
        }
        return strAllParam;
    }
    public void SendEmail(String subject,String content,String emailadd,String env) throws Exception{
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host","smtp.163.com");// smtp服务器地址
        props.put("mail.smtp.port", 465);// 设置端口
        props.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
        Session session = Session.getInstance(props);
        session.setDebug(true);

        Message msg = new MimeMessage(session);
        msg.setSubject(subject);
        msg.setContent(content,"text/html;charset=gb2312");
 //       msg.setText(content);
        msg.setFrom(new InternetAddress("15206115831@163.com",env+"-自动化测试报告","UTF-8"));//发件人邮箱(我的163邮箱)
        msg.setRecipient(Message.RecipientType.TO,
                new InternetAddress(emailadd)); //收件人邮箱
        msg.saveChanges();

        Transport transport = session.getTransport();
        transport.connect("15206115831@163.com","HH8011..");//发件人邮箱,授权码(可以在邮箱设置中获取到授权码的信息)

        transport.sendMessage(msg, msg.getAllRecipients());

        System.out.println("邮件发送成功...");
        transport.close();
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

    /**
     * JAVA从指定位置开始截取指定长度的字符串
     *
     * @param input 输入字符串
     * @param index 截取位置，左侧第一个字符索引值是1
     * @param count 截取长度
     * @return 截取字符串
     */
    public  String middle(String input, int index, int count) {
        count = (count > input.length() - index + 1) ? input.length() - index + 1 :
                count;
        return input.substring(index - 1, index + count - 1);
    }


    public  void createExcel(String tablename,String[][] arr,ArrayList<String> list) {

        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳

        //存储路径--获取桌面位置
        FileSystemView view = FileSystemView.getFileSystemView();
        File directory = view.getHomeDirectory();
        System.out.println(directory);
        //存储Excel的路径
        String path = directory+"\\"+tablename+"_"+date+".xlsx";
        System.out.println(path);
        try {

            //定义一个Excel表格
            XSSFWorkbook wb = new XSSFWorkbook();  //创建工作薄
            XSSFSheet sheet = wb.createSheet("sheet1"); //创建工作表
            int rownum=0;
            XSSFRow row = sheet.createRow(rownum); //行
            XSSFCell cell;  //单元格

            //添加表头数据
            for (int i = 0; i < list.size(); i++) {
                //从前端接受到的参数封装成list集合，然后遍历下标从而取出对应的值
                String value = list.get(i);
                //将取到的值依次写到Excel的第一行的cell中
                row.createCell(i).setCellValue(value);
            }
            for(int i=0;i<arr.length;i++){
                row=sheet.createRow(++rownum);
                for(int j=0;j<arr[i].length;j++)
                    row.createCell(j).setCellValue(arr[i][j]);
            }

            //输出流,下载时候的位置
//            FileWriter outputStream1 = new FileWriter(path);
            FileOutputStream outputStream = new FileOutputStream(path);
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
            System.out.println("写入成功");
        } catch (Exception e) {
            System.out.println("写入失败");
            e.printStackTrace();
        }
    }
}

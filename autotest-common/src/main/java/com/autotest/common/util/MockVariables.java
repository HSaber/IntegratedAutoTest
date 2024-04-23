package com.autotest.common.util;


import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MockVariables {
    private static Logger logger = Logger.getLogger(MockVariables.class);

    public String currentTimeForDiffFormat(String format){
        String time = "";
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        time = sdf.format(d);
        return time;
    }

    public String currentTime() throws InterruptedException {
        String time = "";
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        time = sdf.format(d);
        return time;
    }

    public String dayStart(int i) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - i, 0, 0, 0);
        long tt = calendar.getTime().getTime() / 1000;

        return tt + "";

    }
    /**
     * @param i 当前时间往前往后推，1往后推一天，-1往前推一天
     * @return 2021-12-20
     * @description
     */
        public String dayStartFormat(int i){
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)+i, 23, 59, 59);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(calendar.getTime());
        }


    public String dayEnd() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        long tt = calendar.getTime().getTime() / 1000;

        return tt + "";

    }

    public String monthStart() throws InterruptedException {
        String timeStamp;
        String time = "";
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        timeStamp = c.getTimeInMillis() / 1000 + "";
        return timeStamp;
    }

    public String timeStampExpand(String timeNum) throws Exception {
        Calendar c = Calendar.getInstance();
        String timeStamp = "";
        int Num = Integer.parseInt(timeNum.substring(0, timeNum.length() - 1));
        if (timeNum.endsWith("M"))
            c.add(Calendar.MONTH, Num);
        else if (timeNum.endsWith("D"))
            c.add(Calendar.DAY_OF_MONTH, Num);
        else if (timeNum.endsWith("F"))
            c.add(Calendar.MINUTE, Num);
        else if (timeNum.endsWith("H"))
            c.add(Calendar.HOUR_OF_DAY, Num);
        else if (timeNum.endsWith("S"))
            c.add(Calendar.SECOND, Num);
        timeStamp = c.getTimeInMillis() / 1000 + "";
        return timeStamp;
    }

    /**
      * @Description: 返回当天的日期：yyyymmdd格式
      */
    public String  dateOfToday() throws InterruptedException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        Date dateOfToday = c.getTime();
        return  sdf.format(dateOfToday);
    }
    /**
     * @Description:
     *  &1mock_lastDateOfMonth ,return:当前月份的后1个月的最后一天日期
     *  &-2mock_lastDateOfMonth ,return:当前月份的前2个月的最后一天日期
     *
     * @params: timeNum 当前日期的月份偏移量（1：后1个月，2：后2个月，-3：前3个月）
     * @return: 指定月份的最后一天日期，YYYYMMDD格式
     */
    public String  lastDateOfXMonth(String timeNum) throws InterruptedException{
        int Num = Integer.parseInt(timeNum);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, Num);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        Date lastDateOfPrevMonth = c.getTime();
        return  sdf.format(lastDateOfPrevMonth);
    }
    /**
     * @Description:
     *  &1mock_firstDateOfMonth ,return:当前月份的后1个月的第一天日期
     *  &-2mock_firstDateOfMonth ,return:当前月份的前2个月的第一天日期
     *
     * @params: timeNum 当前日期的月份偏移量（1：后1个月，2：后2个月，-3：前3个月）
     * @return: 指定月份的最后一天日期，YYYYMMDD格式
     */
    public String  firstDateOfXMonth(String timeNum) throws InterruptedException{
        int Num = Integer.parseInt(timeNum);
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH, Num);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return  (format.format(calendar.getTime()));

    }
    /**
     * @Description:
     *  &1mock_lastDateOfMonunixtimestamp,return:当前月份的后1个月的最后一天日期时间戳
     *  &-2mock_lastDateOfMonunixtimestamp,return:当前月份的前2个月的最后一天日期时间戳
     *
     * @params: timeNum 当前日期的月份偏移量（1：后1个月，2：后2个月，-3：前3个月）
     * @return: 指定月份的最后一天日期，时间戳格式
     */
    public String  lastDateOfXMonunixtimestamp(String timeNum) throws InterruptedException{
        int Num = Integer.parseInt(timeNum);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, Num);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return  ( c.getTimeInMillis()/1000+"");

    }
    /**
     * @Description:
     *  &1mock_firstDateOfMonth ,return:当前月份的后1个月的第一天日期时间戳
     *  &-2mock_firstDateOfMonth ,return:当前月份的前2个月的第一天日期时间戳
     *
     * @params: timeNum 当前日期的月份偏移量（1：后1个月，2：后2个月，-3：前3个月）
     * @return: 指定月份的最后一天日期，时间戳格式
     */
    public String firstDateOfXMonunixtimestamp(String timeNum) throws InterruptedException{
        int Num = Integer.parseInt(timeNum);
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH, Num);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

//        System.out.println("上个月第一天："+format.format(calendar.getTime()));
        return  ( calendar.getTimeInMillis()/1000+"" );

    }

    /**
     *
     * @param timeNum
     * @return
     * @throws Exception
     */
    public String datetimeExpand(String timeNum) throws Exception {
        Calendar c = Calendar.getInstance();
        long timeStamp ;
        int Num = Integer.parseInt(timeNum.substring(0, timeNum.length() - 1));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//要转换的时间格式
        if (timeNum.endsWith("M"))
            c.add(Calendar.MONTH, Num);
        else if (timeNum.endsWith("D"))
            c.add(Calendar.DAY_OF_MONTH, Num);
        else if (timeNum.endsWith("F"))
            c.add(Calendar.MINUTE, Num);
        else if (timeNum.endsWith("H"))
            c.add(Calendar.HOUR_OF_DAY, Num);
        else if (timeNum.endsWith("S"))
            c.add(Calendar.SECOND, Num);
        timeStamp = c.getTimeInMillis() / 1000 ;
        Date date=new Date(timeStamp*1000);
        String res=sdf.format(date);
        return res;
    }

}

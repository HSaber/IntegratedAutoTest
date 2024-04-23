package com.autotest.api.listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.log4testng.Logger;

public class TestngRetry implements IRetryAnalyzer {
    private static Logger logger = Logger.getLogger(TestngRetry.class);
    private static int maxRetryCount = 3;//最大的重跑次数
    private int currentRetryCount = 1;

    @Override
    public boolean retry(ITestResult result) {
        if (currentRetryCount <= maxRetryCount) {
            currentRetryCount++;
            return true;
        }
        return false;
    }
    /**
     * 用于重置失败重跑时的次数，还原到初始化的值
     * 如果项目中是使用dataProvider注解来提供用例测试数据参数化的，
     * 那么每个@Test执行的时候都会共有重跑的次数。
     * 例如：一个参数化用例有 3 组参数，如果全部正确，结果是：全部通过
     * 如果第一组参数，第一次失败（第二次成功，这里就用掉了一次重跑的次数，currentRetryCount 就+1了）
     * 接着第二组参数每次执行都失败，这个时候currentRetryCount=2， 那么第二组参数也就只会执行一次重跑。
     */
    public void reSetCount()
        {
        currentRetryCount = 1;
    }

}



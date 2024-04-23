package com.autotest.api.listener;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.util.Iterator;

public class TestngListener extends TestListenerAdapter {
    private static Logger logger = Logger.getLogger(TestngListener.class);

    @Override
    public void onTestFailure(ITestResult tr) {
        if(tr.getMethod().getCurrentInvocationCount()==1)
        {
            super.onTestFailure(tr);
            return;
        }
        processSkipResult(tr);
        logger.info(tr.getName() + " Failure");
        // 对于dataProvider的用例，每次成功后，重置Retry次数
        TestngRetry retry = (TestngRetry) tr.getMethod().getRetryAnalyzer();
        retry.reSetCount();
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        logger.info(tr.getName() + " Skipped");
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        if(tr.getMethod().getCurrentInvocationCount()==1)
        {
            super.onTestSuccess(tr);
            return;
        }

        processSkipResult(tr);
        logger.info(tr.getName() + " Success");
        // 对于dataProvider的用例，每次成功后，重置Retry次数
        TestngRetry retry = (TestngRetry) tr.getMethod().getRetryAnalyzer();
        retry.reSetCount();
    }

    @Override
    public void onTestStart(ITestResult tr) {
        super.onTestStart(tr);
        logger.info(tr.getName() + " Start");
    }


    // Remove all the dup Skipped results
    public void processSkipResult(ITestResult tr)
    {
        ITestContext iTestContext = tr.getTestContext();
        Iterator<ITestResult> processResults = iTestContext.getSkippedTests().getAllResults().iterator();
        while (processResults.hasNext()) {
            ITestResult skippedTest = (ITestResult) processResults.next();
            if (skippedTest.getMethod().getMethodName().equalsIgnoreCase(tr.getMethod().getMethodName()) ) {
                processResults.remove();
            }
        }
    }
}

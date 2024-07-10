package com.autotest.ui;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * ClassName:demo
 * Package:com.autotest.ui
 * Description:
 *
 * @Author huhuan
 * @Create 2024/6/21 14:49
 * @Version 1.0
 */
public class demo {
    @Test
    public void compareAA(){
        Assert.assertEquals("A","A");

    }
    public void compareAB(){
        Assert.assertEquals("AB","A");

    }
}

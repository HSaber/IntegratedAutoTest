package com.autotest.api.asserts;

import org.testng.Assert;

import java.util.ArrayList;

public class ArrayAssert {

    public static void ArrayEqualInAnyOrder(ArrayList<Object> actual,ArrayList<Object> expect){

        Assert.assertEquals(actual.size(),expect.size(),"ArrayList<Object> actual,ArrayList<Object> expect的size不一致！");

        boolean f2;
        for (Object subExpect : expect) {
            f2=false;
            for (Object subActual : actual) {
                if(subExpect.equals(subActual)){
                    f2=true;
                    actual.remove(subActual);
                    break;
                }
            }
            Assert.assertTrue(f2,"期望值"+subExpect+"在实际数组中找不到！");
        }
    }
}

package com.autotest.api.util;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/*
*在使用HttpDelete执行DELETE操作的时候，发现HttpDelete不支持setEntity方法，所以不能携带body信息。
其原因是在HttpMethods中，包含HttpGet, HttpPost, HttpPut, HttpDelete等类来实现http的常用操作。其中，HttpPost继承自HttpEntityEnclosingRequestBase，HttpEntityEnclosingRequestBase类又实现了HttpEntityEnclosingRequest接口，实现了setEntity的方法。 而HttpDelete继承自HttpRequestBase，没有实现setEntity的方法，因此无法设置HttpEntity对象。
我们的解决方案是重写一个MyHttpDelete类，继承自HttpEntityEnclosingRequestBase，覆盖其中的getMethod方法，从而返回DELETE。
 *  */
public class MyHttpDelete extends HttpEntityEnclosingRequestBase{
    public static final String METHOD_NAME = "DELETE";
    public MyHttpDelete(final String uri) {
        super();
        setURI(URI.create(uri));
    }
    public MyHttpDelete(final URI uri) {
        super();
        setURI(uri);
    }
    public MyHttpDelete() {
        super();
    }
    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}

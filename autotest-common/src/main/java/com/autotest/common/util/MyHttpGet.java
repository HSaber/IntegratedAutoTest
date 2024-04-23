package com.autotest.common.util;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;
/*
   重写一个MyHttpDelete/MyHttpGet类，继承自HttpEntityEnclosingRequestBase，覆盖其中的getMethod方法，从而返回GET
 */
public class MyHttpGet extends HttpEntityEnclosingRequestBase{
    public static final String METHOD_NAME = "GET";
    public MyHttpGet(final String uri) {
        super();
        setURI(URI.create(uri));
    }
    public MyHttpGet(final URI uri) {
        super();
        setURI(uri);
    }
    public MyHttpGet() {
        super();
    }
    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
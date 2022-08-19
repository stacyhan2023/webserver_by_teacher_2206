package com.webserver.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * URLDecoder是JAVA提供的一个API,可以将URL地址中%XX的内容进行解码
 */
public class URLDecoderDemo {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String line = "/loginUser?username=%E8%8C%83%E4%BC%A0%E5%A5%87&password=123456";
        line = URLDecoder.decode(line,"UTF-8");
        System.out.println(line);

    }
}

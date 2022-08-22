package com.webserver.test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * java.io.ByteArrayOutputStream
 * 这是一个低级的字节输出流.其内部维护一个字节数组,通过该流写出的所有字节最终都会
 * 保存在其内部维护的字节数组中
 */
public class BAOSDemo {
    public static void main(String[] args) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                out,
                                StandardCharsets.UTF_8
                        )
                ),true
        );
        pw.println("hello");
        byte[] data = out.toByteArray();
        System.out.println(data.length);
        System.out.println(Arrays.toString(data));
    }
}

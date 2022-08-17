package com.webserver.test;

import java.util.Arrays;

public class SplitDemo {
    public static void main(String[] args) {
        String line = "=1=2=3=4=5=6=====";
        //limit:2表示拆分出两项,当后面还有可拆分项时会忽略,吧第一次拆分后第二部分作为数组第二个元素
        String[] data = line.split("=",2);
        //data:[1, 2=3=4=5=6=====]
        System.out.println(Arrays.toString(data));
        //data:[1, 2, 3=4=5=6=====]
        data = line.split("=",3);

        data = line.split("=",10);
        System.out.println(Arrays.toString(data));
        //和split("=")效果一致
        data = line.split("=",0);
        System.out.println(Arrays.toString(data));
        //若字符串末尾连续匹配可拆分项,那么所有拆分出来的空字符串全部保留
        data = line.split("=",-1);
        System.out.println(Arrays.toString(data));

    }
}

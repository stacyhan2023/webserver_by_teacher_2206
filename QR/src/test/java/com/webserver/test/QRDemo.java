package com.webserver.test;

import qrcode.QRCodeUtil;

public class QRDemo {
    public static void main(String[] args) throws Exception {
        String line = "今天是第二阶段的最后一节课~";
        /*
            encode(String content,String descPath)
            参数1:二维码上表示的字符串内容
            参数2:二维码生成的位置
         */
//        QRCodeUtil.encode(line,"./qr.jpg");
        /*
            encode(String content,String logoImg,String descPath,boolean need)
            参数1:二维码上表示的字符串内容
            参数2:logo图片的位置
            参数3:二维码生成的位置
            参数4:logo图片是否压缩尺寸在二维码中央(当logo图片尺寸大是需要压缩)
         */
        QRCodeUtil.encode(line,"./logo.jpg","./qr.jpg",true);
        System.out.println("二维码生成完毕!");
    }
}

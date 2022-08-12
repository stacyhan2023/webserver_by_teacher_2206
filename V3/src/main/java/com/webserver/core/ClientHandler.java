package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 与客户端完成一次HTTP的交互
 * 按照HTTP协议要求，与客户端完成一次交互流程为一问一答
 * 因此，这里分为三步完成该工作:
 * 1:解析请求  目的:将浏览器发送的请求内容读取并整理
 * 2:处理请求  目的:根据浏览器的请求进行对应的处理工作
 * 3:发送响应  目的:将服务端的处理结果回馈给浏览器
 *
 *
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }
    public void run(){
        try {
            InputStream in = socket.getInputStream();
            /*
                http://localhost:8088/index.html
                请求行:GET /index.html HTTP/1.1

                http://localhost:8088
                请求行:GET / HTTP/1.1
             */
            int d;
            StringBuilder builder = new StringBuilder();
            char pre='a',cur='a';//pre记录上次读取的字符 cur记录本次读取的字符
            while((d = in.read())!=-1){
                cur = (char)d;
                if(pre==13&&cur==10){//判断是否连续读取到了回车+换行
                    break;
                }
                builder.append(cur);
                pre = cur;
            }
            String line = builder.toString().trim();
            System.out.println("请求行:"+line);

            //请求行相关信息
            String method;//请求方式
            String uri;//抽象路径
            String protocol;//协议版本
            /*
                GET /index.html HTTP/1.1
                data:{GET, /index.html, HTTP/1.1}
                提示:\s在正则表达式中表示一个空白字符
             */
            String[] data = line.split("\\s");
            method = data[0];
            uri = data[1];
            protocol = data[2];


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

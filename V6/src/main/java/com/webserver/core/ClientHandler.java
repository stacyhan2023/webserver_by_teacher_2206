package com.webserver.core;

import com.webserver.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
            //1解析请求,实例化请求对象的过程就是解析的过程
            HttpServletRequest request = new HttpServletRequest(socket);

            //2处理请求
            String path = request.getUri();
            System.out.println("请求的抽象路径:"+path);


            //3发送响应

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

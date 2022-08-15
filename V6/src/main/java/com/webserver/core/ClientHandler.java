package com.webserver.core;

import com.webserver.http.HttpServletRequest;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
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
    private static File dir;
    private static File staticDir;

    static {
        //定位环境变量ClassPath(类加载路径)中"."的位置
        //在IDEA中执行项目时,类加载路径是从target/classes开始的
        try {
            dir = new File(
                    ClientHandler.class.getClassLoader()
                            .getResource(".").toURI()
            );
            //定位target/classes/static目录
            staticDir = new File(dir,"static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


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
            //本版本测试,将resources下的static目录中的index.html页面响应给浏览器
            //实际虚拟机执行是查看的是target/class目录下的内容
            //maven项目编译后会将src/main/java和src/main/resources下的内容合并放在target/classes下
            //因此我们实际要定位的是target/classes/static/index.html
            File file = new File(staticDir,"index.html");
            /*
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */
            OutputStream out = socket.getOutputStream();
            //发送状态行
            String line = "HTTP/1.1 200 OK";
            byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
            out.write(data);
            out.write(13);
            out.write(10);

            //发送响应头
            line = "Content-Type: text/html";//告诉浏览器正文类型
            data = line.getBytes(StandardCharsets.ISO_8859_1);
            out.write(data);
            out.write(13);
            out.write(10);

            line = "Content-Length: "+file.length();//告诉浏览器正文长度(单位字节)
            data = line.getBytes(StandardCharsets.ISO_8859_1);
            out.write(data);
            out.write(13);
            out.write(10);
            //单独发送个回车+换行表示响应头发送完毕
            out.write(13);
            out.write(10);

            //发送响应正文(index.html页面的所有数据)
            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[1024*10];//10kb
            int len = 0;//记录每次实际读取的字节数
            while( (len = fis.read(buf)) != -1  ){
                out.write(buf,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //按照HTTP协议要求,一问一答后断开连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}

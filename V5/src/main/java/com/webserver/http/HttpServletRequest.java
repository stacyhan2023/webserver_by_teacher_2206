package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequest {
    private Socket socket;
    public HttpServletRequest(Socket socket) throws IOException {
        this.socket = socket;

        String line = readLine();
        System.out.println("请求行:"+line);

        //请求行相关信息
        String method;//请求方式
        String uri;//抽象路径
        String protocol;//协议版本
        String[] data = line.split("\\s");
        method = data[0];
        uri = data[1];//这里可能出现数组下标越界异常ArrayIndexOutOfBoundsException,原因是浏览器的问题！！！后期我们解决。建议:浏览器测试时尽量不使用后退，前进这样的功能测试。
        protocol = data[2];
        System.out.println("method:"+method);
        System.out.println("uri:"+uri);
        System.out.println("protocol:"+protocol);

        //读取消息头
        Map<String,String> headers = new HashMap<>();
//            while(!(line = readLine()).isEmpty()){
        while(true) {
            line = readLine();
            if(line.isEmpty()){//如果单独读取回车+换行，readLine方法会返回空字符串
                break;
            }
            System.out.println("消息头:" + line);
            //将消息头按照冒号空格拆分为消息头的名字和值，并以key，value形式存入headers中
            data = line.split(":\\s");//Connection: keep-alive==>data:[Connection, keep-alive]
            headers.put(data[0],data[1]);
        }
        System.out.println("headers:"+headers);


    }

    private String readLine() throws IOException {
        //当对同一个socket调用多次getInputStream方法时，获取回来的输入流始终是同一条流
        InputStream in = socket.getInputStream();
        int d;
        StringBuilder builder = new StringBuilder();
        char pre='a',cur='a';
        while((d = in.read())!=-1){
            cur = (char)d;
            if(pre==13&&cur==10){
                break;
            }
            builder.append(cur);
            pre = cur;
        }
        return builder.toString().trim();
    }
}

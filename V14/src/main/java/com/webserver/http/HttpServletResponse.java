package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 响应对象
 * 该类的每一个实例用于表示一个HTTP协议要求的响应内容
 * 每个响应由三部分构成:
 * 状态行,响应头,响应正文
 */
public class HttpServletResponse {
    private Socket socket;

    //状态行相关信息
    private int statusCode = 200;
    private String statusReason = "OK";

    //响应头相关信息
    private Map<String,String> headers = new HashMap<>();

    //响应正文相关信息
    private File contentFile;//正文对应的实体文件

    public HttpServletResponse(Socket socket){
        this.socket = socket;
    }

    /**
     * 将当前响应对象内容按照标准的响应格式发送给客户端
     */
    public void response() throws IOException {
        //发送状态行
        sendStatusLine();
        //发送响应头
        sendHeaders();
        //发送响应正文
        sendContent();
    }
    //发送状态行
    private void sendStatusLine() throws IOException {
//        HTTP/1.1 200 OK
        println("HTTP/1.1" + " " + statusCode + " " + statusReason);
    }
    //发送响应头
    private void sendHeaders() throws IOException {
        /*
            headers:
            key             value
            Content-Type    text/html
            Content-Length  245
            ...             ...
         */
        Set<Map.Entry<String,String>> entrySet = headers.entrySet();
        for(Map.Entry<String,String> e: entrySet){
            String name = e.getKey();
            String value = e.getValue();
            println(name + ": " + value);
        }
        //单独发送个回车+换行表示响应头发送完毕
        println("");
    }
    //发送响应正文
    private void sendContent() throws IOException {
        OutputStream out = socket.getOutputStream();
        if(contentFile!=null) {
            FileInputStream fis = new FileInputStream(contentFile);
            byte[] buf = new byte[1024 * 10];//10kb
            int len = 0;//记录每次实际读取的字节数
            while ((len = fis.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        }
    }


    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);
        out.write(10);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;

        //添加用于说明正文的响应头Content-Type和Content-Length
        try {
            String contentType = Files.probeContentType(contentFile.toPath());
                /*
                    如果根据正文文件分析出了Content-Type的值则设置该响应头.
                    HTTP协议规定如果服务端发送的响应中没有包含这个头,就表明让浏览器自行判断
                    响应正文的内容类型.
                 */
            if(contentType!=null){
                addHeader("Content-Type",contentType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        addHeader("Content-Length",contentFile.length()+"");



    }

    /**
     * 添加一个响应头
     * @param name  响应头的名字
     * @param value 响应头的值
     */
    public void addHeader(String name,String value){
        headers.put(name,value);
    }
}

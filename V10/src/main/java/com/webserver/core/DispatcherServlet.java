package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.net.URISyntaxException;

/**
 * 用于完成一个http交互流程中处理请求的环节工作.
 *
 * 实际上这个类是Spring MVC框架提供的一个核心的类,用于和Web容器(Tomcat)整合,
 * 使得处理请求的环节可以由Spring MVC框架完成.
 */
public class DispatcherServlet {
    private static DispatcherServlet instance = new DispatcherServlet();
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

    private DispatcherServlet(){}

    public static DispatcherServlet getInstance(){
        return instance;
    }

    /**
     * 处理请求的方法
     * @param request 请求对象,通过这个对象可以获取来自浏览器提交的内容
     * @param response 响应对象,通过设置响应对象将处理结果最终发送给浏览器
     */
    public void service(HttpServletRequest request, HttpServletResponse response){
        String path = request.getUri();
        System.out.println("请求的抽象路径:"+path);
        File file = new File(staticDir,path);
        if(file.isFile()){//浏览器请求的资源是否存在且是一个文件
            //正确响应其请求的文件
            response.setContentFile(file);
            //添加用于说明正文的响应头Content-Type和Content-Length
            response.addHeader("Content-Type","text/html");
            response.addHeader("Content-Length",file.length()+"");

        }else{
            //响应404
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir,"/root/404.html");
            response.setContentFile(file);
            response.addHeader("Content-Type","text/html");
            response.addHeader("Content-Length",file.length()+"");
        }
    }
}



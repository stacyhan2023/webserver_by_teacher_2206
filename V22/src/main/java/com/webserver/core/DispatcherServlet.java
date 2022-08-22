package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

/**
 * DispatcherServlet是Spring MVC框架提供的一个核心类，用于和底层容器TOMCAT整合使用。
 * 通过它使得程序员在写业务类时，无需再关注请求是如何调用到对应的业务处理类(某Controller)中
 * 对应的处理方法(被@RequestMapping注解标注的方法)，其会根据请求自动调用。
 * 这里忽略了Tomcat和SpringMVC框架整合的细节，直接使用该类完成核心业务逻辑的剖析。
 */
public class DispatcherServlet {
    private static DispatcherServlet servlet;
    private static File rootDir;
    private static File staticDir;

    static {
        servlet = new DispatcherServlet();
        try {
            //定位到:target/classes
            rootDir = new File(
                    DispatcherServlet.class.getClassLoader().getResource(".").toURI()
            );
            //定位static目录
            staticDir = new File(rootDir, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private DispatcherServlet() {
    }


    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //不能直接使用uri作为请求路径处理了，因为可能包含参数，而参数内容不是固定信息。
        String path = request.getRequestURI();

        //根据请求路径判断是否为处理某个业务
        try {
            Method method = HandlerMapping.getMethod(path);//path:/userList
            if(method!=null){
                method.invoke(method.getDeclaringClass().newInstance(),ParameterUtil.getParameterValues(method,request,response));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        File file = new File(staticDir, path);
        System.out.println("该页面是否存在:" + file.exists());

        if (file.isFile()) {//用户请求的资源在static目录下存在且是一个文件
            response.setContentFile(file);
        } else {
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            response.setContentFile(new File(staticDir, "/root/404.html"));
        }

        //测试添加其它响应头
        response.addHeader("Server", "WebServer");

    }

    public static DispatcherServlet getInstance() {
        return servlet;
    }

}

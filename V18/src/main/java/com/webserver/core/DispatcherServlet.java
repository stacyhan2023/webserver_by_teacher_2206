package com.webserver.core;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;
import com.webserver.controller.UserController;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

/**
 * 用于完成一个http交互流程中处理请求的环节工作.
 * <p>
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
                    DispatcherServlet.class.getClassLoader()
                            .getResource(".").toURI()
            );
            //定位target/classes/static目录
            staticDir = new File(dir, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private DispatcherServlet() {
    }

    public static DispatcherServlet getInstance() {
        return instance;
    }

    /**
     * 处理请求的方法
     *
     * @param request  请求对象,通过这个对象可以获取来自浏览器提交的内容
     * @param response 响应对象,通过设置响应对象将处理结果最终发送给浏览器
     */
    public void service(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getRequestURI();
        System.out.println("请求的抽象路径:" + path);
        //path====>/regUser
        //首先判断该请求是否为请求一个业务
        /*
            当我们得到本次请求路径path的值后，我们首先要查看是否为请求业务:
            1:扫描controller包下的所有类
            2:查看哪些被注解@Controller标注的过的类(只有被该注解标注的类才认可为业务处理类)
            3:遍历这些类，并获取他们的所有方法，并查看哪些时业务方法
              只有被注解@RequestMapping标注的方法才是业务方法
            4:遍历业务方法时比对该方法上@RequestMapping中传递的参数值是否与本次请求
              路径path值一致?如果一致则说明本次请求就应当由该方法进行处理
              因此利用反射机制调用该方法进行处理。
            5:如果扫描了所有的Controller中所有的业务方法，均未找到与本次请求匹配的路径
              则说明本次请求并非处理业务，那么执行下面请求静态资源的操作
         */
        try {
            File controllerDir = new File(dir,"/com/webserver/controller");
            File[] subs = controllerDir.listFiles(f->f.getName().endsWith(".class"));
            for(File sub : subs){//遍历目录中所有的.class文件
                String fileName = sub.getName();//获取文件名
                String className = fileName.substring(0,fileName.indexOf("."));//根据文件名截取出类名
                Class cls = Class.forName("com.webserver.controller."+className);//加载类对象
                if(cls.isAnnotationPresent(Controller.class)){//判断这个类是否被@Controller注解标注
                    Method[] methods = cls.getDeclaredMethods();//获取这个类定义的所有方法
                    for(Method method : methods){//遍历每一个方法
                        if(method.isAnnotationPresent(RequestMapping.class)){//判断该方法是否被@RequestMapping注解标注
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);//获取该方法上的注解@RequestMapping
                            String value = rm.value();//获取该注解上定义的参数
                            if(value.equals(path)){//判断注解参数是否与本次请求路径一致
                                method.invoke(cls.newInstance(),request,response);//反射调用该方法
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        File file = new File(staticDir, path);
        if (file.isFile()) {//浏览器请求的资源是否存在且是一个文件
            //正确响应其请求的文件
            response.setContentFile(file);
        } else {
            //响应404
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir, "/root/404.html");
            response.setContentFile(file);
        }
    }
}



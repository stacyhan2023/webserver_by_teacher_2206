package com.webserver.core;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 维护请求路径对对应的业务处理方法(某个Controller的某个方法)
 */
public class HandlerMapping {
    /*
        key:请求路径 例如:/regUser
        value:方法对象(Method实例)  例如:表示Controller的reg方法的Method对象
     */
    private static Map<String, Method> mapping = new HashMap<>();
    static {
        initMapping();
    }
    private static void initMapping(){
        try {
            File dir = new File(
                    HandlerMapping.class.getClassLoader()
                            .getResource(".").toURI()
            );
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

                            mapping.put(value,method);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据请求路径返回对应的处理方法
     * @param path
     * @return
     */
    public static Method getMethod(String path){
        return mapping.get(path);
    }

    public static void main(String[] args) {
        Method method = mapping.get("/regUser");
        //通过方法对象可以获取其所属的类的类对象
        Class cls = method.getDeclaringClass();
        System.out.println(cls);
        System.out.println(method);
    }
}

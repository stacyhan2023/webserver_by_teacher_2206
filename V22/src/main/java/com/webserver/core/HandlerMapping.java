package com.webserver.core;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
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

    private static void initMapping() {
        try {
            File dir = new File(
                    HandlerMapping.class.getClassLoader()
                            .getResource(".").toURI()
            );
            Files.walkFileTree(dir.toPath(), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        File sub = file.toFile();
                        if (sub.getName().endsWith(".class")) {
                            String path = sub.getCanonicalPath().replace(dir.getCanonicalPath() + File.separator, "");
                            while (path.contains(File.separator)) {
                                path = path.replace(File.separator, ".");
                            }
                            path = path.substring(0,path.indexOf(".class"));
                            Class cls = Class.forName(path);//加载类对象
                            if (cls.isAnnotationPresent(Controller.class)) {//判断这个类是否被@Controller注解标注
                                Method[] methods = cls.getDeclaredMethods();//获取这个类定义的所有方法
                                for (Method method : methods) {//遍历每一个方法
                                    if (method.isAnnotationPresent(RequestMapping.class)) {//判断该方法是否被@RequestMapping注解标注
                                        RequestMapping rm = method.getAnnotation(RequestMapping.class);//获取该方法上的注解@RequestMapping
                                        String value = rm.value();//获取该注解上定义的参数

                                        mapping.put(value, method);
                                    }
                                }
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return null;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据请求路径返回对应的处理方法
     *
     * @param path
     * @return
     */
    public static Method getMethod(String path) {
        return mapping.get(path);
    }

    public static void main(String[] args) {
        Method method = mapping.get("/ee");
        //通过方法对象可以获取其所属的类的类对象
        Class cls = method.getDeclaringClass();
        System.out.println(cls);
        System.out.println(method);
    }
}

package com.webserver.test;

import sun.misc.Launcher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassLoaderDemo {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
//        Class cls = Class.forName("javax.activation.SecuritySupport");
//        Method m = cls.getDeclaredMethod("getContextClassLoader");
//        m.setAccessible(true);
//        ClassLoader loader = (ClassLoader)m.invoke(null);
//        loader =  loader.getParent();
//        Launcher.getBootstrapClassPath();
//        System.out.println(loader);
        // 获取当前项目路径, Main是当前类, 可以指定其它类, 这个无所谓
//        String pathname = ClassLoaderDemo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String javaHome = System.getProperty ("java.home");
        System.out.println("当前路径: " + javaHome);

// 使用JarFile打开jar文件
        JarFile jar = null;
        try {
            jar = new JarFile(javaHome+ File.separator+"lib"+File.separator+"rt.jar");
            JarEntry je = jar.getJarEntry("java/lang/String.class");

            Enumeration<JarEntry> entryEnumeration = jar.entries();
            while (entryEnumeration.hasMoreElements()) {
                // 获取JarEntry对象
                JarEntry entry = entryEnumeration.nextElement();
                // 获取当前JarEntry对象的路径+文件名
                System.out.println(entry.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭jar文件
                jar.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

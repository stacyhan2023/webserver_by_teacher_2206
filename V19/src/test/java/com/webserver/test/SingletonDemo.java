package com.webserver.test;

/**
 * 单例模式
 * java23中设计模式之一.
 * 使用该模式后,当前类全局只能有一个实例
 */
public class SingletonDemo {
    public static void main(String[] args) {
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();

        System.out.println("s1:"+s1);
        System.out.println("s2:"+s2);
    }
}

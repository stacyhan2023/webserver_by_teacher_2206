package com.webserver.controller;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;
import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;
import java.net.URISyntaxException;

/**
 * 处理和用户相关的业务类
 */
@Controller
public class UserController {
    private static File userDir;
    static {
        userDir = new File("./users");
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
    }

    @RequestMapping("/loginUser")
    public void login(HttpServletRequest request,HttpServletResponse response){
        //1
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if(username==null||username.isEmpty()||password==null||password.isEmpty()){
            response.sendRedirect("/login_info_error.html");
            return;
        }

        //2
        //根据登录用户的用户名去users目录下寻找该用户信息
        File file = new File(userDir,username+".obj");
        if(file.exists()){//文件存在则说明该用户存在(用户名输入正确)
            //反序列化文件中该用户曾经的注册信息
            try (
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                User user = (User)ois.readObject();
                //比较登录密码和注册时输入的密码是否一致
                // a = "abc123"    b = "AbC123"
                //a.equals(b) ==> false
                //a.equalsIgnoreCase(b) ==> true
                if(user.getPassword().equals(password)){
                    //登录成功
                    response.sendRedirect("/login_success.html");
                    return;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        //登录失败
        response.sendRedirect("/login_fail.html");
    }

    @RequestMapping("/regUser")
    public void reg(HttpServletRequest request, HttpServletResponse response) {
        //1获取表单信息
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        System.out.println(username + "," + password + "," + nickname + "," + ageStr);

        if (username == null || username.isEmpty() || password == null || password.isEmpty() ||
                nickname == null || nickname.isEmpty() || ageStr == null || ageStr.isEmpty() ||
                !ageStr.matches("[0-9]+")) {
            response.sendRedirect("/reg_info_error.html");
            return;
        }

        int age = Integer.parseInt(ageStr);
        User user = new User(username, password, nickname, age);

        File file = new File(userDir, username + ".obj");
        if (file.exists()) {//文件已经存在说明是重复用户
            response.sendRedirect("/have_user.html");
            return;
        }

        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/reg_success.html");

    }
}

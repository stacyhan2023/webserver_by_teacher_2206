package com.webserver.controller;

import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;

/**
 * 处理和用户相关的业务类
 */
public class UserController {
    private static File userDir;
    static {
        userDir = new File("./users");
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
    }


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

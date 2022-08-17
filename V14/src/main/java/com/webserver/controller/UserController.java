package com.webserver.controller;

import com.webserver.core.ClientHandler;
import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;
import java.net.URISyntaxException;

/**
 * 处理和用户相关的业务类
 */
public class UserController {
    private static File userDir;
    private static File dir;
    private static File staticDir;

    static {
        userDir = new File("./users");
        if (!userDir.exists()) {
            userDir.mkdirs();
        }

        //定位环境变量ClassPath(类加载路径)中"."的位置
        //在IDEA中执行项目时,类加载路径是从target/classes开始的
        try {
            dir = new File(
                    UserController.class.getClassLoader()
                            .getResource(".").toURI()
            );
            //定位target/classes/static目录
            staticDir = new File(dir, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
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
            File file = new File(staticDir, "/reg_info_error.html");
            response.setContentFile(file);
            return;
        }

        int age = Integer.parseInt(ageStr);
        User user = new User(username, password, nickname, age);

        File file = new File(userDir, username + ".obj");
        if (file.exists()) {//文件已经存在说明是重复用户
            File haveUser = new File(staticDir, "/have_user.html");
            response.setContentFile(haveUser);
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
        File success = new File(staticDir,"/reg_success.html");
        response.setContentFile(success);

    }
}

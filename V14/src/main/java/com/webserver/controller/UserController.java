package com.webserver.controller;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

/**
 * 处理和用户相关的业务类
 */
public class UserController {
    public void reg(HttpServletRequest request, HttpServletResponse response){
        //1获取表单信息
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        System.out.println(username+","+password+","+nickname+","+ageStr);
    }
}

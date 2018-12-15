package com.heima.travel.admin;

import com.alibaba.fastjson.JSON;
import com.heima.travel.model.User;
import com.heima.travel.service.UserService;
import com.heima.travel.service.imp.UserServiceImp;
import com.heima.travel.utils.CommonsBean;
import com.heima.travel.web.BaseServlet;
import org.apache.commons.lang.StringUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/user")
public class UserManager extends BaseServlet {
    UserService userService = new UserServiceImp();

    //获取后台用户列表
    public void userList(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int curpage = 1;
        String curPageStr = request.getParameter("curPage");
        if (StringUtils.isNotEmpty(curPageStr)) {
            curpage = Integer.parseInt(curPageStr);
        }
        String json = userService.getUserList();
        response.getWriter().write(json);
    }

    //后台管理添加用户
    public void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = CommonsBean.populate(User.class, request);
        Map<String, String> map = validUserInfo(user);
        if (!"ok".equals(map.get("msg"))) {
            System.out.println("111111111111111111111");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }
        boolean b = userService.addUser(user);
        if (b) {
            map.put("msg", "success");
        } else {
            map.put("msg", "fail");
        }
        response.getWriter().write(JSON.toJSONString(map));
    }

    //用户信息验证
    public Map<String, String> validUserInfo(User user) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            map.put("msg", "用户名或密码不能为空");
        } else if (StringUtils.isEmpty(user.getEmail()) || StringUtils.isEmpty(user.getTelephone())) {
            map.put("msg", "邮箱或手机号不能为空");
        } else if (StringUtils.isEmpty(user.getBirthday())) {
            map.put("message", "生日不能为空");
        } else if (!userService.checkUserName(user.getUsername())) {
            map.put("msg", "用户名已存在");
        } else if (!userService.checkEmail(user.getEmail())) {
            map.put("msg", "邮箱已存在");
        } else {
            map.put("msg", "ok");
        }
        return map;
    }


    //删除用户
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        int uid = Integer.parseInt(request.getParameter("uid"));
        userService.deleteUserByUid(uid);
    }

    @SuppressWarnings("all")
    //adminlogin
    public void adminLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        System.out.println("email = " + email);
        String pwd = request.getParameter("password");
        System.out.println("登录密码" + pwd);
        Map<String, String> jsonMap = new HashMap<>();
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(pwd)) {
            jsonMap.put("loginMsg", "用户名或密码不能为空");
        } else if (userService.validUserIsadmin(email)) {
            jsonMap.put("loginMsg", "非法登录");
        } else {
            User user = userService.adminLogin(email, pwd);
            if (user != null) {
                //登录成功
                jsonMap.put("loginMsg", "success");
                request.getSession().setAttribute("user", user);
            } else {
                jsonMap.put("loginMsg", "登录失败,用户名或密码不正确");
            }
        }
        String jsonString = JSON.toJSONString(jsonMap);
        response.getWriter().print(jsonString);
    }

    //修改用户信息
    public void changeUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = CommonsBean.populate(User.class, request);
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            map.put("msg", "用户名或密码不能为空");
        } else {
            boolean b = userService.changeUser(user);
            if (b) {
                map.put("msg", "success");
            } else {
                map.put("msg", "fail");
            }

        }
        response.getWriter().write(JSON.toJSONString(map));

    }


}


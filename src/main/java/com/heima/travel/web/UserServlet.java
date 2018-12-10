package com.heima.travel.web;

import com.alibaba.fastjson.JSON;
import com.heima.travel.model.User;
import com.heima.travel.service.UserService;
import com.heima.travel.utils.BeansUtil;
import com.heima.travel.utils.CommonsBean;
import com.heima.travel.utils.MailUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@WebServlet("/userServlet")
public class UserServlet extends BaseServlet {
    private UserService userService = (UserService) BeansUtil.getBean("userService");

    //修改密码
    public void changePwd(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String email_code = request.getParameter("verify");
        String newPwd = request.getParameter("newPwd");

        String sesion_emai_code = (String) request.getSession().getAttribute(email);
        if (StringUtils.isNotEmpty(sesion_emai_code) && sesion_emai_code.equals(email_code)) {
            userService.changePwd(email, newPwd);
            response.getWriter().print("success");
        } else {
            response.getWriter().print("fail");

        }
    }

    //邮箱发送改密验证码,
    public void sendemailcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        System.out.println("email = " + email);
        String email_code = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int num = random.nextInt(10);
            email_code += num;
        }
        System.out.println(email_code);
        request.getSession().setAttribute(email, email_code);
        try {
            MailUtil.sendEmail(email, "<h2>黑马旅游网修改密码的验证码:" + email_code + "</h2>");
            System.out.println("发送邮箱验证了");
        } catch (EmailException e) {
            response.getWriter().print("发送失败");
            e.printStackTrace();
        }
        response.getWriter().print("发送成功");
    }


    //用户退出
    public void loginOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute("user");//删除session中用户信息
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        response.getWriter().print("退出成功");
    }

    //获取用户信息,判断是否登录
    public void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Map<String, String> jsonmap = new HashMap<>();
        if (user != null) {
            jsonmap.put("emsg", "exist");  //session中存在用户信息
            jsonmap.put("username", user.getUsername());
        } else {
            jsonmap.put("emsg", "noExist"); //session 中不存在用户信息
        }
        String jsonString = JSON.toJSONString(jsonmap);
        response.getWriter().print(jsonString);
    }

    //用户登录
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        System.out.println("email = " + email);
        String pwd = request.getParameter("password");
        String verifyCode = request.getParameter("check");//获取验证码
        String autologin = request.getParameter("autologin"); //获取是否勾选自动登录的CheckBox
        Map<String, String> jsonMap = new HashMap<>();
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(pwd)) {
            jsonMap.put("loginMsg", "用户名或密码不能为空");
        } else if (StringUtils.isEmpty(verifyCode)) {
            jsonMap.put("loginMsg", "验证码不能为空");
        } else if (!((String) request.getSession().getAttribute("code")).equalsIgnoreCase(verifyCode)) {
            jsonMap.put("loginMsg", "验证码错误");
        } else {
            User user = userService.userLogin(email, pwd);
            if (user != null) {
                //登录成功
                jsonMap.put("loginMsg", "success");
                request.getSession().setAttribute("user", user);
                Cookie uidCk = new Cookie("uid", user.getUid() + "");
                Cookie unameCk = new Cookie("username", user.getUsername());
                Cookie upwd = new Cookie("pwd", user.getPassword());
                if (StringUtils.isNotEmpty(autologin)) {//勾选了自动登录
                    uidCk.setMaxAge(60 * 60 * 24);
                    unameCk.setMaxAge(60 * 60 * 24);
                    upwd.setMaxAge(60 * 60 * 24);
                }
                response.addCookie(uidCk);
                response.addCookie(unameCk);
                response.addCookie(upwd);
            } else {
                jsonMap.put("loginMsg", "登录失败,用户名或密码不正确");
            }
        }
        String jsonString = JSON.toJSONString(jsonMap);
        response.getWriter().print(jsonString);
    }

    //用户激活
    public void active(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        boolean b = userService.userActive(code);
        response.getWriter().print("激活成功");
    }


    //用户注册
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = CommonsBean.populate(User.class, request);
        Map<String, String> jsonMap = new HashMap<>();
        int res = checkRegistForm(user); //表单关键字段非空和格式验证方法
        //在这个post技术满天飞的年代,后台验证是必须的,别人注册你网站根本不需要走浏览器
        if (res > 0) {
            jsonMap.put("registFlag", res + "");
            jsonMap.put("msg", "error");
        } else if (!userService.checkUserName(user.getUsername())) {
            jsonMap.put("registFlag", "5");
            jsonMap.put("msg", "error");  //用户名是否存在验证
        } else if (!userService.checkEmail(user.getEmail())) {
            jsonMap.put("registFlag", "6");
            jsonMap.put("msg", "error");  //邮箱是否存在验证
        } else if (((String) request.getSession().getAttribute("code")).equalsIgnoreCase(user.getCheck())) {
            System.out.println(user.getCheck());  //图片验证码校验
            boolean result = userService.register(user);//调用service层的注册功能 ,到这里就直接注册了,数据库添加数据一般都能成功不做判断了,
            System.out.println("result = " + result);
            if (result) {
                jsonMap.put("registFlag", "0");
                jsonMap.put("msg", "success");
                request.getSession().removeAttribute("code");//注册成功清除session中的验证码
            } else {
                jsonMap.put("msg", "error");
            }

        } else {
            jsonMap.put("registFlag", "7");  //图片验证码错误的信息设置
            jsonMap.put("msg", "error");
        }
        String jsonString = JSON.toJSONString(jsonMap);  //返回json数据,让前端去判断吧
        response.getWriter().print(jsonString);

    }


    //Register form check
    public int checkRegistForm(User user) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return 1;
        }
        if (StringUtils.isEmpty(user.getEmail()) || StringUtils.isEmpty(user.getTelephone())) {
            return 2;
        } else if (!user.getEmail().matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")
                || !user.getTelephone().matches("1[3,4,5,8]\\d[\\s,-]?\\d{4}[\\s,-]?\\d{4}+")) {
            return 3;
        }

        if (StringUtils.isEmpty(user.getCheck())) {
            return 4;
        }
        if (StringUtils.isEmpty(user.getBirthday())) {
            return 8;
        }
        return 0;
    }

    /*
     * 邮箱检查
     * */
    public void checkEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        request.setCharacterEncoding("utf-8");
        System.out.println(email);
        if (email == null || "".equals(email)) {
            response.getWriter().print("false");
            return;
        }
        if (!email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
            response.getWriter().print("false");
            return;
        }
        boolean b = userService.checkEmail(email);

        response.getWriter().print(b);


    }

    //用户名检查
    public void checkUName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");

        String username = request.getParameter("username");
        if (null == username || "".equals(username)) {
            response.getWriter().print("false");
            return;
        }
        boolean b = userService.checkUserName(username);
        response.getWriter().print(b);
    }


    //验证码检查
    public void checkVerify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        String check = request.getParameter("check");
        String code = (String) request.getSession().getAttribute("code");
        if (code.equalsIgnoreCase(check)) {
            response.getWriter().print("true");
        } else {
            response.getWriter().print("false");
        }


    }


}

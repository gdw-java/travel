package com.heima.travel.common;

import com.heima.travel.model.User;
import org.apache.commons.lang.StringUtils;

import javax.lang.model.SourceVersion;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class CheckLoginFilter implements Filter {
    public void destroy() {
    }
    //判断用户是否登录
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        //your code
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user=new User();
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (int i = 0; i < cookies.length; i++) {
                    String name = cookies[i].getName();
                    String value=cookies[i].getValue();
                    if ("uid".equals(name)) {
                        user.setUid(Integer.parseInt(value));
                    }
                    if ("username".equals(name)) {
                        user.setUsername(value);
                    }
                    if ("pwd".equals(name)) {
                        user.setPassword(value);
                    }
                }
                if (StringUtils.isNotEmpty(user.getUsername()) && StringUtils.isNotEmpty(user.getUid() + "")) {
                    session.setAttribute("user",user);
                }
            }
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}

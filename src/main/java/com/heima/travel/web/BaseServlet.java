package com.heima.travel.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@WebServlet("/baseServlet")
public class BaseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method=request.getParameter("method");
        System.out.println(request.getParameter("email"));
        try {
            Method methodName = this.getClass().getDeclaredMethod(method,HttpServletRequest.class,HttpServletResponse.class);
            methodName.setAccessible(true);
            methodName.invoke(this,request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

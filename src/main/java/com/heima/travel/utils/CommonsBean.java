package com.heima.travel.utils;

import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;

public class CommonsBean {
    public static <T> T populate(Class<T> clazz, HttpServletRequest request) {
        Object obj=null;
        try {
            obj=clazz.newInstance();
            BeanUtils.populate(obj,request.getParameterMap());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (T) obj;
    }
}

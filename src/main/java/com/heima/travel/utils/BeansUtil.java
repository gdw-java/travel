package com.heima.travel.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BeansUtil {
    private static Map<String, Object> beanMap = new HashMap<>();
    private static ResourceBundle rb = null;

    static {
        rb = ResourceBundle.getBundle("beans");
    }

    public static Object getBean(String beanName) {
        Object obj = null;
        obj = beanMap.get(beanName);
        if (obj != null) {
            return obj;
        } else {
            String rbString = rb.getString(beanName);
            try {
                Class<?> clazz = Class.forName(rbString);
                obj = clazz.newInstance();
                beanMap.put(beanName, obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;
        }

    }
}



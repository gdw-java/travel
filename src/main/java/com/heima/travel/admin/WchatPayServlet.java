package com.heima.travel.admin;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.heima.travel.web.BaseServlet;
import com.heima.travel.wxPayConfig.MyConfig;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@WebServlet("/wchatPay")
public class WchatPayServlet extends BaseServlet {
    public void wx(HttpServletRequest req, HttpServletResponse resp) {
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "黑马旅游支付支付");
        //订单号
        Random random = new Random();
        String str = "";
        for (int i = 0; i < 15; i++) {
            int i1 = random.nextInt(10);
            str += i1 + "";
        }
        data.put("out_trade_no", str);
        data.put("device_info", "");
        //默认人民币
        data.put("fee_type", "CNY");
        //标价金额 分为单位
        data.put("total_fee", "1");
        data.put("spbill_create_ip", "123.12.12.123");
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        data.put("product_id", "12");
        try {
            Map<String, String> m = wxpay.unifiedOrder(data);//获取支付信息,支付url等
            HashMap<Object, Object> hashMap = new HashMap<>();
            hashMap.put("url", m.get("code_url"));
            hashMap.put("ding", str);
            String s = JSON.toJSONString(hashMap);
            resp.getWriter().write(s);
            System.out.println(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void querywx(HttpServletRequest req, HttpServletResponse resp) {
        MyConfig myConfig = new MyConfig();
        WXPay wxPay = new WXPay(myConfig);
        String oid = req.getParameter("oid");

        HashMap<String, String> data = new HashMap<>();
        data.put("out_trade_no", oid);
        String state = "";
        try {
            Map<String, String> map = wxPay.orderQuery(data);
            state = map.get("trade_state");
            if ("SUCCESS".equals(state)) {
                resp.getWriter().write("success");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

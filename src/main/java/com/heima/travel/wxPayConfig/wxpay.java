package com.heima.travel.wxPayConfig;

import com.github.wxpay.sdk.WXPay;

import java.util.HashMap;
import java.util.Map;

public class wxpay {
    public static void main(String[] args) throws Exception {

        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "estore商城支付");
        //订单号
        data.put("out_trade_no", "201609092312311059593000012");
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
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

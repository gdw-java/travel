package com.heima.travel.wxPayConfig;

import com.github.wxpay.sdk.WXPay;

import java.util.HashMap;
import java.util.Map;

public class WXPayUtil {

    public static void main(String[] args) {

    }

    /**
     * �����������ڻ�ȡ֧���ĵ�ַ
     *
     * @param out_trade_no
     * @return
     */
    public static String pay(String out_trade_no) {
        //��ȡ΢��֧����������Ϣ
        MyConfig config = new MyConfig();
        //����΢��֧������
        WXPay wxpay = new WXPay(config);
        //����һ��map�����ڴ��֧������
        Map<String, String> data = new HashMap<String, String>();
        //֧����Ʒ����Ʒ����
        data.put("body", "estore�̳�֧������-΢��֧��");
        //�̻��Ķ����ţ����ǽ��׵Ķ���id
//        data.put("out_trade_no", "2016010910595900000012");
        data.put("out_trade_no", out_trade_no);
        //���׵ı��֣�����ң�CNY
        data.put("fee_type", "CNY");
        //�����ܽ���λΪ�֣�
        data.put("total_fee", "1");
        //�û���ip��ַ
        data.put("spbill_create_ip", "123.12.12.123");
        //֧���ɹ���Ļص���ַ������֪ͨ���÷�֧������Ϣ
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        data.put("trade_type", "NATIVE");  // �˴�ָ��Ϊɨ��֧��
        //��Ʒid�����������ɨ��֧����ʱ��ش��Ĳ���
        data.put("product_id", "12");
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
            //��΢��֧���ӿ����ɵ�֧����ַ����
            return resp.get("code_url");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("΢��֧���쳣");
        }
    }

    /**
     * ��ѯ������Ϣ�����ڻ�ȡ������֧��״̬
     *
     * @return SUCCESS��֧���ɹ� REFUND��ת���˿�
     * NOTPAY��δ֧��
     * CLOSED���ѹر�
     * REVOKED���ѳ�����ˢ��֧����
     * USERPAYING--�û�֧����
     * PAYERROR--֧��ʧ��(����ԭ�������з���ʧ��)
     */
    public static String search(String out_trade_no) {
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        //���׵ĵ���
        //data.put("out_trade_no", "2016010910595900000012");
        data.put("out_trade_no", out_trade_no);
        try {
            Map<String, String> resp = wxpay.orderQuery(data);
            System.out.println(resp);
            return resp.get("trade_state");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("��ѯ������Ϣ�쳣");
        }
    }
}

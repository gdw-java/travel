package com.heima.travel.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 邮件发送工具类
 */
public class MailUtil {
    private static String hostName = null;
    private static String authen_name = null;
    private static String authen_pwd = null;
    private static String charset = null;

    static {
        Properties p = new Properties();
        InputStream inputStream = MailUtil.class.getClassLoader().getResourceAsStream("mail.properties");
        try {
            p.load(inputStream);
            hostName = p.getProperty("host.name");
            authen_name = p.getProperty("authen.name");
            authen_pwd = p.getProperty("authen.pwd");
            charset = p.getProperty("charset");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取HtmlEmail对象
     * @return
     * @throws EmailException
     */
    public static HtmlEmail getHtmlEmail() throws EmailException {
        //创建HtmlEmail对象
        HtmlEmail htmlEmail = new HtmlEmail();
        htmlEmail.setHostName(hostName);
        htmlEmail.setAuthentication(authen_name, authen_pwd);
        htmlEmail.setFrom(authen_name);
        htmlEmail.setCharset(charset);
        return htmlEmail;
    }


    /**
     * 发送邮件
     * @param emailTo  收件人
     * @param emailMsg 邮件内容
     */
    public static void sendEmail(String emailTo, String emailMsg) throws EmailException {
        //创建HtmlEmail对象
        HtmlEmail htmlEmail = new HtmlEmail();
        //设置邮件服务器地址
        htmlEmail.setHostName(hostName);
        //设置账号和密码
        htmlEmail.setAuthentication(authen_name, authen_pwd);
        //设置发件人
        htmlEmail.setFrom(authen_name);
        //设置编码
        htmlEmail.setCharset(charset);

        //设置收件人
        htmlEmail.addTo(emailTo);
        //设置邮件主题
        htmlEmail.setSubject("【黑马旅游】");
        //设置邮件内容
        htmlEmail.setMsg(emailMsg);
        //发送邮件
        htmlEmail.send();
    }


    /**
     * 测试
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        MailUtil.sendEmail("58496573@qq.com", "<h2>恭喜您，注册成功！</h2><a href=http://localhost:8080/userServlet?requestMethod=active&code=123>请点击次链接进行激活</a>");
    }
}

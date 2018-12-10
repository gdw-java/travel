package com.heima.travel.service.imp;

import com.heima.travel.dao.UserDao;
import com.heima.travel.model.User;
import com.heima.travel.service.UserService;
import com.heima.travel.utils.BeansUtil;
import com.heima.travel.utils.Md5Util;
import com.heima.travel.utils.UuidUtil;
import org.apache.commons.lang.StringUtils;

import java.util.List;


public class UserServiceImp implements UserService {
    private UserDao userDao = (UserDao) BeansUtil.getBean("userDao");

    //用户登录
    @Override
    public User userLogin(String email, String pwd) {
        try {
            String userPwd = Md5Util.encodeByMd5(pwd);
            User user = userDao.lqueryUserLogin(email, userPwd);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    //用户激活
    @Override
    public boolean userActive(String code) {
        User user = userDao.userActive(code);
        if (user != null) {
            return true;
        } else
            return false;
    }

    //用户注册业务
    @Override
    public boolean register(User user) {
        String uuid = UuidUtil.getUuid();
        user.setCode(uuid);
        if (StringUtils.isEmpty(user.getBirthday())) {
            user.setBirthday("1970-01-01");
        }
        try {
            user.setPassword(Md5Util.encodeByMd5(user.getPassword())); //密码加密存储
        } catch (Exception e) {
            e.printStackTrace();
        }
        int row = userDao.addUser(user);
        System.out.println("row = " + row);
        if (row > 0) {
//            //注册成功发送激活邮件
//            try {
//                String link="http://localhost:8080/userServlet?method=active&code="+user.getCode();
//                MailUtil.sendEmail(user.getEmail(),"<h2>恭喜您，注册成功！</h2><a href="+link+">请点击次链接进行激活"+link+"</a>");
//            } catch (EmailException e) {
//                e.printStackTrace();
//            }
            return true;
        }
        return false;
    }

    //用户修改密码
    @Override
    public void changePwd(String email, String newPwd) {
        try {
            newPwd = Md5Util.encodeByMd5(newPwd);
            userDao.changePwdByEmai(email, newPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //检测邮箱是否存在
    @Override
    public boolean checkEmail(String email) {
        List<User> userlist = userDao.queryEmail(email);
        if (userlist.size() == 0) {
            return true; //数据库不存在这个邮箱
        } else {
            return false; //数据库已经存在这个邮箱
        }
    }

    //检测用户名是否存在
    @Override
    public boolean checkUserName(String username) {
        List<User> userlist = userDao.queryUserName(username);
        System.out.println(userlist.size());
        if (userlist.size() == 0) {
            return true;  //用户名不存在
        } else {
            return false; //用户名已存在
        }
    }


}

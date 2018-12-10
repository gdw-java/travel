package com.heima.travel.dao;

import com.heima.travel.model.User;

import java.util.List;

public interface UserDao {
    List<User> queryEmail(String email);

    List<User> queryUserName(String username);

    int addUser(User user);

    User userActive(String code);

    User lqueryUserLogin(String email, String pwd);

    void changePwdByEmai(String email, String newPwd);
}

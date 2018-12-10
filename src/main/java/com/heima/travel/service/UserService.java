package com.heima.travel.service;

import com.heima.travel.model.User;

public interface UserService {
    boolean checkEmail(String email);

    boolean checkUserName(String username);

    boolean register(User user);

    boolean userActive(String code);

    User userLogin(String email, String pwd);

    void changePwd(String email, String newPwd);
}

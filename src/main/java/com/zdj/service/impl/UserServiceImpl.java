package com.zdj.service.impl;

import com.zdj.service.UserService;

/**
 * @author zhangdanjiang
 * @description
 * @date 2018/11/14
 */
public class UserServiceImpl implements UserService {


    @Override
    public void addUser(String userName, String passWord) {
        System.out.println("add user:{userName="+userName+",passWord="+passWord+"}");
    }
}

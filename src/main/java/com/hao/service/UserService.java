package com.hao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hao.domain.User;


/**
* @author Xhao
* @description 针对表【user(用户信息)】的数据库操作Service
* @createDate 2022-11-22 21:43:36
*/
public interface UserService  extends IService<User> {

    void sendVerifyCode(String mail);

    boolean doVerify(String mail, String code);
}

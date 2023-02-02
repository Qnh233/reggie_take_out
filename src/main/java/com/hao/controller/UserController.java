package com.hao.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.common.R;
import com.hao.domain.User;
import com.hao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author 必燃
 * @version 1.0
 * @create 2023-01-18 19:48
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user)
    {
        log.info("发送验证码到{}",user.getPhone());
        userService.sendVerifyCode(user.getPhone());
        return R.success("OK");
    }

    @PostMapping("/login")
    public R<String> Userlogin(@RequestBody Map<String, String> map,HttpServletRequest request)
    {
        log.info("验证~");
        String phone = map.get("phone");
        String code = map.get("code");
        User user = new User();
        user.setPhone(phone);
        boolean b = userService.doVerify(phone, code);
        if(b)
        {
            LambdaQueryWrapper<User> QueryWrapper = new LambdaQueryWrapper<>();
            QueryWrapper.eq(User::getPhone,phone);
            User one = userService.getOne(QueryWrapper);
            Long id;
            if(one!=null)
            {
                 id = one.getId();
            }
            else
            {
                userService.save(user);
                User one1 = userService.getOne(QueryWrapper);
                id=one1.getId();
            }
            HttpSession session = request.getSession();
            session.setAttribute("user",id);
            return R.success("登录成功");
        }
        else
        {
            return R.error("验证码错误。");
        }

    }



}

package com.hao.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.domain.CodeCheck;
import com.hao.domain.User;
import com.hao.mapper.UserMapper;
import com.hao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Random;

/**
 * @author 必燃
 * @version 1.0
 * @create 2023-01-18 19:18
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    JavaMailSender sender;  //一个用于发送邮箱的类
    //    @Resource
//    StringRedisTemplate template;   //用于Redis数据库操作
    @Value("${spring.mail.username}")
    String email;

    @Override
    public void sendVerifyCode(String mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件标题
        message.setSubject("【吉瑞网站】您的注册码");
        //设置随机数作为验证码
        Random random = new Random();
        int code = random.nextInt(89999) + 10000;//小心机
//        //用redis保存验证码
//        //提前验证一下，如果发送了多次则删除上一次的验证码，保留最新的验证码
//        if (template.opsForValue().get("verify:code:"+mail)!=null){
//            template.delete("verify:code:"+mail);
//        }
//        template.opsForValue().set("verify:code:"+mail,code+"",5, TimeUnit.MINUTES);//设置五分钟过期时间
        //不使用微服务 使用本地记录
        CodeCheck.setcodes(code);
        //邮件内容
        message.setText("您的验证码是："+code+"，请及时完成注册。若不是本人操作请忽略");
        message.setFrom(email); //谁发送，必须和yaml文件中的账号一致
        message.setTo(mail);   //谁接收
//        sender.send(message);
        log.info("您的验证码是：{}",code);
    }

    @Override
    public boolean doVerify(String mail, String code) {
//        String string = template.opsForValue().get("verify:code:"+mail);
        Integer integer = Integer.valueOf(code);
        if (CodeCheck.getcodes()==integer){
//            integertemplate.delete("verify:code:"+mail);
            CodeCheck.removecode();
            return true;
        }
        return false;
    }
}

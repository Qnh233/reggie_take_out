package com.hao.common;

import com.hao.exception.BusinessExcetption;
import com.hao.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;


/**
 * @author 必燃
 * @version 1.0
 * @create 2022-11-28 20:18
 */
//拦截annotations所包括的注解
@ControllerAdvice(annotations = {RestController.class , Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

//    //未知异常
//    @ExceptionHandler(Exception.class)
//    public R<String> doException(Exception e)
//    {
//        System.out.println("异常");
//        return null;
//    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex)
    {
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry"))
        {
            String[] s = ex.getMessage().split(" ");
            String msg=s[2]+"已存在";
            return R.error(msg);
        }
        return R.error("失败");
    }



    @ExceptionHandler(SystemException.class)
    public R<String> doSystemException(SystemException e)
    {
        //日志
        //发送消息给运维
        //
        log.error(e.getMessage());
        return new R(e.getCode(),e.getMessage(),null);
    }
    @ExceptionHandler(BusinessExcetption.class)
    public R<String> doBusinessExcetption(BusinessExcetption e)
    {

        log.error(e.getMessage());
        return new R(e.getCode(),e.getMessage(),null);
    }

    //未知异常
//    @ExceptionHandler(Exception.class)
//    public R<String> doException(Exception e)
//    {
////        System.out.println("异常");
//        log.info(e.getMessage());
//        log.error("未知的异常: "+e.getMessage());
//        return new R(0,"系统繁忙，请稍后重试！",null);
//    }
}

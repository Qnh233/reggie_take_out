package com.hao.common;

/**
 * @author 必燃
 * @version 1.0
 * @create 2023-01-13 18:39
 *
 * ThreadLocal 封装工具类
 */
public class BaseContext {
    private  static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static void setCurrentId(Long id)
    {
        threadLocal.set(id);
    }
    public static Long getCurrentId()
    {
        return threadLocal.get();
    }
}

package com.hao.domain;

import lombok.Data;

/**
 * @author 必燃
 * @version 1.0
 * @create 2023-01-18 19:28
 */
public class CodeCheck {

    private static int codes;
    public static int getcodes()
    {
        return codes;
    }
    public static void setcodes(int c)
    {
        codes=c;
    }
    public static void removecode(){}{ codes=0;}

}

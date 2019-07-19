package com.eshel.core;

import java.util.Locale;

/**
 * Created by EshelGuo on 2019/6/19.
 */
public class Test {

    private static Object a = new Object();
    public static void main(String[] args){
        System.out.println(String.format(Locale.CHINA, "价值%d秀钻礼物，兑换成功后放入包裹", 66666666666666616L));
    }
}
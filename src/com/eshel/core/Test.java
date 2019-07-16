package com.eshel.core;

import com.eshel.ann.format.PublicParamsTableModel;

/**
 * Created by EshelGuo on 2019/6/19.
 */
public class Test {

    private static Object a = new Object();
    public static void main(String[] args){
        /*Object a1 = new Object();
        long ago = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            a1.hashCode();
        }
        long now = System.currentTimeMillis();
        System.out.println(now - ago);*/

        PublicParamsTableModel.Table table = new PublicParamsTableModel.Table("hahaha", "name", "desc", "String");
        System.out.println(table);
        try {
            Object clone = table.clone();
            System.out.println(clone);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.eshel.core.util;

/**
 * Created by EshelGuo on 2019/6/14.
 * 通用回调方法
 */
public interface Callback<Params,Return> {
    Return callback(Params params);
}

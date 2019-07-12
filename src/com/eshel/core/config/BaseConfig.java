package com.eshel.core.config;

import java.io.Serializable;

/**
 * Created by EshelGuo on 2019/6/20.
 */
public abstract class BaseConfig implements Serializable{

    public BaseConfig() {
    }

    public void save(){
        ConfigHelper.save(this);
    }
}

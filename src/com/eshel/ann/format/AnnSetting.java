package com.eshel.ann.format;

import com.eshel.core.config.BaseConfig;
import com.eshel.core.config.ConfigHelper;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by EshelGuo on 2019/6/22.
 */
public class AnnSetting extends BaseConfig{
    //是否是第一次启动
    public boolean isFirstOpen = true;

    // 是否使用常量
    public boolean useConst = true;

    // 是否使用单行注释, false 则使用文档注释
    public boolean useSignLineDoc = true;

    //类型映射
    public LinkedHashMap<String, String> typeMap;

    public List<PublicParamsTableModel.Table> publicParams;

    public static AnnSetting load(){
        return ConfigHelper.load(AnnSetting.class);
    }

    public static void save(AnnSetting setting){
        if(setting != null)
            setting.save();
    }

    public void isFirstOpen(float versionCode){

    }
}

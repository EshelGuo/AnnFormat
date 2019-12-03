/*
 * Copyright (c) 2019 Eshel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

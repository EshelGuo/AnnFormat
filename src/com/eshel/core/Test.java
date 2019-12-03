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
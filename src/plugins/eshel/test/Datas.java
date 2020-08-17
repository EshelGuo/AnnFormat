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

package plugins.eshel.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eshel on 2019/12/6.
 */
public class Datas {
    private static ChangeListener listener;
    public static List<String> datas = new ArrayList<>(10);

    public static void register(ChangeListener listener){
        Datas.listener = listener;
    }

    public static void addData(String data){
        datas.add(data);
        if(listener != null)
            listener.insert(data);
    }

    interface ChangeListener {
        void insert(String data);
    }
}

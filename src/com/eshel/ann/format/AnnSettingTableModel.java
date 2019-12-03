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

import com.eshel.core.view.table.BaseTableModel;
import com.eshel.core.view.table.TableData;
import com.eshel.core.view.table.anno.Name;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EshelGuo on 2019/6/26.
 */
public class AnnSettingTableModel extends BaseTableModel<AnnSettingTableModel.Table> {

    @Override
    protected void setValueAt(Table data, Object value, int index, Name annotation, int position) {
        super.setValueAt(data, value, index, annotation, position);
        switch (annotation.id()){
            case Table.ID_KEYWORD:
                if(position == mData.size() - 1 && !isEmptyData(data)) {
                    addEmptyDataToLast(5);
                }
                break;
            case Table.ID_TYPE:
                break;
        }
    }

    public void addData(LinkedHashMap<String, String> map) {
        List<Table> data = new ArrayList<>(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            data.add(new Table(entry.getKey(), entry.getValue()));
        }
        addData(data);
    }

    public static class Table extends TableData{

        private static final int ID_KEYWORD = 127;
        private static final int ID_TYPE = 410;

        @Name(value = "关键字", id = ID_KEYWORD)
        public String keyword;
        @Name(value = "对应类型", id = ID_TYPE)
        public String type;

        public Table() {}

        public Table(String keyword, String type) {
            this.keyword = keyword;
            this.type = type;
        }
    }
}

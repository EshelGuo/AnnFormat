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

package plugins.eshel.ann.format;


import plugins.eshel.core.view.table.BaseTableModel;
import plugins.eshel.core.view.table.TableData;
import plugins.eshel.core.view.table.anno.Name;


/**
 * Created by EshelGuo on 2019/7/15.
 */
public class PublicParamsTableModel extends BaseTableModel<PublicParamsTableModel.Table> {

    @Override
    protected void setValueAt(Table data, Object value, int index, Name annotation, int position) {
        switch (annotation.id()){
            case Table.ID_PARAMS:
                data.setParams((String) value);
                break;
            default:
                super.setValueAt(data, value, index, annotation, position);
                break;
        }
    }

    public static class Table extends TableData {
        private static final int ID_PARAMS = 273;
        @Name(value = "替换参数", id = ID_PARAMS)
        private String params;
        @Name("公参名")
        private String name;
        @Name("描述")
        private String desc;
        @Name("类型")
        private String type;

        public Table() {
        }

        public Table(String params, String name, String desc, String type) {
            this.params = params;
            this.name = name;
            this.desc = desc;
            this.type = type;
        }

        public void setParams(String params) {
            params = params.trim().replace("，", ",").replace(" ", "");
            if(params.endsWith(",")){
                params = params.substring(0, params.length() - 1);
            }
            this.params = params;
        }

        public String[] getParams() {
            return params.split(",");
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }

        public String getType() {
            return type;
        }

        @Override
        public Table clone(){
            try {
                return (Table) super.clone();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Table(params, name, desc, type);
        }

        @Override
        public String toString() {
            return "Table{" +
                    "params='" + params + '\'' +
                    ", name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    ", type='" + type + '\'' +
                    '}'+super.toString();
        }

        /*@Override
        public PublicParamsTableModel.Table clone(){
            try {
                Object clone = super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return new Table(params, name, desc, type);
        }*/
    }
}

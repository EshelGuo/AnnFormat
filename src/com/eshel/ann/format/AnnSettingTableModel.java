package com.eshel.ann.format;

import com.eshel.core.util.Util;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EshelGuo on 2019/6/26.
 */
public class AnnSettingTableModel extends AbstractTableModel {
    String[] TableHead = {"关键字","对应类型"};    //表格标题
    List<Table> mData = new ArrayList<>(10);
    @Override
    public int getRowCount() {
        return mData.size();
    }

    @Override
    public int getColumnCount() {
        return TableHead.length;
    }

    @Override
    public String getColumnName(int column) {
        return TableHead[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Table table = mData.get(rowIndex);
        switch (columnIndex){
            case 0:
                return table.keyword;
            case 1:
                return table.type;
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex < 0)
            return false;
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        Table data = mData.get(row);
        switch (column){
            case 0:
                data.keyword = toString(value);
                if(row == mData.size() - 1 && dataNoDefault()) {
                    addEmptyData(5);
                }
                break;
            case 1:
                data.type = toString(value);
                break;
        }
        fireTableCellUpdated(row, column);
    }

    public void addEmptyData(int number){
        for (int i = 0; i < number; i++) {
            mData.add(Table.createDefault());
        }
        this.fireTableDataChanged();
    }

    private boolean dataNoDefault(){
        if(Util.isEmpty(mData))
            return true;
        for (Table temp : mData) {
            if(temp.isDefault())
                return false;
        }
        return true;
    }

    private String toString(Object obj){
        if(obj == null)
            return null;
        return obj.toString();
    }

    public List<Table> getData(){
        if(mData != null){
            ArrayList<Table> temp = new ArrayList<>(mData);
            temp.removeIf(Table::isDefault);
            return temp;
        }
        return null;
    }

    public void addData(LinkedHashMap<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            mData.add(new Table(entry.getKey(), entry.getValue()));
        }
    }

    public static class Table{

        private static final int DEFAULT = -1;

        private int isDefault = 0;

        public Table() {
        }

        public Table(String keyword, String type) {
            this.keyword = keyword;
            this.type = type;
        }

        public boolean isDefault() {
            return isDefault == DEFAULT;
        }

        public String keyword;
        public String type;

        public static Table createDefault() {
            return new Table().setDefault();
        }

        private Table setDefault() {
            isDefault = DEFAULT;
            return this;
        }
    }
}

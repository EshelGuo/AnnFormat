package com.eshel.core.view.table;

import com.eshel.core.util.Cloneable;

/**
 * Created by EshelGuo on 2019/7/12.
 */
public abstract class TableData implements Cloneable{
    protected long tid;

    public TableData() {}

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

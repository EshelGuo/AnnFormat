package com.eshel.core.view.table;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.Vector;

/**
 * Created by EshelGuo on 2019/7/12.
 */
public class TableView extends JTable{


    public TableView() {
        init();
    }

    public TableView(TableModel dm) {
        super(dm);
        init();
    }

    public TableView(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
        init();
    }

    public TableView(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
        init();
    }

    public TableView(int numRows, int numColumns) {
        super(numRows, numColumns);
        init();
    }

    public TableView(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
        init();
    }

    public TableView(@NotNull Object[][] rowData, @NotNull Object[] columnNames) {
        super(rowData, columnNames);
        init();
    }

    private void init(){
        setRowHeight((int) (getRowHeight()*1.5f));
    }
}

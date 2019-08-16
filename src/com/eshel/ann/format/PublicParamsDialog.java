package com.eshel.ann.format;

import com.eshel.core.util.Log;
import com.eshel.core.util.Util;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class PublicParamsDialog extends JDialog {

    public static final String TAG = PublicParamsDialog.class.getSimpleName();

    private JPanel contentPane;
    private JButton ok;
    private JButton buttonCancel;
    private JTable table;
    private JButton btnLineUp;
    private JButton btnLineDown;
    private JButton btnInsert;
    private JTextField etInsertLineNumber;
    private JButton delete;
    private PublicParamsTableModel mAdapter;
    private AnnSetting setting;

    public PublicParamsDialog(AnnSetting setting) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(ok);

        ok.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> dispose());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.setting = setting;
        initTable();
        btnLineUp.addActionListener(e -> mAdapter.lineUp());
        btnLineDown.addActionListener(e -> mAdapter.lineDown());
        btnInsert.addActionListener(e -> {
            String text = etInsertLineNumber.getText();
            try{
                mAdapter.insertLine(Integer.valueOf(text));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
        delete.addActionListener(e -> {
            int[] rows = table.getSelectedRows();
            mAdapter.remove(rows);
        });

        Log.d(TAG, "public params dialog showing");
    }



    private void initTable() {
        table.setRowHeight((int) (table.getRowHeight()*1.5f));
        mAdapter = new PublicParamsTableModel();
        table.setModel(mAdapter);
        List<PublicParamsTableModel.Table> data = new ArrayList<>();
        if(Util.notEmpty(setting.publicParams)){
            for (PublicParamsTableModel.Table publicParam : setting.publicParams) {
                data.add(publicParam.clone());
            }
        }
        mAdapter.setNewData(data);
    }

    private void onOK() {
        setting.publicParams = mAdapter.getDatas();
        dispose();
    }
}

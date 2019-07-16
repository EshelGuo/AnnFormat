package com.eshel.ann.format;

import javax.swing.*;
import java.awt.event.*;
import java.util.LinkedHashMap;
import java.util.List;

public class AnnSettingDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JCheckBox cbUseConst;
    private JRadioButton rbDocComment;
    private JRadioButton rbLineComment;
    private JTable table;
    private JButton lineDown;
    private JButton btnInsert;
    private JTextField etInsertNumber;
    private JButton lineUp;
    private JButton publicParamsSetting;
    private JButton delete;
    private final AnnSetting setting;
    private AnnSettingTableModel adapter;

    public AnnSettingDialog() {
        setting = AnnSetting.load();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        lineDown.addActionListener(e -> adapter.lineDown());
        lineUp.addActionListener(e -> adapter.lineUp());
        btnInsert.addActionListener(e -> {
            try{
                String text = etInsertNumber.getText();
                Integer position = Integer.valueOf(text);
                adapter.insertLine(position);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });

        lineDown.setToolTipText("所有数据都下移一行");
        lineUp.setToolTipText("所有数据都上移一行, 删除遇到的第一个空行");
        btnInsert.setToolTipText("在第n行后插入1条数据, 如需在第一行前边插入请输入0");

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        ButtonGroup group = new ButtonGroup();
        group.add(rbDocComment);
        group.add(rbLineComment);
        if(setting.useSignLineDoc){
            rbLineComment.setSelected(true);
        }else {
            rbDocComment.setSelected(true);
        }

        rbLineComment.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED)
                setting.useSignLineDoc = true;
        });
        rbDocComment.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED)
                setting.useSignLineDoc = false;
        });
        cbUseConst.setSelected(setting.useConst);
        cbUseConst.addItemListener(e -> setting.useConst = e.getStateChange() == ItemEvent.SELECTED);
        initTable();
        publicParamsSetting.addActionListener(e -> showPublicParamsDialog());
        delete.addActionListener(e -> adapter.remove(table.getSelectedRows()));
    }

    private void initTable() {
        table.setRowHeight((int) (table.getRowHeight()*1.5f));
        adapter = new AnnSettingTableModel();
        table.setModel(adapter);
        adapter.addData(setting.typeMap);
        adapter.addEmptyDataToLast(5);
    }

    private void onOK() {
        // add your code here
        List<AnnSettingTableModel.Table> data = adapter.getDatas();
        setting.typeMap = toMap(data);
        setting.save();
        dispose();
    }

    private LinkedHashMap<String, String> toMap(List<AnnSettingTableModel.Table> data) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(data.size());
        for (AnnSettingTableModel.Table temp : data) {
            map.put(temp.keyword, temp.type);
        }
        return map;
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void showPublicParamsDialog() {
        PublicParamsDialog dialog = new PublicParamsDialog(setting);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
/*
    public static void main(String[] args) {
        AnnSettingDialog dialog = new AnnSettingDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }*/
}

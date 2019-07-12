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
    private final AnnSetting setting;
    private AnnSettingTableModel adapter;

    public AnnSettingDialog() {
        setting = AnnSetting.load();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

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
        cbUseConst.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setting.useConst = e.getStateChange() == ItemEvent.SELECTED;
            }
        });
        initTable();
    }

    private void initTable() {
        table.setRowHeight((int) (table.getRowHeight()*1.5f));
        adapter = new AnnSettingTableModel();
        table.setModel(adapter);
        adapter.addData(setting.typeMap);
        adapter.addEmptyData(5);
    }

    private void onOK() {
        // add your code here
        List<AnnSettingTableModel.Table> data = adapter.getData();
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
/*
    public static void main(String[] args) {
        AnnSettingDialog dialog = new AnnSettingDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }*/
}

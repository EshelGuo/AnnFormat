package com.eshel.ann.format;

import com.eshel.core.Dialog;
import com.eshel.core.util.Log;
import com.eshel.core.util.Util;
import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Consumer;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;

public class AnnSettingDialog extends JDialog {

    public static final String TAG = AnnSettingDialog.class.getSimpleName();

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
    private JButton btnImport;
    private JButton btnExport;
    private AnnSetting setting;
    private AnnSettingTableModel adapter;
    private AnActionEvent event;

    public static void showSetting(AnActionEvent event){
        AnnSettingDialog dialog = new AnnSettingDialog(event);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public AnnSettingDialog(AnActionEvent event) {
        this.event = event;
        setting = AnnSetting.load();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        lineDown.addActionListener(e -> adapter.lineDown());
        lineUp.addActionListener(e -> adapter.lineUp());
        btnExport.addActionListener(e -> exportSetting());
        btnImport.addActionListener(e -> importSetting());
        btnInsert.addActionListener(e -> {
            try {
                String text = etInsertNumber.getText();
                Integer position = Integer.valueOf(text);
                adapter.insertLine(position);
            } catch (Exception ex) {
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
        if (setting.useSignLineDoc) {
            rbLineComment.setSelected(true);
        } else {
            rbDocComment.setSelected(true);
        }

        rbLineComment.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED)
                setting.useSignLineDoc = true;
        });
        rbDocComment.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED)
                setting.useSignLineDoc = false;
        });
        cbUseConst.setSelected(setting.useConst);
        cbUseConst.addItemListener(e -> setting.useConst = e.getStateChange() == ItemEvent.SELECTED);
        initTable();
        publicParamsSetting.addActionListener(e -> showPublicParamsDialog());
        delete.addActionListener(e -> adapter.remove(table.getSelectedRows()));
        Log.d(TAG, "setting dialog showing");
    }

    private void importSetting() {
        File disktop = Util.getDisktop();
        File config = new File(disktop, "AnnFormatConfig.json");

        File defaultFile = disktop;
        if(config.exists() && config.isFile()){
            defaultFile = config;
        }
        VirtualFile defaultVFile = LocalFileSystem.getInstance().findFileByIoFile(defaultFile);
        FileChooserDescriptor fcd = new FileChooserDescriptor(true, false, false, false, false, false);
        FileChooser.chooseFile(fcd, event.getProject(), defaultVFile, new Consumer<VirtualFile>() {
            @Override
            public void consume(VirtualFile virtualFile) {
                importSettingImpl(virtualFile);

            }
        });
    }

    private void importSettingImpl(VirtualFile virtualFile) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(virtualFile.getInputStream()));
            String line;
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            Util.closeIO(br);
        }
        String json = sb.toString();
        AnnSetting setting = new Gson().fromJson(json, AnnSetting.class);
        setting.save();
        dispose();
        showSetting(event);
    }

    private void exportSetting() {
        String data = new Gson().toJson(setting, AnnSetting.class);
        File desktop = Util.getDisktop();
        File output = new File(desktop, "AnnFormatConfig.json");
        BufferedWriter br = null;
        try {
            br = new BufferedWriter(new FileWriter(output, false));
            br.write(data);
            br.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.closeIO(br);
        }
        Dialog.showSuccess("配置文件 AnnFormatConfig.json 已保存至桌面");
    }

    private void initTable() {
        table.setRowHeight((int) (table.getRowHeight() * 1.5f));
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
        dialog.setSize(800, 500);
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

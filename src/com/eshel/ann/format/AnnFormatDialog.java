package com.eshel.ann.format;

import com.eshel.core.util.Util;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;
import java.awt.event.*;

public class AnnFormatDialog extends JDialog {
    private JPanel contentPane;
    private JButton btnOK;
    private JButton btnCancel;
    private JButton btnSetting;
    private JTextArea etContent;
    private AnnFormatPresenter mPresenter;

    public AnnFormatDialog(AnActionEvent e) {
        mPresenter = new AnnFormatPresenter(e);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnOK);

        btnOK.addActionListener(action -> onOK());

        btnCancel.addActionListener(action -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e13 -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        btnSetting.addActionListener(action -> showSettingDialog());
    }

    private void showSettingDialog() {
        AnnSettingDialog dialog = new AnnSettingDialog();
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void onOK() {
        String content = etContent.getText();
        if(Util.notEmpty(content)) {
            mPresenter.insertCode(content);
            dispose();
        }
    }

    private void onCancel() {
        dispose();
    }
}

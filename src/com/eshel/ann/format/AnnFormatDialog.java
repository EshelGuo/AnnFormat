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

import com.eshel.core.util.Log;
import com.eshel.core.util.Util;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AnnFormatDialog extends JDialog {
    public static final String TAG = AnnFormatDialog.class.getSimpleName();
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
        btnSetting.addActionListener(action -> AnnSettingDialog.showSetting(e));
        Log.d(TAG, "format dialog showing");
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

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

package plugins.eshel.test;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;

public class ConsoleWindow implements Datas.ChangeListener {
    private JPanel root;
    private JTextPane textPane1;

    public ConsoleWindow(ToolWindow window) {
        //设置不可编辑
        textPane1.setEditable(false);
        textPane1.setDocument(new DefaultStyledDocument());
        Document document = textPane1.getDocument();
        for (String data : Datas.datas) {
            try {
                document.insertString(document.getLength(), data + "\n", new SimpleAttributeSet());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
        Datas.register(this);
    }

    public JPanel getRoot(){
        return root;
    }

    @Override
    public void insert(String data) {
        Document document = textPane1.getDocument();
        try {
            document.insertString(document.getLength(), data + "\n", new SimpleAttributeSet());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}

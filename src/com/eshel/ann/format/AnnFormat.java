package com.eshel.ann.format;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.LinkedHashMap;

/**
 * Created by EshelGuo on 2019/6/20.
 */
public class AnnFormat extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        AnnSetting setting = AnnSetting.load();
        if(setting.isFirstOpen){
            setting.typeMap = new LinkedHashMap<>();
            setting.typeMap.put("userId", "long");
            setting.typeMap.put("Id", "int");
            setting.typeMap.put("type", "int");
            setting.typeMap.put("Type", "int");
            setting.isFirstOpen = false;
            setting.save();
        }
        AnnFormatDialog dialog = new AnnFormatDialog(e);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}

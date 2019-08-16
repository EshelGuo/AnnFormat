package com.eshel.ann.format;

import com.eshel.core.util.Log;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by EshelGuo on 2019/6/20.
 */
public class AnnFormat extends AnAction {

    public static final String TAG = AnnFormat.class.getSimpleName();

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            actionPerformedInternal(e);
        }catch(Exception exception){
            exception.printStackTrace();
            Log.printStackTrace(exception);
        }
    }

    private void actionPerformedInternal(AnActionEvent e) {
        AnnSetting setting = AnnSetting.load();
        if(setting.isFirstOpen){
            setting.typeMap = new LinkedHashMap<>();
            setting.typeMap.put("userId", "long");
            setting.typeMap.put("Id", "int");
            setting.typeMap.put("type", "int");
            setting.typeMap.put("Type", "int");
            setting.isFirstOpen = false;
            setting.publicParams = new LinkedList<>();
            setting.publicParams.add(new PublicParamsTableModel.Table(
                    "roomId,roomType", "roomPublicParams", "直播间公参", "JSONObject"
            ));
            setting.save();
        }
        AnnFormatDialog dialog = new AnnFormatDialog(e);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        Log.d(TAG, "program start");
//        throw new NullPointerException("TEST .... test");
    }
}

package com.eshel.ann.format;

import com.eshel.core.util.Util;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by EshelGuo on 2019/6/21.
 */
public class AnnFormatPresenter {
    private AnActionEvent action;

    public AnnFormatPresenter(AnActionEvent e) {
        action = e;
    }

    public void insertCode(String noFormatCode) {
        Map<PT, List<EN>> formatedCode = formatCode(noFormatCode);
        if (Util.isEmpty(formatedCode)) {
            return;
        }
        System.out.println(formatedCode);

        new AnnWriteCommandAction(action, formatedCode).execute();
    }

    private Map<PT, List<EN>> formatCode(String codes){
        if(Util.isEmpty(codes))
            return new HashMap<>();

        String[] lines = codes.split("\\n");
        if(Util.isEmpty(lines)){
            return new HashMap<>();
        }

        Map<PT, List<EN>> result = new HashMap<>();

        for (String line : lines) {
            line = line.trim();
            line = line.replaceAll("\\t+", "\t");
            String[] temp = line.split("\\t");
            ArrayList<String> sourceData = new ArrayList<>(Arrays.asList(temp));
            EN en = EN.create(sourceData);
            if(en == null)
                continue;
            List<EN> ens = null;

            PT key = null;
            Set<PT> pts = result.keySet();
            for (PT pt : pts) {
                if(pt.value.equals(en.PT.value)){
                    key = pt;
                    ens = result.get(key);
                }
            }

            if(ens == null){
                ens = new ArrayList<>();
                key = en.PT;
                result.put(key, ens);
            }else {
                if(key.doc == null && en.PT.doc != null){
                    key.doc = en.PT.doc;
                }
            }
            ens.add(en);
        }

        /*for (Map.Entry<PT, List<EN>> entry : result.entrySet()) {
            PT pt = entry.getKey();
            List<EN> ens = entry.getValue();
            if(Util.notEmpty(ens)){
                for (EN en : ens) {
                    if(en != null && Util.notEmpty(en.PT.doc)){
                        pt.doc = en.PT.doc;
                        break;
                    }
                }
            }
        }*/
        return result;
    }


    public static class PT{
        public String value;
        public String doc;

        public PT() {
        }

        public PT(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            return value == obj || (value != null && value.equals(obj));
        }

        @Override
        public int hashCode() {
            if(value == null)
                return 0;
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "PT{" +
                    "value='" + value + '\'' +
                    ", doc='" + doc + '\'' +
                    '}';
        }
    }

    public static class EN {
        public PT PT;
        public String EN;
        public String doc;
        public List<Params> params;

        @Override
        public String toString() {
            return "EN{" +
                    "PT=" + PT +
                    ", EN='" + EN + '\'' +
                    ", doc='" + doc + '\'' +
                    ", params=" + params +
                    '}';
        }

        private static EN create(@NotNull List<String> sourceData){
            if(Util.isEmpty(sourceData))
                return null;
            EN en = new EN();

            int ptIndex = -1;
            int enIndex = -1;

            for (int i = 0; i < sourceData.size(); i++) {
                String data = sourceData.get(i).trim();

                Integer integer = Util.toInteger(data);
                if(integer != null){
                    if(integer % 1000 == 0){
                        if(ptIndex != -1)
                            continue;
                        ptIndex = i;
                        en.PT = new PT();
                        en.PT.value = integer.toString();
                    }else {
                        if(enIndex != -1)
                            continue;
                        enIndex = i;
                        en.EN = integer.toString();
                    }
                }
            }

            if(en.EN == null) {
                return null;
            }

            if(en.PT == null){
                en.PT = new PT();
            }

            if(en.PT.value == null){
                en.PT.value = String.valueOf(Util.toInteger(en.EN) / 1000 * 1000);
            }

            int ptenIndex = ptIndex != -1 ? ptIndex : enIndex;
            int enDocIndex = ptenIndex - 1;
            int ptDocIndex = enDocIndex - 1;
            if(enDocIndex >= 0){
                en.doc = sourceData.get(enDocIndex);
            }

            if(ptDocIndex >= 0){
                en.PT.doc = sourceData.get(ptDocIndex);
            }

            int paramsIndex = enIndex + 1;
            if(paramsIndex < sourceData.size()){
                String paramsSourceData = sourceData.get(paramsIndex);
                paramsSourceData = paramsSourceData.trim()//.replace("{", ",").replace("}", ",")
                    .replace("：", ":").replaceAll(":+", ":").replace("，", ",");
                en.params = Params.findParams(paramsSourceData);
                en.params = Params.parsePublicParams(en.params);
            }
            return en;
        }
    }

    public static class Params{
        private boolean isPublishParams;
        private String name;
        String doc;
        String type = "String";

        public Params() {
        }

        public Params(boolean isPublishParams, String name, String doc, String type) {
            this.isPublishParams = isPublishParams;
            this.name = name;
            this.doc = doc;
            this.type = type;
        }

        public boolean isPublishParams() {
            return isPublishParams;
        }

        @Override
        public String toString() {
            return "Params{" +
                    "name='" + name + '\'' +
                    ", doc='" + doc + '\'' +
                    '}';
        }

        public void setName(String name){
            if(name == null)
                return;
            name = name.trim();
            this.name = name;

            AnnSetting setting = AnnSetting.load();
            for (Map.Entry<String, String> entry : setting.typeMap.entrySet()) {
                if(name.contains(entry.getKey())){
                    type = entry.getValue();
                    break;
                }
            }
        }

        public String getName() {
            return name;
        }

        public void setDoc(String doc) {
            doc = doc.trim();
            while (true){
                if(doc.length() == 0)
                    return;

                char c = doc.charAt(doc.length() - 1);
                if(c == '}' || c==',' || c=='{'){
                    doc = doc.substring(0, doc.length() - 1);
                }else {
                    break;
                }
            }
            this.doc = doc;
        }

        private static List<Params> findParams(String sourceData){
            if(Util.isEmpty(sourceData) || sourceData.length() < 2)
                return null;
            List<Params> paramsList = new ArrayList<>();
            int index = 0;
            while (true){
                int nextIndex;
                index = sourceData.indexOf(":", index + 1);
                if(index != -1) {
                    Params params = new Params();
                    params.setName(findName(sourceData, index));
                    if(params.name == null)
                        continue;

                    nextIndex = sourceData.indexOf(":", index + 1);
                    if(nextIndex != -1){
                        int nextNameIndex = findNameIndex(sourceData, nextIndex);
                        if(nextNameIndex != -1 && nextNameIndex - 1 != index + 1){
                            params.setDoc(sourceData.substring(index + 1, nextNameIndex - 1));
                        }
                    }else {
                        params.setDoc(sourceData.substring(index + 1, sourceData.length()).replace("}", ""));
                    }
                    paramsList.add(params);
                }else {
                    break;
                }
            }
            return paramsList;
        }

        private static String findName(String sourceData, int lastIndex) {
            int index = findNameIndex(sourceData, lastIndex);
            if(index == -1 || index == lastIndex) {
                return null;
            }else {
                return sourceData.substring(index, lastIndex);
            }
        }

        private static int findNameIndex(String sourceData, int lastIndex){
            boolean findFirstChar = false;
            for (int i = lastIndex - 1; i >= 0; i--) {
                char c = sourceData.charAt(i);
                if(isLegalChar(c)){
                    //do nothing
                    findFirstChar = true;
                }else {
                    if(!findFirstChar){
                        continue;
                    }
                    return i+1;
                }
            }
            return 0;
        }

        public static List<Params> parsePublicParams(List<Params> normalParams) {
            if(Util.isEmpty(normalParams))
                return normalParams;
            //clone setting 中的公参
            List<PublicParamsTableModel.Table> publicParams = Util.cloneList(AnnSetting.load().publicParams);

            for (PublicParamsTableModel.Table publicParam : publicParams) {

                String[] normalParamNames = publicParam.getParams();
                if(Util.isEmpty(normalParamNames))
                    continue;
                List<Params> findedNormalParams = new ArrayList<>(normalParamNames.length);
                for (String normalParamName : normalParamNames) {
                    Params findedNormalParam = findByName(normalParams, normalParamName);
                    if(findedNormalParam == null)
                        break;
                    findedNormalParams.add(findedNormalParam);
                }
                if(findedNormalParams.size() == normalParamNames.length){
                    normalParams.removeAll(findedNormalParams);
                    normalParams.add(0, new Params(true, publicParam.getName(), publicParam.getDesc(), publicParam.getType()));
                }
            }
            return normalParams;
        }

        public static Params findByName(List<Params> params, String name){
            if(Util.isEmpty(params))
                return null;
            for (Params param : params) {
                if(param.name.equals(name) && !param.isPublishParams())
                    return param;
            }
            return null;
        }
    }

    private static boolean isLegalChar(char c){
        return (c >= '0' && c <= '9') || ((c >= 'a' && c <= 'z')) || (c >= 'A' && c <= 'Z') || c == '_' || c == '$';
    }

    public static void main(String[] args){
        new AnnFormatPresenter(null).insertCode("积分商城 - 积分不足弹窗 - 页面\t\t47009\t\t\t删除\n" +
                "积分商城 - 积分不足弹窗 - 去跟班 - 点击\t47000\t47010\t\t\t删除\n" +
                "积分商城半屏\t直播间-PK 面板-兑换道具-点击\t37000\t37166 \t\t\t{roomId：直播间id，roomType：直播间类型}\n" +
                "\t直播间-积分商城半屏-商品说明提示文案-我知道了-点击\t\t37171 \t\t\t{roomId：直播间id，roomType：直播间类型}mallId：商品 id\n" +
                "\t直播间-积分商城半屏-兑换成功提示框-展示\t\t37177 \t\t\troomId：直播间id，roomType：直播间类型, mallId：商品 id，typeCode：：1=实物商品，2=礼物，3=座驾，4=靓号，5=道具，6=勋章\n");
    }
}

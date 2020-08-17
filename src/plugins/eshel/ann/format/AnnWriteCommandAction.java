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

package plugins.eshel.ann.format;

import plugins.eshel.ann.format.AnnFormatPresenter.EN;
import plugins.eshel.ann.format.AnnFormatPresenter.PT;
import plugins.eshel.ann.format.AnnFormatPresenter.Params;
import plugins.eshel.core.util.Log;
import plugins.eshel.core.util.PsiUtils;
import plugins.eshel.core.util.Util;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EshelGuo on 2019/6/22.
 */
public class AnnWriteCommandAction extends WriteCommandAction.Simple {

    private static final String TAG = "AnnWriteCommandAction";
    private Map<PT, List<EN>> mDatas;
    private AnActionEvent event;
    AnnWriteCommandAction(AnActionEvent event, Map<PT, List<EN>> codes) {
        super(event.getProject(), PsiUtils.getCurrentFile(event));
        mDatas = codes;
        this.event = event;
    }

    private Map<PT, PsiClass> getTagetInterface(Runnable[] runnables) {
        // 获取光标所在类, 如果是接口则返回, 不是则查找其中的内部类
        Map<PT, PsiClass> map = new HashMap<>();
        PsiClass currentClass = PsiUtils.getCurrentClass(event);
        Log.d(TAG, currentClass.toString());

        PsiClass[] innerClasses;
        if(currentClass.isInterface()){
            innerClasses = new PsiClass[]{currentClass};
        }else {
            innerClasses = currentClass.getInnerClasses();
        }
        if(innerClasses != null){
            Log.w(TAG, String.valueOf(innerClasses.length));
            Log.w(TAG, Arrays.toString(innerClasses));
        }

        for (PsiClass innerClass : innerClasses) {
            Log.d(TAG, innerClass.toString());
            if(innerClass.isInterface()){
                Log.d(TAG, "innerClass is interface");
                PT pt = getPTFromAnno(innerClass, innerClass.getModifierList().getAnnotations());
                if(pt != null){
                    Log.d(TAG, "PT not null");
                    map.put(pt, innerClass);
                }else {
                    Log.d(TAG, "PT is null");
                }
            }
        }

        if(map.size() == 0 && !currentClass.isInterface()){
            PT pt = getMorePTFromDatas();
            PsiClass anInterface = PsiUtils.createInterface(currentClass, "IPT"+pt.value);
            anInterface.getModifierList().addAnnotation("PT(PT_" + pt.value + ")");
            runnables[0] = new Runnable() {
                @Override
                public void run() {
                    currentClass.add(anInterface);
                }
            };
            map.put(pt, anInterface);
        }
        return map;
    }

    // 获取元素最多的PT
    private PT getMorePTFromDatas(){
        PT result = null;
        int size = 0;
        for (Map.Entry<PT, List<EN>> entry : mDatas.entrySet()) {
            if(entry.getValue().size() > size){
                size = entry.getValue().size();
                result = entry.getKey();
            }
        }
        return result;
    }

    private PT getPTFromAnno(PsiClass currentClass, PsiAnnotation[] annotations) {
        if(Util.isEmpty(annotations)){
            Log.e(TAG, "注解为空");
            return null;
        }
        Log.e(TAG, Arrays.toString(annotations));

        for (PsiAnnotation annotation : annotations) {

            if("PT".equals(annotation.getQualifiedName()) || "com.wobo.census.ann.PT".equals(annotation.getQualifiedName())){
                Log.e(TAG, "anno name is PT");
                String ptName = annotation.findAttributeValue("value").getText();
                String pt = ptName.replace("\"", "").replace("PT_", "").replace("PT", "");
                Integer iPT = Util.toInteger(pt);
                if(iPT == null){
                    String PT = PsiUtils.getConstValue(currentClass, ptName);
                    if(PT != null)
                        pt = PT;
                }
                Log.w(TAG, "pt: "+pt);
                return new PT(pt);
            }else {
                Log.e(TAG, annotation.getQualifiedName());
            }
        }
        return null;
    }

    @Override
    protected void run() throws Throwable {
//        generateImport();
        try {
            generateConst();
            Log.i(TAG, "生成常量完成");
            Runnable[] runnables = new Runnable[1];
            Map<PT, PsiClass> tagetInterfaces = getTagetInterface(runnables);
            generateGet(tagetInterfaces);
            Log.i(TAG, "get方法生成成功");
            generateMethod(tagetInterfaces);
            Log.i(TAG, "EN 方法生成成功");
            if(runnables.length > 0 && runnables[0] != null)
                runnables[0].run();
            Log.i(TAG, "代码生成完成");
        }catch(Exception exception){
            Log.logE("AnnWriteCommandAction error: ", exception);
        }
    }

    private void generateImport() {
        PsiClass currentClass = PsiUtils.getCurrentClass(event);
        PsiElementFactory factory = PsiUtils.getElementFactory(currentClass);
        JavaPsiFacade jpf = JavaPsiFacade.getInstance(event.getProject());
        PsiClass EN = jpf.findClass("com.wobo.census.ann.EN", GlobalSearchScope.projectScope(event.getProject()));
        PsiClass PT = jpf.findClass("com.wobo.census.ann.PT", GlobalSearchScope.projectScope(event.getProject()));
        PsiClass Params = jpf.findClass("com.wobo.census.ann.Params", GlobalSearchScope.projectScope(event.getProject()));
        PsiClass AnnProxy = jpf.findClass("com.wobo.census.ann.AnnProxy", GlobalSearchScope.projectScope(event.getProject()));
        PsiClass RoomAnnMsg = jpf.findClass("com.wobo.room.ann.RoomAnnMsg", GlobalSearchScope.projectScope(event.getProject()));

        PsiElement firstChild = currentClass.getFirstChild();

        if(EN != null) currentClass.addBefore(factory.createImportStatement(EN), firstChild);
        if(PT != null) currentClass.addBefore(factory.createImportStatement(PT), firstChild);
        if(Params != null) currentClass.addBefore(factory.createImportStatement(Params), firstChild);
        if(AnnProxy != null) currentClass.addBefore(factory.createImportStatement(AnnProxy), firstChild);
        if(RoomAnnMsg != null) currentClass.addBefore(factory.createImportStatement(RoomAnnMsg), firstChild);

    }

    private void generateGet(Map<PT, PsiClass> tagetInterfaces) {
        // 如果光标所在类是接口, 则跳过这一步
        PsiClass currentClass = PsiUtils.getCurrentClass(event);
        if(currentClass.isInterface()){
            return;
        }
        //如果已有get方法, 则跳过这一步
        PsiMethod[] getsMethod = currentClass.findMethodsByName("get", false);
        if(!Util.isEmpty(getsMethod))
            return;
        // 生成 get 函数
        PT pt = getMorePTFromDatas();
        PsiClass interfaceClass = tagetInterfaces.get(pt);
        String methodName = "public static " + interfaceClass.getQualifiedName() + " get() {\n" +
                "return AnnProxy.create(" + interfaceClass.getQualifiedName() + ".class);\n" +
                "}\n";
        PsiUtils.createMethodFromTextAdd(currentClass, methodName);
    }

    private void generateMethod(Map<PT, PsiClass> interfaces) {
        // 生成对应的方法(EN) 注解 参数及注释
        PT morePt = getMorePTFromDatas();
        PsiClass defaultInterface = getValueFromPTMap(interfaces, morePt);
        if(defaultInterface == null){
            defaultInterface = interfaces.entrySet().iterator().next().getValue();
        }
        for (Map.Entry<PT, List<EN>> entry : mDatas.entrySet()) {
            PsiClass target =getValueFromPTMap(interfaces, entry.getKey());
            if(target == null)
                target = defaultInterface;
            for (EN en : entry.getValue()) {
                generateMethodInternal(en, target);
            }
        }
    }

    private <T> T getValueFromPTMap(Map<PT, T> map, PT key){
        for (Map.Entry<PT, T> entry : map.entrySet()) {
            if(entry.getKey().value.equals(key.value)){
                return entry.getValue();
            }
        }
        return null;
    }

    private void generateMethodInternal(EN en, PsiClass target) {
        StringBuilder sb = new StringBuilder();
        AnnSetting setting = AnnSetting.load();
        PT pt = getPTFromAnno(PsiUtils.getParentClass(target), target.getModifierList().getAnnotations());
        if (!setting.useSignLineDoc && Util.notEmpty(en.doc)) {
            //拼接文档注释
            sb.append("/**\n");
            sb.append(" * ").append(en.doc).append("\n");
            if(Util.notEmpty(en.params)){
                for (Params param : en.params) {
                    sb.append("* @param ").append(param.getName()).append(" ").append(param.doc == null ? "" : param.doc).append("\n");
                }
            }
            sb.append(" */\n");
        }
        String enS = setting.useConst ? ("EN_" + en.EN) : ("\"" + en.EN + "\"");
        String ptS = setting.useConst ? ("PT_" + en.PT.value) : ("\"" + en.PT.value + "\"");
        if (!en.PT.value.equals(pt.value)){
            sb.append("@PT(").append(ptS).append(")\n");
        }
        sb.append("@EN(").append(enS).append(")\n");
        sb.append("void EN_").append(en.EN).append("(");

        List<Params> params = en.params;
        if(params != null) {
            for (int i = 0; i < params.size(); i++) {
                Params param = params.get(i);

                if (i != 0) {
                    sb.append(", ");
                }

                if(param.isPublishParams()){
                    sb.append(param.type).append(" ").append(param.getName());
                }else {
                    sb.append("@Params(\"").append(param.getName()).append("\")").append(param.type).append(" ").append(param.getName());
                }
            }
        }
        sb.append(");");
        if(setting.useSignLineDoc && en.doc != null){
            sb.append("// ").append(en.doc);
        }
        sb.append("\n");
        PsiUtils.createMethodFromTextAdd(target, sb.toString());
    }

    private void generateConst() {
        // 生成PT EN 常量, 并生成相应注释
        // 不管是在接口中还是在类种, 都生成常量
        try {
            PsiClass targetClass;
            PsiClass currentClass = PsiUtils.getCurrentClass(event);
            if(currentClass.isInterface()){
                PsiElement parent = currentClass.getParent();
                if(parent instanceof PsiClass)
                    targetClass = (PsiClass) currentClass.getParent();
                else
                    targetClass = currentClass;
            }else {
                targetClass = currentClass;
            }

            for (Map.Entry<PT, List<EN>> entry : mDatas.entrySet()) {
                PsiUtils.createPrivateStringConstFailed(targetClass, "PT_"+entry.getKey().value, entry.getKey().value, entry.getKey().doc);
                for (EN en : entry.getValue()) {
                    PsiUtils.createPrivateStringConstFailed(targetClass, "EN_"+en.EN, en.EN, en.doc);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

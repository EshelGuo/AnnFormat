package com.eshel.core.view.table.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by EshelGuo on 2019/7/12.
 * 每一列的标题
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
    String value();
    int id() default 0;
}

package com.rocinante

import com.rocinante.extension.RocinanteExtension
import org.spockframework.runtime.extension.ExtensionAnnotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.FIELD, ElementType.TYPE])
@ExtensionAnnotation(RocinanteExtension)
public @interface Rocinante {
    String binding() default '';
    String config() default '';
    boolean isFile() default false;
}
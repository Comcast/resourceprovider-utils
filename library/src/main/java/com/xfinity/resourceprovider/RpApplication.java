package com.xfinity.resourceprovider;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Target(value = TYPE)
public @interface RpApplication {
    boolean generateIdProvider() default true;
    boolean generateIntegerProvider() default true;
    boolean generateDimensionProvider() default true;
    boolean generateColorProvider() default true;
    boolean generateDrawableProvider() default true;
    boolean generateStringProvider() default true;
}

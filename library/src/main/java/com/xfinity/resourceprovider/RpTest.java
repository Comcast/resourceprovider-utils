package com.xfinity.resourceprovider;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target(value = FIELD)
public @interface RpTest {
    boolean generateIdProvider() default true;
}

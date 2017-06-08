package com.nibado.example.springaop.aspects;

public @interface Restrict {
    boolean localOnly() default false;
    boolean admin() default true;
}

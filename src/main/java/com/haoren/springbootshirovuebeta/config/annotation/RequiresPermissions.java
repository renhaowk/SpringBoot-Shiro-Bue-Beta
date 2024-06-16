package com.haoren.springbootshirovuebeta.config.annotation;

import com.haoren.springbootshirovuebeta.util.constants.Logical;

public @interface RequiresPermissions {
    String[] value();
    Logical logical() default Logical.AND;
}

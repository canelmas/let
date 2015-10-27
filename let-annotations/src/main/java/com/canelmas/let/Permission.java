package com.canelmas.let;

/**
 * Created by can on 18/09/15.
 */
public @interface Permission {

    String value() default "";

    /**
     * whether or not the permission request result should be taken into account
     * when this permission isn't granted
     *
     * @return
     */
    boolean required() default true;

}

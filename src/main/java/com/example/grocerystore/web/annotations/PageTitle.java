package com.example.grocerystore.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.example.grocerystore.util.constants.AppConstants.EMPTY_STRING;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PageTitle {

    String value() default EMPTY_STRING;
}

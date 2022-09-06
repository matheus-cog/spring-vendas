package com.matheusguedes.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.matheusguedes.api.Response.LISTA_NAO_PODE_SER_VAZIA;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = com.matheusguedes.validation.constraint.NotEmptyList.class)
public @interface NotEmptyList {

    String message() default LISTA_NAO_PODE_SER_VAZIA;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
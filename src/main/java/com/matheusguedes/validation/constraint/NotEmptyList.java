package com.matheusguedes.validation.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NotEmptyList implements ConstraintValidator<com.matheusguedes.validation.NotEmptyList, List> {

    @Override
    public void initialize(com.matheusguedes.validation.NotEmptyList constraintAnnotation) {}

    @Override
    public boolean isValid(List list, ConstraintValidatorContext context) {
        return list != null && !list.isEmpty();
    }

}
package com.edutech.api.validation;

import com.edutech.api.validation.impl.AssignmentTypeValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AssignmentTypeValidation.class)
@Documented
public @interface AssignmentType {
    boolean allowNull() default false;
    String message() default "Assignment type invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
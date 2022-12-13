package com.edutech.api.validation;

import com.edutech.api.validation.impl.GenderValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GenderValidation.class)
@Documented
public @interface Gender{
    boolean allowNull() default true;
    String message() default "Gender kind invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package com.edutech.api.validation;

import com.edutech.api.validation.impl.ProvinceKindValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProvinceKindValidation.class)
@Documented
public @interface ProvinceKind {
    boolean allowNull() default false;
    String message() default "Province kind invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

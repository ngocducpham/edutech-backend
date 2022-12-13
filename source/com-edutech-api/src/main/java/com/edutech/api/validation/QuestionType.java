package com.edutech.api.validation;

import com.edutech.api.validation.impl.QuestionTypeValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = QuestionTypeValidation.class)
@Documented
public @interface QuestionType {
    boolean allowNull() default false;
    String message() default "Question type invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

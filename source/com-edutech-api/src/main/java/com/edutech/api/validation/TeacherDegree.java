package com.edutech.api.validation;

import com.edutech.api.validation.impl.TeacherDegreeValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TeacherDegreeValidation.class)
@Documented
public @interface TeacherDegree {
    String message() default "Teacher degree kind invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

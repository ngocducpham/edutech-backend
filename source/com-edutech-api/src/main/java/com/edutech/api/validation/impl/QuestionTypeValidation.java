package com.edutech.api.validation.impl;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.validation.QuestionType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class QuestionTypeValidation implements ConstraintValidator<QuestionType, Integer> {
    boolean allowNull;
    @Override
    public void initialize(QuestionType constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer type, ConstraintValidatorContext constraintValidatorContext) {
        if(allowNull && type == null){
            return true;
        }
        return Objects.equals(type, EduTechConstant.QUESTION_TYPE_SINGLE_CHOICE)
                || Objects.equals(type, EduTechConstant.QUESTION_TYPE_MULTI_CHOICE)
                || Objects.equals(type, EduTechConstant.QUESTION_TYPE_ESSAY);
    }
}

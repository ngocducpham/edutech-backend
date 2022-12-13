package com.edutech.api.validation.impl;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.validation.AssignmentType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class AssignmentTypeValidation implements ConstraintValidator<AssignmentType, Integer> {
    boolean allowNull;

    @Override
    public void initialize(AssignmentType constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer type, ConstraintValidatorContext constraintValidatorContext) {
        if(allowNull && type == null){
            return true;
        }
        return Objects.equals(type, EduTechConstant.ASSIGNMENT_TYPE_ESSAY)
                || Objects.equals(type, EduTechConstant.ASSIGNMENT_TYPE_UPLOAD_FILE)
                || Objects.equals(type, EduTechConstant.ASSIGNMENT_TYPE_CHOICE);
    }
}

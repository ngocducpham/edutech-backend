package com.edutech.api.validation.impl;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.validation.Gender;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class GenderValidation implements ConstraintValidator<Gender, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(Gender constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer gender, ConstraintValidatorContext constraintValidatorContext) {
        if(gender == null && allowNull){
            return true;
        }
        if(!Objects.equals(gender, EduTechConstant.GENDER_FEMALE)
                && !Objects.equals(gender, EduTechConstant.GENDER_MALE)
                && !Objects.equals(gender, EduTechConstant.GENDER_OTHER)){
            return false;
        }
        return true;
    }
}

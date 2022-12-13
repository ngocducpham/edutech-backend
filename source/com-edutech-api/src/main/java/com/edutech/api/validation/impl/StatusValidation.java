package com.edutech.api.validation.impl;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.validation.Status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class StatusValidation  implements ConstraintValidator<Status, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(Status constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }
    @Override
    public boolean isValid(Integer status, ConstraintValidatorContext constraintValidatorContext) {
        if(status == null && allowNull){
            return true;
        }
        if(!Objects.equals(status, EduTechConstant.STATUS_ACTIVE)
                && !Objects.equals(status, EduTechConstant.STATUS_LOCK)
                && !Objects.equals(status, EduTechConstant.STATUS_DELETE)
                && !Objects.equals(status, EduTechConstant.STATUS_PENDING)){
            return false;
        }
        return true;
    }
}

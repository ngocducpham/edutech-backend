package com.edutech.api.validation.impl;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.validation.ProvinceKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class ProvinceKindValidation implements ConstraintValidator<ProvinceKind, String> {
    boolean allowNull;

    @Override
    public void initialize(ProvinceKind constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String provinceKind, ConstraintValidatorContext constraintValidatorContext) {
        if(allowNull && Objects.isNull(provinceKind)) {
            return true;
        }

        switch (provinceKind) {
            case EduTechConstant.PROVINCE_KIND_PROVINCE:
            case EduTechConstant.PROVINCE_KIND_DISTRICT:
            case EduTechConstant.PROVINCE_KIND_COMMUNE:
                return true;
            default:
                return false;
        }
    }
}

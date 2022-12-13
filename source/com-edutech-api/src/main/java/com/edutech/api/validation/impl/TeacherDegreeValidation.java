package com.edutech.api.validation.impl;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.validation.TeacherDegree;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TeacherDegreeValidation implements ConstraintValidator<TeacherDegree, String> {

    @Override
    public boolean isValid(String degree, ConstraintValidatorContext constraintValidatorContext) {

        switch (degree) {
            case EduTechConstant.TEACHER_DEGREE_PROFESSOR:
            case EduTechConstant.TEACHER_DEGREE_MASTER:
            case EduTechConstant.TEACHER_DEGREE_DOCTOR:
                return true;
            default:
                return false;
        }
    }
}

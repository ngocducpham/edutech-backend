package com.edutech.api.constant;


import com.edutech.api.utils.ConfigurationService;

public class EduTechConstant {
    public static final String ROOT_DIRECTORY =  ConfigurationService.getInstance().getString("file.upload-dir","/tmp/upload");

    public static final Integer USER_KIND_ADMIN = 1;
    public static final Integer USER_KIND_TEACHER = 2;
    public static final Integer USER_KIND_STUDENT = 3;
    public static final Integer USER_KIND_CUSTOMER = 6;
    public static final Integer USER_KIND_EMPLOYEE = 7;
    public static final Integer USER_KIND_COLLABORATOR = 8;
    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;
    public static final Integer STATUS_LOCK = -1;
    public static final Integer STATUS_DELETE = -2;

    public static final Integer GROUP_KIND_SUPER_ADMIN = 1;
    public static final Integer GROUP_KIND_TEACHER = 2;
    public static final Integer GROUP_KIND_STUDENT = 3;
    public static final Integer GROUP_KIND_EMPLOYEE = 4;
    public static final Integer GROUP_KIND_COLLABORATOR = 4;

    public static final Integer MAX_ATTEMPT_FORGET_PWD = 5;
    public static final Integer MAX_TIME_FORGET_PWD = 5 * 60 * 1000; //5 minutes
    public static final Integer MAX_ATTEMPT_LOGIN = 5;
    public static final Integer MAX_TIME_VERIFY_ACCOUNT = 5 * 60 * 1000; //5 minutes

    public static final Integer CATEGORY_KIND = 1;


    public static final Integer GENDER_MALE = 1;
    public static final Integer GENDER_FEMALE = 2;
    public static final Integer GENDER_OTHER = 3;

    public static final String PROVINCE_KIND_PROVINCE = "PROVINCE_KIND_PROVINCE";
    public static final String PROVINCE_KIND_DISTRICT = "PROVINCE_KIND_DISTRICT";
    public static final String PROVINCE_KIND_COMMUNE = "PROVINCE_KIND_COMMUNE";

    public static final String TEACHER_DEGREE_MASTER = "MASTER";
    public static final String TEACHER_DEGREE_DOCTOR = "DOCTOR";
    public static final String TEACHER_DEGREE_PROFESSOR = "PROFESSOR";

    public static final Integer ASSIGNMENT_TYPE_CHOICE = 1;
    public static final Integer ASSIGNMENT_TYPE_ESSAY = 2;
    public static final Integer ASSIGNMENT_TYPE_UPLOAD_FILE = 3;

    public static final Integer ASSIGNMENT_CLASS_SHOW = 1;

    public static final Integer ASSIGNMENT_CLASS_HIDDEN = 2;

    public static final Integer QUESTION_TYPE_SINGLE_CHOICE = 1;
    public static final Integer QUESTION_TYPE_MULTI_CHOICE = 2;
    public static final Integer QUESTION_TYPE_ESSAY = 3;

    private EduTechConstant(){
        throw new IllegalStateException("Utility class");
    }

}

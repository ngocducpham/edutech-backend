package com.edutech.api.form.student;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateStudentProfileForm {
    @NotBlank
    private String password;
    @NotBlank
    private String currentPassword;
}

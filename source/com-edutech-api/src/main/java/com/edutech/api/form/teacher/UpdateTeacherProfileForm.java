package com.edutech.api.form.teacher;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateTeacherProfileForm {
    @NotBlank
    private String password;
    @NotBlank
    private String currentPassword;
}

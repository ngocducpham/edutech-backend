package com.edutech.api.form.teacher;

import com.edutech.api.validation.TeacherDegree;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@Getter
@Schema
public class CreateTeacherForm {
    @TeacherDegree
    @Schema(required = true)
    private String degree;

    @NotBlank
    @Schema(required = true)
    private String address;

    @NotBlank
    @Schema(required = true)
    private String username;

    @NotBlank
    @Schema(required = true)
    private String password;

    @NotBlank
    @Schema(required = true)
    private String fullName;

    @NotBlank
    @Schema(required = true)
    private String email;

    @NotBlank
    @Schema(required = true)
    private String phone;

    private String avatarPath;

    private Date birthDay;

    @NotNull
    @Schema(required = true)
    private Long provinceId;

    private Long districtId;

    private Long communeId;
}

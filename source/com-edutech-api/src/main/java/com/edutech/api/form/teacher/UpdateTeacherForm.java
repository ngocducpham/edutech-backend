package com.edutech.api.form.teacher;

import com.edutech.api.validation.Status;
import com.edutech.api.validation.TeacherDegree;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UpdateTeacherForm {
    @NotNull
    @Schema(required = true)
    private Long id;

    @TeacherDegree
    @Schema(required = true)
    private String degree;

    @NotBlank
    @Schema(required = true)
    private String address;

    private String email;
    private String phone;
    private Date birthDay;
    private String avatarPath;
    private String password;
    private String fullName;

    @Status
    private Integer status;

    @NotNull
    @Schema(required = true)
    private Long provinceId;

    private Long districtId;

    private Long communeId;
}

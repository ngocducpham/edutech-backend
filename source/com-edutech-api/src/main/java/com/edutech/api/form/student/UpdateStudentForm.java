package com.edutech.api.form.student;

import com.edutech.api.validation.Status;
import com.edutech.api.validation.TeacherDegree;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
@Data
public class UpdateStudentForm {
    @NotNull
    @Schema(required = true)
    private Long id;

    @NotNull
    @Schema(required = true)
    private Long majorId;

    @NotBlank
    @Schema(required = true)
    private String admissionYear;

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

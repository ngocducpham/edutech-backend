package com.edutech.api.form.account;

import com.edutech.api.validation.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Schema
public class CreateAccountAdminForm {
    @NotEmpty(message = "username cant not be null")
    @Schema(name = "username", required = true)
    private String username;
    @Schema(name = "email")
    @Email
    private String email;
    @NotEmpty(message = "password cant not be null")
    @Schema(name = "password", required = true)
    private String password;
    @NotEmpty(message = "fullName cant not be null")
    @Schema(name = "fullName",required = true)
    private String fullName;
    private String avatarPath;
    private Date birthDay;
    @Status
    private Integer status = 1;
    @NotEmpty(message = "phone can not be empty")
    @Schema(name = "phone")
    private String phone;

    @NotNull(message = "kind cannot be null")
    @Schema(name = "kind", required = true)
    private Integer kind;
}

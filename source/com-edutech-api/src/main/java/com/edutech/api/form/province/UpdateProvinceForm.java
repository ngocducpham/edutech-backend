package com.edutech.api.form.province;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateProvinceForm {
    @NotNull(message = "id cannot be null")
    @Schema(required = true)
    private Long provinceId;

    @NotEmpty(message = "provinceName cannot be null")
    @Schema(required = true)
    private String provinceName;

}

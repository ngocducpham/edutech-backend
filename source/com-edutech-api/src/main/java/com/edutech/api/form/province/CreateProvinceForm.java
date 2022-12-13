package com.edutech.api.form.province;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.validation.ProvinceKind;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateProvinceForm {
    @NotEmpty(message = "provinceName cannot be null")
    @Schema(required = true)
    private String provinceName;
    private Long parentProvinceId;

    @Schema(hidden = true)
    @JsonIgnore
    private String kind = EduTechConstant.PROVINCE_KIND_PROVINCE;
}

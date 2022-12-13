package com.edutech.api.dto.province;

import com.edutech.api.dto.ABasicAdminDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProvinceDto extends ABasicAdminDto {
    @JsonProperty("id")
    private Long provinceId;
    @JsonProperty("name")
    private String provinceName;
    @JsonProperty("kind")
    private String provinceKind;
    @JsonProperty("parentId")
    private Long parentProvinceId;
}

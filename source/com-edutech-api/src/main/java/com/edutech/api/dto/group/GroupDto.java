package com.edutech.api.dto.group;

import com.edutech.api.dto.permission.PermissionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class GroupDto {
    @Schema(name = "id")
    private Long id;
    @Schema(name = "name")
    private String name;
    @Schema(name = "description")
    private String description;
    @Schema(name = "kind")
    private int kind;
    @Schema(name = "permissions")
    private List<PermissionDto> permissions ;
}

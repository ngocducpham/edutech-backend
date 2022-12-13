package com.edutech.api.dto.major;

import com.edutech.api.dto.ABasicAdminDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MajorDto extends ABasicAdminDto{
    private String name;
    private String description;
    private String image;
    private Integer status;
}

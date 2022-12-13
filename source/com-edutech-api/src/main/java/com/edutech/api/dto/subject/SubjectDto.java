package com.edutech.api.dto.subject;

import com.edutech.api.dto.ABasicAdminDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectDto extends ABasicAdminDto{
    private String name;
    private String description;
    private String code;
    private Integer status;
}

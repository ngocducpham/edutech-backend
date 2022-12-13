package com.edutech.api.dto;

import com.edutech.api.dto.group.GroupDto;
import com.edutech.api.dto.province.ProvinceDto;
import com.edutech.api.storage.model.Group;
import lombok.Data;

import java.util.Date;

@Data
public class ABasicUserDto extends ABasicAdminDto{
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private Date birthDay;
    private String avatarPath;
    private String address;
    private ProvinceDto province;
    private ProvinceDto district;
    private ProvinceDto commune;
    private Integer kind;
    private GroupDto group;
}

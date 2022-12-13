package com.edutech.api.dto.classnews;

import com.edutech.api.dto.classdto.ClassDto;
import com.edutech.api.dto.teacher.TeacherDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ClassNewsDto {
    private Long id;
    private String content;
    private Long classId;
    private Integer status;
    private Boolean pintop;
    private Date created;
    private String imageURL;
    private String teacherName;
    private String teacherAvatar;
}

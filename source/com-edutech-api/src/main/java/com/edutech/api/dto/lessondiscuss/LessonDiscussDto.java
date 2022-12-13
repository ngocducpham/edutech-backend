package com.edutech.api.dto.lessondiscuss;

import lombok.Data;

import java.util.Date;

@Data
public class LessonDiscussDto {
    private Long id;
    private String title;
    private String content;
    private Long classId;
    private Long userId;
    private Date created_date;
    private Long lessonId;
    private String lessonName;
    private String userName;
    private String userAvatar;
}

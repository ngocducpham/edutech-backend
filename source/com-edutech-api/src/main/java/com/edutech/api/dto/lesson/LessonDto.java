package com.edutech.api.dto.lesson;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonDto {
    private Long id;
    private Long chapterId;
    private String chapterName;
    private String title;
    private String icon;
    private Long order;
    private String attachment;
    private String content;
    private Boolean hasAssignment;
}

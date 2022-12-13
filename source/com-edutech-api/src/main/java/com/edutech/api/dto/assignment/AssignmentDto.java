package com.edutech.api.dto.assignment;

import com.edutech.api.dto.lesson.LessonDto;
import com.edutech.api.storage.model.Lesson;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AssignmentDto {
    private Long id;
    private String title;
    private String description;
    private Integer type;
    private Date createdDate;
    private LessonDto lesson;
}

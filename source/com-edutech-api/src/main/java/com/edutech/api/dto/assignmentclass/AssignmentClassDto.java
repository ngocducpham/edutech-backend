package com.edutech.api.dto.assignmentclass;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AssignmentClassDto {
    private Long id;
    private Date start;
    private Date end;
    private int duration;
    private int maxAttempt;
    private int status;
    private Long classId;
    private String className;
    private Long assignmentId;
    private String assignmentTitle;
    private String assignmentDescription;
    private Integer type;
    private String title;
    private Long lessonId;
    private String description;
}

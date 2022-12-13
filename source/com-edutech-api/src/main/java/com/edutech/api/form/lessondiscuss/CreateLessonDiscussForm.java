package com.edutech.api.form.lessondiscuss;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CreateLessonDiscussForm {
    @Schema(required = true)
    private String title;
    private String content;

    private Long classId;

    private Long userId;

    private Long lessonId;
    private Date created_date;
}

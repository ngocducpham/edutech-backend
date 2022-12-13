package com.edutech.api.form.lessondiscuss;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UpdateLessonDiscussForm {
    @NotNull(message = "id cannot be null")
    @Schema(required = true)
    private Long id;
    private String title;
    private String content;

    @NotNull(message = "class id can not be null")
    private Long classId;

    @NotNull(message = "user id can not be null")
    private Long userId;

    @NotNull(message = "lesson id can not be null")
    private Long lessonId;

    private Date created_date;
    private Integer status;
}

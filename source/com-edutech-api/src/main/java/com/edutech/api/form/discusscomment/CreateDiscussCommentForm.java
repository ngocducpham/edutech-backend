package com.edutech.api.form.discusscomment;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CreateDiscussCommentForm {
    @NotNull(message = "discuss id can not be null")
    private Long discussId;
    private Long userId;
    private Long parentId;
    private Date created_date;
    private String content;
}

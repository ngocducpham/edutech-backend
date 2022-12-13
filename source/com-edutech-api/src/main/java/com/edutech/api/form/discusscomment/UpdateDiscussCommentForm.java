package com.edutech.api.form.discusscomment;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UpdateDiscussCommentForm {
    private Long id;
    private String content;
}

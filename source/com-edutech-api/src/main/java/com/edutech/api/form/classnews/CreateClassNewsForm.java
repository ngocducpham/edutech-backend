package com.edutech.api.form.classnews;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateClassNewsForm {
    @NotBlank
    private String content;
    @NotNull
    private Long classId;
    private String imageURL;
}

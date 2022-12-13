package com.edutech.api.form.classnews;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateClassNewsForm {
    private String content;

    private Boolean pintop;

    private String imageURL;

    private Integer status;
}

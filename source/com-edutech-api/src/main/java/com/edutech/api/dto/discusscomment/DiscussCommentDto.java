package com.edutech.api.dto.discusscomment;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DiscussCommentDto {
    private Long id;
    private Long discussId;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Long parentId;
    private Date created_date;
    private String content;
    private List<DiscussCommentDto> childs;
}

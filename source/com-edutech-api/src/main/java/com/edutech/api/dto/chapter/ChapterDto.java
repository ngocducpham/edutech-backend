package com.edutech.api.dto.chapter;

import com.edutech.api.dto.lesson.LessonDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChapterDto {
    private Long id;
    private Long syllabusId;
    private String title;
    private String description;
    private List<LessonDto> lessons;
}

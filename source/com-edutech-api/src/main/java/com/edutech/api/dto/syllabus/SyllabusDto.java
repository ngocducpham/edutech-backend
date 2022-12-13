package com.edutech.api.dto.syllabus;

import com.edutech.api.dto.ABasicAdminDto;
import com.edutech.api.dto.chapter.ChapterDto;
import com.edutech.api.dto.subject.SubjectDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SyllabusDto extends ABasicAdminDto {
    private String title;
    private String description;
    private String avatar;
    private SubjectDto subject;
    private List<ChapterDto> chapters;
}

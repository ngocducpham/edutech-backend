package com.edutech.api.mapper;

import com.edutech.api.dto.exam.ExamDto;
import com.edutech.api.form.exam.CreateExamForm;
import com.edutech.api.form.exam.UpdateExamForm;
import com.edutech.api.storage.model.Exam;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {QuestionMapper.class})
public interface ExamMapper {
    @Named("examFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "attempt_time", source = "attempt_time")
    @Mapping(target = "submit_time", source = "submit_time")
    @Mapping(target = "total_point", source = "total_point")
    @Mapping(target = "attachment", source = "attachment")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "assignmentClassId", source = "assignmentClass.id")
    @Mapping(target = "questionList", source = "assignmentClass.assignment.questions")
    @Mapping(target = "answerList", source = "answerList")
    ExamDto fromEntityToDto(Exam exam);

    @IterableMapping(qualifiedByName = "examFromEntityToDto")
    List<ExamDto> fromEntitiesToDtos(List<Exam> exams);

    @Named("examFromEntityToDtoWithQA")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "attempt_time", source = "attempt_time")
    @Mapping(target = "submit_time", source = "submit_time")
    @Mapping(target = "total_point", source = "total_point")
    @Mapping(target = "attachment", source = "attachment")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "assignmentClassId", source = "assignmentClass.id")
    @Mapping(target = "questionList", source = "assignmentClass.assignment.questions")
    @Mapping(target = "answerList", source = "answerList")
    ExamDto fromEntityToDtoWithQA(Exam exam);

    @IterableMapping(qualifiedByName = "examFromEntityToDtoWithQA")
    List<ExamDto> fromEntitiesToDtosWithQA(List<Exam> exams);

    @Named("examFromEntityToDtoAutoComplete")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamDto fromEntityToDtoAutoComplete(Exam exam);

    @IterableMapping(qualifiedByName = "examFromEntityToDtoAutoComplete")
    List<ExamDto> fromEntitiesToDtosAutoComplete(List<Exam> exams);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "attempt_time", source = "attempt_time")
    @Mapping(target = "attachment", source = "attachment")
    @Mapping(target = "student.id", source = "studentId")
    @Mapping(target = "assignmentClass.id", source = "assignmentClassId")
    Exam fromCreateFormToEntity(CreateExamForm form);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "submit_time", source = "submit_time")
    @Mapping(target = "attachment", source = "attachment")
    void fromUpdateFormToEntity(UpdateExamForm form, @MappingTarget Exam exam);
}

package com.edutech.api.mapper;

import com.edutech.api.dto.examanswer.ExamAnswerDto;
import com.edutech.api.form.examanswer.CreateExamAnswerForm;
import com.edutech.api.form.examanswer.UpdateExamAnswerForm;
import com.edutech.api.storage.model.ExamAnswer;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExamAnswerMapper {
    @Named("examanswerFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "question_id", source = "question.id")
    @Mapping(target = "answer", source = "answer")
    @Mapping(target = "point", source = "point")
    @Mapping(target = "answer_date", source = "answer_date")
    @Mapping(target = "exam_id", source = "exam.id")
    ExamAnswerDto fromEntityToDto(ExamAnswer examanswer);

    @IterableMapping(qualifiedByName = "examanswerFromEntityToDto")
    List<ExamAnswerDto> fromEntitiesToDtos(List<ExamAnswer> examanswers);

    @Named("examanswerFromEntityToDtoAutoComplete")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamAnswerDto fromEntityToDtoAutoComplete(ExamAnswer examanswer);

    @IterableMapping(qualifiedByName = "examanswerFromEntityToDtoAutoComplete")
    List<ExamAnswerDto> fromEntitiesToDtosAutoComplete(List<ExamAnswer> examanswers);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "question.id", source = "question_id")
    @Mapping(target = "answer", source = "answer")
    @Mapping(target = "answer_date", source = "answer_date")
    @Mapping(target = "exam.id", source = "exam_id")
    ExamAnswer fromCreateFormToEntity(CreateExamAnswerForm form);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "question.id", source = "question_id")
    @Mapping(target = "answer", source = "answer")
    @Mapping(target = "point", source = "point")
    @Mapping(target = "answer_date", source = "answer_date")
    @Mapping(target = "exam.id", source = "exam_id")
    void fromUpdateFormToEntity(UpdateExamAnswerForm form, @MappingTarget ExamAnswer examanswer);
}

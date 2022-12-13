package com.edutech.api.mapper;

import com.edutech.api.dto.question.QuestionDto;
import com.edutech.api.form.question.CreateQuestionForm;
import com.edutech.api.form.question.UpdateQuestionForm;
import com.edutech.api.storage.model.Question;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AssignmentMapper.class})
public interface QuestionMapper {

    @Named("questionFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "answer", source = "answer")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "point", source = "point")
    @Mapping(target = "assignment", source = "assignment", qualifiedByName = "assignmentFromEntityToDtoAutoComplete")
    QuestionDto fromEntityToDto(Question question);

    @IterableMapping(qualifiedByName = "questionFromEntityToDto")
    List<QuestionDto> fromEntitiesToDtos(List<Question> questions);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "content", source = "content")
    @Mapping(target = "answer", source = "answer")
    @Mapping(target = "point", source = "point")
    @Mapping(target = "assignment.id", source = "assignmentId")
    @Mapping(target = "type", source = "type")
    Question fromCreateFormToEntity(CreateQuestionForm form);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "content", source = "content")
    @Mapping(target = "answer", source = "answer")
    @Mapping(target = "point", source = "point")
    @Mapping(target = "type", source = "type")
    void fromUpdateFormToEntity(UpdateQuestionForm form, @MappingTarget Question question);

    @Named("questionFromUpdateFormsToEntity")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "form.id")
    @Mapping(target = "content", source = "form.content")
    @Mapping(target = "answer", source = "form.answer")
    @Mapping(target = "point", source = "form.point")
    @Mapping(target = "type", source = "form.type")
    @Mapping(target = "assignment.id", source = "assignmentId")
    Question fromUpdateFormsToEntity(UpdateQuestionForm form, Long assignmentId);

    default List<Question> fromUpdateFormsToEntities(List<UpdateQuestionForm> forms, Long assignmentId) {
        if (forms == null) {
            return null;
        }

        List<Question> list = new ArrayList<Question>(forms.size());
        for (UpdateQuestionForm updateQuestionForm : forms) {
            list.add(fromUpdateFormsToEntity(updateQuestionForm, assignmentId));
        }

        return list;

    }
}

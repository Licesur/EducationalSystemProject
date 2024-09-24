package ru.sova.educationapp.EducationalSystemApp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.sova.educationapp.EducationalSystemApp.DTO.TutorDTO;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;

/**
 * Маппер, предназначенный для перевода объекта сущности преподавателя к DTO и обратно
 */
@Mapper(uses = StudentMapper.class, componentModel = "spring")
public interface TutorMapper {
    TutorMapper INSTANCE = Mappers.getMapper(TutorMapper.class);

    @Mapping(source = "id", target = "id")
    TutorDTO toTutorDTO(Tutor tutor);

    @Mapping(source = "id", target = "id")
    Tutor toTutor(TutorDTO tutorDTO);
}

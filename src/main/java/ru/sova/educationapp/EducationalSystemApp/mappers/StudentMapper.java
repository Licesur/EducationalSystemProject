package ru.sova.educationapp.EducationalSystemApp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.models.Student;

/**
 * Маппер, предназначенный для перевода объекта сущности студента к DTO и обратно
 */
@Mapper(uses = {VerificationWorkMapper.class}, componentModel = "spring")
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    StudentDTO toStudentDTO(Student student);

    Student toStudent(StudentDTO studentDTO);
}

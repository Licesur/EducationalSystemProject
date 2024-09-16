package ru.sova.educationapp.EducationalSystemApp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.sova.educationapp.EducationalSystemApp.DTO.StudentDTO;
import ru.sova.educationapp.EducationalSystemApp.models.Student;

@Mapper(uses = {VerificationWorkMapper.class}, componentModel = "spring")
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);
    @Mapping(source = "id", target = "id")
    StudentDTO toStudentDTO(Student student);
    @Mapping(source = "id", target = "id")
    Student toStudent(StudentDTO studentDTO);
}

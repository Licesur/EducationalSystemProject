package ru.sova.educationapp.EducationalSystemApp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.sova.educationapp.EducationalSystemApp.DTO.VerificationWorkDTO;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;

@Mapper(uses = {TaskMapper.class}, componentModel = "spring")
public interface VerificationWorkMapper {
    VerificationWorkMapper INSTANCE = Mappers.getMapper(VerificationWorkMapper.class);

    @Mapping(source = "id", target = "id")
    VerificationWorkDTO toVerificationWorkDTO(VerificationWork verificationWork);

    @Mapping(source = "id", target = "id")
    VerificationWork toVerificationWork(VerificationWorkDTO verificationWorkDTO);

    @Mapping(source = "verificationWorkDTO.tasks", target = "verificationWorkDTOToBeUpdated.tasks")
    void updateDTO(VerificationWorkDTO verificationWorkDTO, @MappingTarget VerificationWorkDTO verificationWorkDTOToBeUpdated);
}

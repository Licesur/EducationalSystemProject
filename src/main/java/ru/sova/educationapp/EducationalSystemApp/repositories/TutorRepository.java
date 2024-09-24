package ru.sova.educationapp.EducationalSystemApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;



@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
}

package ru.sova.educationapp.EducationalSystemApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;

@Repository
public interface VerificationWorkRepository extends JpaRepository<VerificationWork, Long> {
}

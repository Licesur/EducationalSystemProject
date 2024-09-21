package ru.sova.educationapp.EducationalSystemApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sova.educationapp.EducationalSystemApp.models.Task;
import ru.sova.educationapp.EducationalSystemApp.models.VerificationWork;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByVerificationWorksContains(VerificationWork verificationWork);
}

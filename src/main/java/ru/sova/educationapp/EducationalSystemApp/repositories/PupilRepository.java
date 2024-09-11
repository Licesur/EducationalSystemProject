package ru.sova.educationapp.EducationalSystemApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sova.educationapp.EducationalSystemApp.models.Pupil;


import java.util.List;

@Repository
public interface PupilRepository extends JpaRepository<Pupil, Integer> {

}

package ru.sova.educationapp.EducationalSystemApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.models.Tutor;
import ru.sova.educationapp.EducationalSystemApp.repositories.TutorRepository;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TutorService {

    private final TutorRepository tutorRepository;
    private final StudentService studentService;

    @Autowired
    public TutorService(TutorRepository tutorRepository, StudentService studentService) {
        this.tutorRepository = tutorRepository;
        this.studentService = studentService;
    }
    public List<Tutor> finAll(){
        return tutorRepository.findAll();
    }

    public Tutor findById(int id){
        Optional<Tutor> foundBook = tutorRepository.findById(id);

        return foundBook.orElse(null);
    }

    @Transactional(readOnly = false)
    public Tutor save(Tutor tutor){
        return tutorRepository.save(tutor);
    }

    @Transactional(readOnly = false)
    public void deleteById(int id){
        tutorRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void update(int id, Tutor tutor){
        Tutor tutorToBeUpdated = tutorRepository.findById(id).get();

        tutor.setId(id);

        //двухфакторная аунтификация!
        tutor.setStudents(tutorToBeUpdated.getStudents());
        tutorRepository.save(tutor); // соглашение - обновлять мтеодом сейв
    }

    @Transactional(readOnly = false)
    public void addPupil(Student student, Tutor tutor){
        tutor = tutorRepository.findById(tutor.getId()).get();
        if (tutor.getStudents() == null){
            tutor.setStudents(Collections.singletonList(student));
        } else if (!tutor.getStudents().contains(student)) {
            tutor.getStudents().add(student);
        }
        if (student.getTutors() == null){
            student.setTutors(Collections.singletonList(tutor));
        } else if (!student.getTutors().contains(tutor)) {
            student.getTutors().add(tutor);
        }
        studentService.save(student);
        tutorRepository.save(tutor);
    }


    //Pagination
//    public Page<Tutor> finAll(int page, int pageSize, boolean sortByYear) {
//        if (sortByYear) {
//            return tutorRepository.findAll(PageRequest.of(page, pageSize, Sort.by("publishYear")));
//        } else {
//            return tutorRepository.findAll(PageRequest.of(page, pageSize));
//
//        }
//    }
}

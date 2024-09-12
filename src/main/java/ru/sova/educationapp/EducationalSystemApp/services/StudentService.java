package ru.sova.educationapp.EducationalSystemApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sova.educationapp.EducationalSystemApp.models.Student;
import ru.sova.educationapp.EducationalSystemApp.repositories.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> finAll(){
        return studentRepository.findAll();
    }

    public Student findById(int id){
        Optional<Student> foundPerson = studentRepository.findById(id);

        return foundPerson.orElse(null);
    }

    @Transactional(readOnly = false)
    public Student save(Student student){
        return studentRepository.save(student);
    }

    @Transactional(readOnly = false)
    public void deleteById(int id){
        studentRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void update(int id, Student student){
        student.setId(id);
        studentRepository.save(student); // соглашение - обновлять мтеодом сейв
    }

//    public List<Book> getBooksByPersonId(int id) {
//        Optional<Person> person = personRepository.findById(id);
//        if (person.isPresent()) {
//            Hibernate.initialize(person.get().getBooks());
//            person.get().getBooks().forEach(book -> {
//                long daysDiff = LocalDate.now().getDayOfYear() - book.getGetDay().getDayOfYear();
//                book.setTimeToGetBack(daysDiff > 10);
//            });
//            return person.get().getBooks();
//        } else {
//            return Collections.emptyList();
//        }
//    }
}

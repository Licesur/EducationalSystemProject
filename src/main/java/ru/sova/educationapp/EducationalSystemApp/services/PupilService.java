package ru.sova.educationapp.EducationalSystemApp.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sova.educationapp.EducationalSystemApp.models.Pupil;
import ru.sova.educationapp.EducationalSystemApp.repositories.PupilRepository;
import ru.sova.educationapp.EducationalSystemApp.repositories.TutorRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PupilService {

    private final PupilRepository pupilRepository;

    @Autowired
    public PupilService(PupilRepository pupilRepository) {
        this.pupilRepository = pupilRepository;
    }

    public List<Pupil> finAll(){
        return pupilRepository.findAll();
    }

    public Pupil findById(int id){
        Optional<Pupil> foundPerson = pupilRepository.findById(id);

        return foundPerson.orElse(null);
    }

    @Transactional(readOnly = false)
    public Pupil save(Pupil pupil){
        return pupilRepository.save(pupil);
    }

    @Transactional(readOnly = false)
    public void deleteById(int id){
        pupilRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void update(int id, Pupil pupil){
        pupil.setId(id);
        pupilRepository.save(pupil); // соглашение - обновлять мтеодом сейв
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

package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.entities.Course;
import ir.maktab.arf.quiz.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    public List<Course> findAll(){
        return courseRepository.findAll();
    }

    public Course save(Course course){
        return courseRepository.save(course);
    }

    public void removeById(Long id){
        courseRepository.deleteById(id);
    }

    public Course findById(Long id){
        return courseRepository.findById(id).get();
    }
}

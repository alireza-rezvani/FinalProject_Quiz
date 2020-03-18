package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.entities.Account;
import ir.maktab.arf.quiz.entities.Course;
import ir.maktab.arf.quiz.repositories.CoursePagingRepository;
import ir.maktab.arf.quiz.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * course service
 * @author Alireza
 */

@Service
public class CourseService {

    private CourseRepository courseRepository;
    private CoursePagingRepository coursePagingRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, CoursePagingRepository coursePagingRepository) {
        this.courseRepository = courseRepository;
        this.coursePagingRepository = coursePagingRepository;
    }

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

    public List<Course> findByTeacherUsername(String teacherUsername){
        return courseRepository.findByTeacherUsername(teacherUsername);
    }

    public List<Course> findByStudentAccount(Account studentAccount){
        return courseRepository.findByStudentsContains(studentAccount);
    }

    public Page<Course> findPage(Account studentAccount, Integer pageNumber, Integer pageSize, String sortBasedOn){
        return coursePagingRepository
                .findAllByStudentsContains
                        (studentAccount, PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortBasedOn)));
    }
}

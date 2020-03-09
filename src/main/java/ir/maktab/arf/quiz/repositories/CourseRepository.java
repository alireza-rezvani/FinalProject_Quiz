package ir.maktab.arf.quiz.repositories;

import ir.maktab.arf.quiz.entities.Account;
import ir.maktab.arf.quiz.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    public List<Course> findByTeacherUsername(String teacherUsername);
    public List<Course> findByStudentsContains(Account studentAccount);
}

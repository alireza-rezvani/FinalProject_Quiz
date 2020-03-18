package ir.maktab.arf.quiz.repositories;

import ir.maktab.arf.quiz.entities.Account;
import ir.maktab.arf.quiz.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


/**
 * another repository for course that is used for pagination
 * @author Alireza
 */

@Repository
public interface CoursePagingRepository extends PagingAndSortingRepository<Course, Long> {

    public Page<Course> findAllByStudentsContains(Account studentAccount, Pageable pageable);
}

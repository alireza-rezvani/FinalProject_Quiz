package ir.maktab.arf.quiz.repositories;

import ir.maktab.arf.quiz.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * question repository
 * @author Alireza
 */

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    public List<Question> findByCreatorTeacherIdAndRelatedCourseId(Long creatorTeacherId, Long relatedCourseId);
}

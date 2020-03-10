package ir.maktab.arf.quiz.repositories;

import ir.maktab.arf.quiz.entities.QuizOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizOperationRepository extends JpaRepository<QuizOperation, Long> {
    public List<QuizOperation> findAllByStudentIdAndCourseId(Long studentId, Long courseId);
     public QuizOperation findByStudentIdAndCourseIdAndQuizId(Long studentId, Long courseId, Long quizId);
}

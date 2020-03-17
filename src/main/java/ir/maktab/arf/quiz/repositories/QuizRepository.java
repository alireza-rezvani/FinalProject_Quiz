package ir.maktab.arf.quiz.repositories;

import ir.maktab.arf.quiz.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * quiz repository
 * @author Alireza
 */

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
}

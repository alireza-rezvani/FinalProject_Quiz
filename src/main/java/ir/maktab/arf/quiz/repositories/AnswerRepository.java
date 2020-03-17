package ir.maktab.arf.quiz.repositories;

import ir.maktab.arf.quiz.entities.Answer;
import ir.maktab.arf.quiz.entities.QuizOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * answer repository
 * @author Alireza
 */

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

}

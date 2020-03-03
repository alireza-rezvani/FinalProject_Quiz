package ir.maktab.arf.quiz.repositories;

import ir.maktab.arf.quiz.abstraction.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}

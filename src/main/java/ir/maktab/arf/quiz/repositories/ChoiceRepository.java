package ir.maktab.arf.quiz.repositories;

import ir.maktab.arf.quiz.entities.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * choice repository
 * @author Alireza
 */

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long> {
}

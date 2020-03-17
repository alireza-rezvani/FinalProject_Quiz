package ir.maktab.arf.quiz.repositories;

import ir.maktab.arf.quiz.entities.Status;
import ir.maktab.arf.quiz.utilities.StatusTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * status repository
 * @author Alireza
 */

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    public Status findByTitle(StatusTitle title);
}

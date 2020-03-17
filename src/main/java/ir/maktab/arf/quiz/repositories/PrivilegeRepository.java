package ir.maktab.arf.quiz.repositories;

import ir.maktab.arf.quiz.entities.Privilege;
import ir.maktab.arf.quiz.utilities.PrivilegeTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * privilege repository
 * @author Alireza
 */

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    public Privilege findByTitle(PrivilegeTitle title);
}

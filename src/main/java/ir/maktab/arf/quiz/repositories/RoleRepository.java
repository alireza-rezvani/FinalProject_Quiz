package ir.maktab.arf.quiz.repositories;

import ir.maktab.arf.quiz.entities.Role;
import ir.maktab.arf.quiz.utilities.RoleTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * role repository
 * @author Alireza
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role findByTitle(RoleTitle title);
}

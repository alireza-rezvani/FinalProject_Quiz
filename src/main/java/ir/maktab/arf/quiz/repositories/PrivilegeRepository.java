package ir.maktab.arf.quiz.repositories;

import ir.maktab.arf.quiz.entities.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
}

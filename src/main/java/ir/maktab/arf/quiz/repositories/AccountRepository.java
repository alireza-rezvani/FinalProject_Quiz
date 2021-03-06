package ir.maktab.arf.quiz.repositories;


import ir.maktab.arf.quiz.entities.Account;
import ir.maktab.arf.quiz.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * account repository
 * @author Alireza
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    public Account findByUsername(String username);
}

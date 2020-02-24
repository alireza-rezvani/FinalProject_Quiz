package ir.maktab.quiz.arf.repositories;


import ir.maktab.quiz.arf.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    public Account findAccountByUsernameAndPassword(String username, String password);
}

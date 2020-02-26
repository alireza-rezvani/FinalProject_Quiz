package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.entities.Account;
import ir.maktab.arf.quiz.repositories.AccountRepository;
import ir.maktab.arf.quiz.repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StatusRepository statusRepository;

    public Account save(Account account){
        return accountRepository.save(account);
    }

}

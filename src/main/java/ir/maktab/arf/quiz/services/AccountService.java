package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.entities.Account;
import ir.maktab.arf.quiz.repositories.AccountRepository;
import ir.maktab.arf.quiz.repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StatusRepository statusRepository;

    public Account save(Account account){
        return accountRepository.save(account);
    }

    public boolean isUsernameExisting(String username){
        if (accountRepository.findByUsername(username) == null)
            return false;
        else
            return true;
    }

    public List<Account> findAll(){
        return accountRepository.findAll();
    }

    public Account findById(Long id){
        return accountRepository.findById(id).get();
    }

    public Account findByUsername(String username){
        return accountRepository.findByUsername(username);
    }
}
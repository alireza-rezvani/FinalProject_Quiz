package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.entities.Account;
import ir.maktab.arf.quiz.repositories.AccountRepository;
import ir.maktab.arf.quiz.repositories.StatusRepository;
import ir.maktab.arf.quiz.utilities.StatusTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * account service
 * @author Alireza
 */

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private StatusRepository statusRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, StatusRepository statusRepository) {
        this.accountRepository = accountRepository;
        this.statusRepository = statusRepository;
    }

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

    public void activateAccount(Long accountId){
        Account requestedAccount = findById(accountId);
        requestedAccount.setStatus(statusRepository.findByTitle(StatusTitle.ACTIVE));
        save(requestedAccount);
    }

    public void inActivateAccount(Long accountId){
        Account requestedAccount = findById(accountId);
        requestedAccount.setStatus(statusRepository.findByTitle(StatusTitle.INACTIVE));
        save(requestedAccount);
    }


    public Account getSignedInAccount(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails)
            username = ((UserDetails)principal).getUsername();
        else
            username = principal.toString();

        return findByUsername(username);
    }

    public List<String> getStringTitlesOfRolesOfSignedInAccount(){
        return getSignedInAccount().getRoles().stream().map(role -> role.getTitle().name()).collect(Collectors.toList());
    }
}

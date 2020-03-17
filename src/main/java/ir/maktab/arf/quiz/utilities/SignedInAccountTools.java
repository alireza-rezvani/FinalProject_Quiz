package ir.maktab.arf.quiz.utilities;

import ir.maktab.arf.quiz.entities.Account;
import ir.maktab.arf.quiz.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


/**
 * this class is used to recognize signed in account and processing it
 * @author Alireza
 */

@Component
public class SignedInAccountTools {

    private AccountService accountService;

    @Autowired
    public SignedInAccountTools(AccountService accountService) {
        this.accountService = accountService;
    }


    //following two methods copied to account service. find usages of these and refactor project
    public Account getAccount(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails)
            username = ((UserDetails)principal).getUsername();
        else
            username = principal.toString();

        return accountService.findByUsername(username);
    }

    public List<String> getStringTitlesOfRoles(){
        return getAccount().getRoles().stream().map(role -> role.getTitle().name()).collect(Collectors.toList());
    }
}

package ir.maktab.arf.quiz.utilities;

import ir.maktab.arf.quiz.entities.Account;
import ir.maktab.arf.quiz.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SignedInAccountTools {

    @Autowired
    AccountService accountService;

    public Account getAccount(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails)
            username = ((UserDetails)principal).getUsername();
        else
            username = principal.toString();

        return accountService.findByUsername(username);
    }
}

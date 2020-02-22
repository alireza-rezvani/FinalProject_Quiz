package ir.maktab.quiz.arf.controllers;

import ir.maktab.quiz.arf.entities.Account;
import ir.maktab.quiz.arf.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    public String signUp(Model model){
        model.addAttribute("account", new Account());
        return "sign-up-page";
    }
}

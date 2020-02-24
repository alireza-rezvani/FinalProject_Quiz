package ir.maktab.quiz.arf.controllers;

import ir.maktab.quiz.arf.entities.Account;
import ir.maktab.quiz.arf.entities.Role;
import ir.maktab.quiz.arf.services.AccountService;
import ir.maktab.quiz.arf.services.RoleService;
import ir.maktab.quiz.arf.services.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StatusService statusService;

    @RequestMapping(value = "/signUp", method = RequestMethod.GET)
    public String signUp(Model model){
        model.addAttribute("account", new Account());
        model.addAttribute("accountPrimaryRole", new Role());
        model.addAttribute("allRoles", roleService.findAll());
        return "sign-up-page";
    }


    @RequestMapping(value = "/signUp", params = "ok", method = RequestMethod.POST)
    public String submitSignUp(@ModelAttribute Account account, @ModelAttribute Role accountPrimaryRole){
        Account inputAccount = account;

        inputAccount.setRoles(Arrays.asList(roleService.findByTitle(accountPrimaryRole.getTitle())));
        inputAccount.setStatus(statusService.findByTitle("در انتظار تایید"));
        accountService.save(inputAccount);
        return "sign-up-successful-page";
    }

    @RequestMapping(value = "/signUp", params = "cancel", method = RequestMethod.POST)
    public String cancelSignUp(){
        return "main-page";
    }

    @RequestMapping(value = "/signIn", method = RequestMethod.GET)
    public String signIn(Model model){
        model.addAttribute("account", new Account());
        return "sign-in-page";
    }

    @RequestMapping(value = "signIn", params = "ok", method = RequestMethod.POST)
    public String submitSignIn(@ModelAttribute Account account){
        String inputUsername = account.getUsername();
        String inputPassword = account.getPassword();
        if (accountService.signIn(inputUsername, inputPassword))
            return "sign-in-successful-page";
        else return "sign-in-failed-page";
    }

    @RequestMapping(value = "signIn", params = "cancel", method = RequestMethod.POST)
    public String cancelSignIn(){return "main-page";}
}

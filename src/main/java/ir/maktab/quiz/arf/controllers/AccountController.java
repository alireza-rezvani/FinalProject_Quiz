package ir.maktab.quiz.arf.controllers;

import ir.maktab.quiz.arf.entities.Account;
import ir.maktab.quiz.arf.entities.Role;
import ir.maktab.quiz.arf.services.AccountService;
import ir.maktab.quiz.arf.services.RoleService;
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

    @RequestMapping(value = "/define", method = RequestMethod.GET)
    public String defineAccount(Model model){
        model.addAttribute("account", new Account());
        model.addAttribute("accountPrimaryRole", new Role());
        model.addAttribute("allRoles", roleService.findAll());
        return "define-account-page";
    }


    @RequestMapping(value = "/define", params = "ok", method = RequestMethod.POST)
    public String submitDefineAccount(@ModelAttribute Account account, @ModelAttribute Role accountPrimaryRole){
        Account inputAccount = account;
        Role inputRole = accountPrimaryRole;
        Role choiceRole = roleService.findByTitle(accountPrimaryRole.getTitle());
        inputAccount.setRoles(Arrays.asList(choiceRole));
        System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
        System.out.println(account);
        System.out.println(accountPrimaryRole);

        accountService.save(inputAccount);
        return "account-successful-saved-page";
    }

    @RequestMapping(value = "define", params = "cancel", method = RequestMethod.POST)
    public String cancelDefineAccount(){
        return "main-page";
    }
}

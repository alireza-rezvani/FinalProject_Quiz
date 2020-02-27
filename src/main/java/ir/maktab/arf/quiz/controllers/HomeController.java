package ir.maktab.arf.quiz.controllers;

import ir.maktab.arf.quiz.dto.SignUpInfoDto;
import ir.maktab.arf.quiz.entities.Account;
import ir.maktab.arf.quiz.entities.PersonalInfo;
import ir.maktab.arf.quiz.entities.Role;
import ir.maktab.arf.quiz.repositories.PersonalInfoRepository;
import ir.maktab.arf.quiz.services.PersonalInfoService;
import ir.maktab.arf.quiz.services.RoleService;
import ir.maktab.arf.quiz.services.StatusService;
import ir.maktab.arf.quiz.services.AccountService;
import ir.maktab.arf.quiz.utilities.RoleTitle;
import ir.maktab.arf.quiz.utilities.StatusTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequestMapping
public class HomeController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PersonalInfoService personalInfoService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StatusService statusService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/signUp", method = RequestMethod.GET)
    public String signUp(Model model){
        model.addAttribute("signUpInfoDto", new SignUpInfoDto());
        model.addAttribute("allRoles", roleService.findAll());
        return "sign-up-page";
    }


    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public String submitSignUp(@ModelAttribute SignUpInfoDto signUpInfoDto){

        boolean isInputInvalid = false;
        String redirectUrl = "redirect:/signUp/?";
        if (signUpInfoDto.getUsername().isEmpty() || accountService.isUsernameExisting(signUpInfoDto.getUsername())) {
            redirectUrl += "usernameError";
            isInputInvalid = true;
        }
        // TODO: 2/27/2020 add validation for other inputs and edit redirectUrl if needed


        if (isInputInvalid) {
            return redirectUrl;
        }
        else {
            PersonalInfo personalInfo = new PersonalInfo();
            personalInfo.setNationalCode(signUpInfoDto.getNationalCode());
            personalInfo.setFirstName(signUpInfoDto.getFirstName());
            personalInfo.setLastName(signUpInfoDto.getLastName());
            personalInfo.setMobileNumber(signUpInfoDto.getMobileNumber());
            personalInfo.setEmail(signUpInfoDto.getEmail());
            PersonalInfo savedPersonalInfo = personalInfoService.save(personalInfo);

            Account account = new Account();
            account.setUsername(signUpInfoDto.getUsername());
            account.setPassword(passwordEncoder.encode(signUpInfoDto.getPassword()));
            account.setPersonalInfo(savedPersonalInfo);
            account.setRoles(new ArrayList<>(Arrays.asList(roleService.findByTitle(RoleTitle.valueOf(signUpInfoDto.getRoleTitleName())))));
            account.setStatus(statusService.findByTitle(StatusTitle.WAITING_FOR_VERIFY));
            accountService.save(account);

            return "index";
        }

    }

//    @RequestMapping(value = "/signUp", params = "cancel", method = RequestMethod.POST)
//    public String cancelSignUp(){
//        return "index";
//    }


    @RequestMapping(value = "/signIn", method = RequestMethod.GET)
    public String signIn(){
        return "sign-in-page";
    }

//    @RequestMapping(value = "signIn", params = "ok", method = RequestMethod.POST)
//    public String submitSignIn(@ModelAttribute Account account){
//        String inputUsername = account.getUsername();
//        String inputPassword = account.getPassword();
//        if (accountService.signIn(inputUsername, inputPassword))
//            return "sign-in-successful-page";
//        else return "sign-in-failed-page";
//    }


//    @RequestMapping(value = "signIn", params = "cancel", method = RequestMethod.POST)
//    public String cancelSignIn(){return "main-page";}

//    @RequestMapping(value = "/waiting")
//    public String browseWaitingAccounts(Model model){
//        model.addAttribute("waitingAccountsList", accountService.findAllByWaitingStatus());
//        return "waiting-accounts-page";
//    }

    @RequestMapping(value = "/signOut")
    public String signOut(){
        return "index";
    }
}

package ir.maktab.arf.quiz.controllers;

import ir.maktab.arf.quiz.dto.SignUpInfoDto;
import ir.maktab.arf.quiz.entities.Account;
import ir.maktab.arf.quiz.entities.PersonalInfo;
import ir.maktab.arf.quiz.services.AccountService;
import ir.maktab.arf.quiz.services.PersonalInfoService;
import ir.maktab.arf.quiz.services.RoleService;
import ir.maktab.arf.quiz.services.StatusService;
import ir.maktab.arf.quiz.utilities.IsNumeric;
import ir.maktab.arf.quiz.utilities.PrivilegeTitle;
import ir.maktab.arf.quiz.utilities.RoleTitle;
import ir.maktab.arf.quiz.utilities.StatusTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * home controller handles general tasks in application (like sign up, menu and ...)
 * @author Alireza
 */
@Controller
@RequestMapping
public class HomeController {

    private AccountService accountService;
    private PersonalInfoService personalInfoService;
    private RoleService roleService;
    private StatusService statusService;
    private PasswordEncoder passwordEncoder;


    /**
     * preparing requirements for this controller using @Autowired
     * @param accountService is autowired by constructor
     * @param personalInfoService is autowired by constructor
     * @param roleService is autowired by constructor
     * @param statusService is autowired by constructor
     * @param passwordEncoder is autowired by constructor
     */

    @Autowired
    public HomeController(AccountService accountService,
                          PersonalInfoService personalInfoService,
                          RoleService roleService,
                          StatusService statusService,
                          PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.personalInfoService = personalInfoService;
        this.roleService = roleService;
        this.statusService = statusService;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * handles displaying menu based on signed in user authority
     * @param model contains requirements
     * @return menu-page.html
     */

    @Secured(value = {
            "ROLE_ADMIN_GENERAL_PRIVILEGE",
            "ROLE_TEACHER_GENERAL_PRIVILEGE",
            "ROLE_STUDENT_GENERAL_PRIVILEGE"})
    @RequestMapping(value = "/menu")
    public String getMenuPage(Model model){
        List<String> authoritiesNames = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(authority -> authority.toString()).collect(Collectors.toList());
        List<String> accountPrivilegesPersian = authoritiesNames.stream()
                .map(privilege -> privilege.substring(5,privilege.length()))
                .map(privilege -> PrivilegeTitle.valueOf(privilege).getPersian())
                .collect(Collectors.toList());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails)
             username = ((UserDetails)principal).getUsername();
        else
             username = principal.toString();

        model.addAttribute("statusTitleName", accountService.findByUsername(username).getStatus().getTitle().name());
        model.addAttribute("privilegesNamesPersian", accountPrivilegesPersian);
        model.addAttribute("authoritiesNames", authoritiesNames);
        return "menu-page";
    }


    /**
     * handles sign up
     * @param model contains requirements of sign up page
     * @return sign-up-page.html
     */

    @RequestMapping(value = "/signUp", method = RequestMethod.GET)
    public String signUp(Model model){
        model.addAttribute("signUpInfoDto", new SignUpInfoDto());
        model.addAttribute("allRoles", roleService.findAll());
        return "sign-up-page";
    }


    /**
     * validates user's inputs and then saves new user in database
     * @param signUpInfoDto contains user inputs
     * @return sign-in-page.html if inputs be valid
     */

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public String submitSignUp(@ModelAttribute SignUpInfoDto signUpInfoDto){

        boolean isInputInvalid = false;
        String redirectUrl = "redirect:/signUp/?";
        if (signUpInfoDto.getNationalCode().isEmpty()
                || signUpInfoDto.getNationalCode().length() != 10
                || !IsNumeric.run(signUpInfoDto.getNationalCode())
                || personalInfoService.isNationalCodeExisting(signUpInfoDto.getNationalCode())){
            if (redirectUrl.length() > 18)
                redirectUrl += "&";
            redirectUrl += "nationalCodeError";
            isInputInvalid = true;
        }
        if (signUpInfoDto.getFirstName().isEmpty()){
            if (redirectUrl.length() > 18)
                redirectUrl += "&";
            redirectUrl += "firstNameError";
            isInputInvalid = true;
        }
        if (signUpInfoDto.getLastName().isEmpty()){
            if (redirectUrl.length() > 18)
                redirectUrl += "&";
            redirectUrl += "lastNameError";
            isInputInvalid = true;
        }
        if (signUpInfoDto.getMobileNumber().isEmpty()
                || signUpInfoDto.getMobileNumber().length() != 11
                || !IsNumeric.run(signUpInfoDto.getMobileNumber())){
            if (redirectUrl.length() > 18)
                redirectUrl += "&";
            redirectUrl += "mobileNumberError";
            isInputInvalid = true;
        }
        if (signUpInfoDto.getEmail().isEmpty()
                || !signUpInfoDto.getEmail().contains("@")
                || personalInfoService.isEmailExisting(signUpInfoDto.getEmail())){
            if (redirectUrl.length() > 18)
                redirectUrl += "&";
            redirectUrl += "emailError";
            isInputInvalid = true;
        }
        if (signUpInfoDto.getUsername().isEmpty() || accountService.isUsernameExisting(signUpInfoDto.getUsername())) {
            if (redirectUrl.length() > 18)
                redirectUrl += "&";
            redirectUrl += "usernameError";
            isInputInvalid = true;
        }
        if (signUpInfoDto.getPassword().isEmpty()){
            if (redirectUrl.length() > 18)
                redirectUrl += "&";
            redirectUrl += "passwordError";
            isInputInvalid = true;
        }
        if (!signUpInfoDto.getPasswordConfirm().equals(signUpInfoDto.getPassword())){
            if (redirectUrl.length() > 18)
                redirectUrl += "&";
            redirectUrl += "passwordConfirmError";
            isInputInvalid = true;
        }


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
            account.setRoles(new ArrayList<>(Arrays.asList(roleService.findByTitle
                    (RoleTitle.valueOf(signUpInfoDto.getRoleTitleName())))));
            account.setStatus(statusService.findByTitle(StatusTitle.WAITING_FOR_VERIFY));
            accountService.save(account);

            return "sign-in-page";
        }

    }


    /**
     * takes user to sign in page
     * @return sign-in-page.html
     */

    @RequestMapping(value = "/signIn", method = RequestMethod.GET)
    public String signIn(){
        return "sign-in-page";
    }


    /**
     * takes user to first page
     * sign out task is handled by spring security
     * @return index.html
     */

    @RequestMapping(value = "/signOut")
    public String signOut(){
        return "index";
    }
}

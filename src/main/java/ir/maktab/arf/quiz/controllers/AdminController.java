package ir.maktab.arf.quiz.controllers;

import ir.maktab.arf.quiz.dto.EditAccountDto;
import ir.maktab.arf.quiz.dto.SearchAccountDto;
import ir.maktab.arf.quiz.dto.SignUpInfoDto;
import ir.maktab.arf.quiz.entities.Account;
import ir.maktab.arf.quiz.entities.Course;
import ir.maktab.arf.quiz.entities.PersonalInfo;
import ir.maktab.arf.quiz.services.*;
import ir.maktab.arf.quiz.utilities.RoleTitle;
import ir.maktab.arf.quiz.utilities.StatusTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    AccountService accountService;

    @Autowired
    RoleService roleService;

    @Autowired
    PersonalInfoService personalInfoService;

    @Autowired
    StatusService statusService;

    @Autowired
    CourseService courseService;

    @RequestMapping(value = "")
    public String getAdminPage(){
        return "admin-page";
    }

    @RequestMapping(value = "/accountsList")
    public String getAccountsList(Model model){
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("allStatuses", statusService.findAll());
        model.addAttribute("accounts", accountService.findAll());
        model.addAttribute("searchAccountDto", new SearchAccountDto());
        return "accounts-list-page";
    }

    @RequestMapping(value = "/accountsList", method = RequestMethod.POST)
    public String getFilteredAccountsList(Model model, @ModelAttribute SearchAccountDto searchAccountDto){
        List<Account> filteredAccounts = accountService.findAll();


        filteredAccounts = filteredAccounts.stream()
                .filter(account -> account.getUsername().contains(searchAccountDto.getUsername()))
                .filter(account -> account.getPersonalInfo().getNationalCode().contains(searchAccountDto.getNationalCode()))
                .filter(account -> account.getPersonalInfo().getFirstName().contains(searchAccountDto.getFirstName()))
                .filter(account -> account.getPersonalInfo().getLastName().contains(searchAccountDto.getLastName()))
                .collect(Collectors.toList());


        if (!searchAccountDto.getRoleTitleName().equals("All"))
            filteredAccounts = filteredAccounts.stream()
                .filter(account -> account.getRoles().contains(roleService.findByTitle(RoleTitle.valueOf(searchAccountDto.getRoleTitleName()))))
                    .collect(Collectors.toList());

        if (!searchAccountDto.getStatusTitleName().equals("All"))
            filteredAccounts = filteredAccounts.stream()
                .filter(account -> account.getStatus().getTitle().name().equals(searchAccountDto.getStatusTitleName()))
                    .collect(Collectors.toList());


        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("allStatuses", statusService.findAll());
        model.addAttribute("accounts", filteredAccounts);
        model.addAttribute("searchAccountDto", new SearchAccountDto());
        return "accounts-list-page";
    }

    @RequestMapping(value = "/editAccount/{id}")
    public String editAccount(Model model, @PathVariable(value = "id") final Long id){
        Account requestedAccount = accountService.findById(id);
        EditAccountDto editAccountDto = new EditAccountDto();
        editAccountDto.setId(requestedAccount.getId());
        editAccountDto.setNationalCode(requestedAccount.getPersonalInfo().getNationalCode());
        editAccountDto.setFirstName(requestedAccount.getPersonalInfo().getFirstName());
        editAccountDto.setLastName(requestedAccount.getPersonalInfo().getLastName());
        editAccountDto.setMobileNumber(requestedAccount.getPersonalInfo().getMobileNumber());
        editAccountDto.setEmail(requestedAccount.getPersonalInfo().getEmail());
        editAccountDto.setUsername(requestedAccount.getUsername());
        editAccountDto.setRoleTitleName(requestedAccount.getRoles().get(0).getTitle().name());
        editAccountDto.setStatusTitleName(requestedAccount.getStatus().getTitle().name());

        model.addAttribute("editAccountDto" , editAccountDto);
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("allStatuses", statusService.findAll());
        return "edit-account-page";
    }

    @RequestMapping(value = "/editAccount/{id}", method = RequestMethod.POST)
    public String submitEditAccount(@ModelAttribute EditAccountDto editAccountDto, @PathVariable(value = "id") final Long id) {

        boolean isInputInvalid = false;
        String redirectUrl = "redirect:/admin/editAccount/"+id+"/?";
        if (editAccountDto.getUsername().isEmpty()
                || (accountService.isUsernameExisting(editAccountDto.getUsername())
                && accountService.findByUsername(editAccountDto.getUsername()).getId() != editAccountDto.getId())) {
            redirectUrl += "usernameError";
            isInputInvalid = true;
        }
        // TODO: 2/27/2020 add validation for other inputs and edit redirectUrl if needed


        if (isInputInvalid) {
            return redirectUrl;
        } else {
            PersonalInfo requestedPersonalInfo = accountService.findById(id).getPersonalInfo();
            requestedPersonalInfo.setNationalCode(editAccountDto.getNationalCode());
            requestedPersonalInfo.setFirstName(editAccountDto.getFirstName());
            requestedPersonalInfo.setLastName(editAccountDto.getLastName());
            requestedPersonalInfo.setMobileNumber(editAccountDto.getMobileNumber());
            requestedPersonalInfo.setEmail(editAccountDto.getEmail());
            personalInfoService.save(requestedPersonalInfo);

            Account requestedAccount = accountService.findById(id);
            requestedAccount.setUsername(editAccountDto.getUsername());
            requestedAccount.setRoles(new ArrayList<>(Arrays.asList(roleService.findByTitle(RoleTitle.valueOf(editAccountDto.getRoleTitleName())))));
            requestedAccount.setStatus(statusService.findByTitle(StatusTitle.valueOf(editAccountDto.getStatusTitleName())));
            accountService.save(requestedAccount);

            return "redirect:/admin/accountsList";
        }
    }


    @RequestMapping(value = "/addCourse")
    public String addCourse(Model model){
        model.addAttribute("allCourses", courseService.findAll());
        model.addAttribute("course", new Course());
        return "add-course-page";
    }

    @RequestMapping(value = "/addCourse", method = RequestMethod.POST)
    public String submitCourseAddition(Model model, @ModelAttribute Course course){

        if (course.getStartDate() != null && course.getFinishDate() != null && !course.getTitle().isEmpty())
            courseService.save(course);

        model.addAttribute("allCourses", courseService.findAll());
        model.addAttribute("course", new Course());
        return "add-course-page";
    }


    @RequestMapping(value = "/deleteCourse/{id}")
    public String deleteCourse(Model model, @PathVariable Long id){
        courseService.removeById(id);
        model.addAttribute("allCourses", courseService.findAll());
        model.addAttribute("course", new Course());
        return "add-course-page";
    }
}

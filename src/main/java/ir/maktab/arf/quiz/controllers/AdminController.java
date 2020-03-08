package ir.maktab.arf.quiz.controllers;

import ir.maktab.arf.quiz.dto.EditAccountDto;
import ir.maktab.arf.quiz.dto.SearchAccountDto;
import ir.maktab.arf.quiz.entities.Account;
import ir.maktab.arf.quiz.entities.Course;
import ir.maktab.arf.quiz.entities.PersonalInfo;
import ir.maktab.arf.quiz.services.*;
import ir.maktab.arf.quiz.utilities.RoleTitle;
import ir.maktab.arf.quiz.utilities.StatusTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private AccountService accountService;
    private RoleService roleService;
    private PersonalInfoService personalInfoService;
    private StatusService statusService;
    private CourseService courseService;

    @Autowired
    public AdminController(AccountService accountService,
                           RoleService roleService,
                           PersonalInfoService personalInfoService,
                           StatusService statusService,
                           CourseService courseService) {
        this.accountService = accountService;
        this.roleService = roleService;
        this.personalInfoService = personalInfoService;
        this.statusService = statusService;
        this.courseService = courseService;
    }


    @RequestMapping(value = "")
    public String getAdminPage(){
        return "redirect:/menu";
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


    @RequestMapping("/accountActivation/{accountId}")
    public String activateAccount(@PathVariable Long accountId){
        if (accountService.findById(accountId).getStatus().getTitle() != StatusTitle.ACTIVE)
            accountService.activateAccount(accountId);
        else
            accountService.inActivateAccount(accountId);
        return "redirect:/admin/accountsList";
    }

    @RequestMapping(value = "/addCourse")
    public String addCourse(Model model){
        model.addAttribute("allCourses", courseService.findAll());
        model.addAttribute("course", new Course());
        return "add-course-page";
    }

    @RequestMapping(value = "/addCourse", method = RequestMethod.POST)
    public String submitCourseAddition(Model model, @ModelAttribute Course course){

        // TODO: 3/7/2020 display message if didnt save 
        if (course.getStartDate() != null && course.getFinishDate() != null && !course.getTitle().isEmpty()
                && course.getStartDate().compareTo(course.getFinishDate()) < 0)
            courseService.save(course);

        model.addAttribute("allCourses", courseService.findAll());
        model.addAttribute("course", new Course());
        return "add-course-page";
    }


    @RequestMapping(value = "/deleteCourse/{id}")
    public String deleteCourse(Model model, @PathVariable Long id){
        //quizzes will be deleted
        // we also can delete questions of this course here
        courseService.removeById(id);
        model.addAttribute("allCourses", courseService.findAll());
        model.addAttribute("course", new Course());
        return "add-course-page";
    }


    @RequestMapping(value = "/editCourse/{id}")
    public String editCourse(Model model, @PathVariable Long id){
        model.addAttribute("allCourses", courseService.findAll());
        model.addAttribute("course", courseService.findById(id));
        return "add-course-page";
    }


    @RequestMapping(value = "/courseMembers/{courseId}")
    public String getCourseMembers(Model model, @PathVariable Long courseId){
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("course", courseService.findById(courseId));
        return "course-members-page";
    }

    @RequestMapping(value = "/addMemberToCourse/{courseId}")
    public String addMemberToCourse(Model model, @PathVariable Long courseId, @RequestParam(name = "roleTitleName") String roleTitleName){
        Course requestedCourse = courseService.findById(courseId);
        List<Account> accountsWithRequestedRole = accountService.findAll().stream()
                .filter(account -> account.getRoles().contains(roleService.findByTitle(RoleTitle.valueOf(roleTitleName))))
                .collect(Collectors.toList());

        List<Account> accountsToLoad = new ArrayList<>();
        if (roleTitleName.equals("TEACHER")) {
            if (requestedCourse.getTeacher() != null) {
                accountsToLoad = accountsWithRequestedRole.stream()
                        .filter(account -> !requestedCourse.getTeacher().equals(account))
                        .collect(Collectors.toList());
            }
            else
                accountsToLoad = accountsWithRequestedRole;
        }
        else if (roleTitleName.equals("STUDENT")) {
            accountsToLoad = accountsWithRequestedRole.stream()
                    .filter(account -> !requestedCourse.getStudents().contains(account))
                    .collect(Collectors.toList());
        }
        else {
            // TODO: 2/28/2020 if members with other role added to course
        }

        model.addAttribute("roleTitleName", roleTitleName);
        model.addAttribute("roleTitleNamePersianHeader", "لیست " + RoleTitle.valueOf(roleTitleName).getPersian() + "ها");
        model.addAttribute("course", requestedCourse);
        model.addAttribute("accountsToAdd", accountsToLoad);
        return "choose-account-to-add-to-course-page";
    }
    
    @RequestMapping(value = "/addMemberToCourse/{courseId}", method = RequestMethod.POST)
    public String submitAddMemberToCourse(@PathVariable Long courseId,
                                          @RequestParam(name = "accountId") Long accountId,
                                          @RequestParam(name = "roleTitleName") String roleTitleName){
        Course requestedCourse = courseService.findById(courseId);
        Account requestedAccount = accountService.findById(accountId);
        if (roleTitleName.equals("TEACHER"))
            requestedCourse.setTeacher(requestedAccount);
        else if (roleTitleName.equals("STUDENT"))
            requestedCourse.getStudents().add(requestedAccount);
        else {
            // TODO: 2/28/2020 if members with other role added to course
        }
        courseService.save(requestedCourse);
        return "redirect:/admin/addMemberToCourse/" + courseId + "?roleTitleName=" + roleTitleName;
    }

    @RequestMapping(value = "/deleteCourseMember/{courseId}")
    public String deleteCourseMember(@PathVariable Long courseId,
                                     @RequestParam(name = "memberId") Long memberId,
                                     @RequestParam(name = "roleTitleName") String roleTitleName){
        Course requestedCourse = courseService.findById(courseId);
        Account requestedMember = accountService.findById(memberId);
        if (roleTitleName.equals("TEACHER") && requestedCourse.getTeacher().equals(requestedMember))
            requestedCourse.setTeacher(null);
        else if (roleTitleName.equals("STUDENT"))
            requestedCourse.getStudents().remove(requestedMember);
        else {
            // TODO: 2/28/2020  if members with other role added to course
        }
        courseService.save(requestedCourse);
        return "redirect:/admin/courseMembers/" + courseId;
    }

}

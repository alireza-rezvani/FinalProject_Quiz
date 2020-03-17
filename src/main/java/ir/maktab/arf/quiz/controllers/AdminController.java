package ir.maktab.arf.quiz.controllers;

import ir.maktab.arf.quiz.dto.CourseDto;
import ir.maktab.arf.quiz.dto.EditAccountDto;
import ir.maktab.arf.quiz.dto.SearchAccountDto;
import ir.maktab.arf.quiz.entities.Account;
import ir.maktab.arf.quiz.entities.Course;
import ir.maktab.arf.quiz.entities.PersonalInfo;
import ir.maktab.arf.quiz.services.*;
import ir.maktab.arf.quiz.utilities.RoleTitle;
import ir.maktab.arf.quiz.utilities.StatusTitle;
import org.joda.time.DateTime;
import org.kaveh.commons.farsi.date.JalaliDateImpl;
import org.kaveh.commons.farsi.utils.JalaliCalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Admin controller class handles admin tasks
 * @author Alireza
 */

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private AccountService accountService;
    private RoleService roleService;
    private PersonalInfoService personalInfoService;
    private StatusService statusService;
    private CourseService courseService;


    /**
     * preparing requirements for this controller using @Autowired
     * @param accountService is autowired by constructor
     * @param roleService is autowired by constructor
     * @param personalInfoService is autowired by constructor
     * @param statusService is autowired by constructor
     * @param courseService is autowired by constructor
     */

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


    /**
     * admin main page
     * @return redirect to menu page
     */

    @RequestMapping(value = "")
    public String getAdminPage(){
        return "redirect:/menu";
    }


    /**
     * this method prepares required items to display list of all accounts to admin user
     * @param model preparing requirements
     * @return accounts-list-page.html
     */

    @RequestMapping(value = "/accountsList")
    public String getAccountsList(Model model){
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("allStatuses", statusService.findAll());
        model.addAttribute("accounts", accountService.findAll());
        model.addAttribute("searchAccountDto", new SearchAccountDto());
        return "accounts-list-page";
    }


    /**
     * prepares only filtered result from all accounts
     * user can search by username, national code, first name, last name, role, status or a mix of these items
     * @param model contains prepared items
     * @param searchAccountDto data transfer object for searching between accounts
     * @return accounts-list-page.html
     */

    @RequestMapping(value = "/accountsList", method = RequestMethod.POST)
    public String getFilteredAccountsList(Model model, @ModelAttribute SearchAccountDto searchAccountDto){
        List<Account> filteredAccounts = accountService.findAll();

        filteredAccounts = filteredAccounts.stream()
                .filter(account -> account.getUsername().contains(searchAccountDto.getUsername()))
                .filter(account -> account.getPersonalInfo().getNationalCode()
                        .contains(searchAccountDto.getNationalCode()))
                .filter(account -> account.getPersonalInfo().getFirstName().contains(searchAccountDto.getFirstName()))
                .filter(account -> account.getPersonalInfo().getLastName().contains(searchAccountDto.getLastName()))
                .collect(Collectors.toList());

        if (!searchAccountDto.getRoleTitleName().equals("All"))
            filteredAccounts = filteredAccounts.stream()
                .filter(account -> account.getRoles().contains(roleService.findByTitle(RoleTitle
                        .valueOf(searchAccountDto.getRoleTitleName()))))
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

    /**
     * handles editing account by admin
     * @param model contains prepared items
     * @param id path variable that contains requested account's id
     * @return edit-account-page.html
     */
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


    /**
     * receives edit results using post method
     * validates entered information
     * updates account if validation passed
     * @param editAccountDto contains admin inputs
     * @param id requested account's id for edit
     * @return redirects to list of accounts
     */

    @RequestMapping(value = "/editAccount/{id}", method = RequestMethod.POST)
    public String submitEditAccount(@ModelAttribute EditAccountDto editAccountDto,
                                    @PathVariable(value = "id") final Long id) {

        boolean isInputInvalid = false;
        String redirectUrl = "redirect:/admin/editAccount/"+id+"/?";
        if (editAccountDto.getUsername().isEmpty()
                || (accountService.isUsernameExisting(editAccountDto.getUsername())
                && accountService.findByUsername(editAccountDto.getUsername()).getId() != editAccountDto.getId())) {
            redirectUrl += "usernameError";
            isInputInvalid = true;
        }
        // TODO: 2/27/2020 add validation for other inputs and edit redirectUrl if needed(like what i did in sign up)

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
            requestedAccount.setRoles(new ArrayList<>(
                    Arrays.asList(roleService.findByTitle(RoleTitle.valueOf(editAccountDto.getRoleTitleName())))));
            requestedAccount.setStatus(
                    statusService.findByTitle(StatusTitle.valueOf(editAccountDto.getStatusTitleName())));
            accountService.save(requestedAccount);

            return "redirect:/admin/accountsList";
        }
    }


    /**
     * this method is used only for account activation or inactivation by admin
     * using this admin does not need to enter edit page
     * and admin can activate or inactivate any account using the button prepared for each row of accounts list
     * @param accountId requested account id
     * @return redirects to accounts list
     */

    @RequestMapping("/accountActivation/{accountId}")
    public String activateAccount(@PathVariable Long accountId){
        if (accountService.findById(accountId).getStatus().getTitle() != StatusTitle.ACTIVE)
            accountService.activateAccount(accountId);
        else
            accountService.inActivateAccount(accountId);
        return "redirect:/admin/accountsList";
    }


    /**
     * prepares items for admin inputs to add course
     * we are using a persian date picker (designed by reza babakhani) for date input
     * we also use farsi-commons dependency to convert gregorian and jalali dates to each other
     * @param model contains list of all courses to show to admin and a course DTO to hold inputs
     * @return add-course-page.html
     */

    @RequestMapping(value = "/addCourse")
    public String addCourse(Model model){

        List<CourseDto> allCoursesDtos = new ArrayList<>();
        List<Course> allCourses = courseService.findAll();
        if (allCourses != null && allCourses.size() > 0){
            for (Course courseItem : allCourses){
                allCoursesDtos.add(
                        new CourseDto(
                                courseItem.getId(),
                                courseItem.getTitle(),
                                JalaliCalendarUtils.convertGregorianToJalali(
                                        new DateTime(courseItem.getStartDate())).toString(),
                                JalaliCalendarUtils.convertGregorianToJalali(
                                        new DateTime(courseItem.getFinishDate())).toString()
                        )
                );
            }
        }

        model.addAttribute("allCoursesDtoList", allCoursesDtos);
        model.addAttribute("courseDto", new CourseDto());
        return "add-course-page";
    }


    /**
     * saves course after validation of all inputs
     * @param model contains required items for add course page. its useless after using redirect
     * @param addingCourseDto contains admin inputs
     * @return redirects to add course page (with error params if needed)
     */

    @RequestMapping(value = "/addCourse", method = RequestMethod.POST)
    public String submitCourseAddition(Model model, @ModelAttribute CourseDto addingCourseDto){

        Course inputCourse = new Course();
        inputCourse.setId(addingCourseDto.getId());
        inputCourse.setTitle(addingCourseDto.getTitle());

        if (addingCourseDto.getStartDate() != null && !addingCourseDto.getStartDate().isEmpty()
                && addingCourseDto.getStartDate().split("/").length == 3) {
            //add more condition above to be sure about input validation (for example all parts being numeric)
            int persianYear = Integer.parseInt(addingCourseDto.getStartDate().split("/")[0]);
            int persianMonth = Integer.parseInt(addingCourseDto.getStartDate().split("/")[1]);
            int persianDay = Integer.parseInt(addingCourseDto.getStartDate().split("/")[2]);

            inputCourse.setStartDate(JalaliCalendarUtils
                    .convertJalaliToGregorian(new JalaliDateImpl(persianYear,persianMonth,persianDay)).toDate());
        }

        if (addingCourseDto.getFinishDate() != null && !addingCourseDto.getFinishDate().isEmpty()
                && addingCourseDto.getFinishDate().split("/").length == 3) {
            //add more condition above to be sure about input validation (for example all parts being numeric)
            int persianYear = Integer.parseInt(addingCourseDto.getFinishDate().split("/")[0]);
            int persianMonth = Integer.parseInt(addingCourseDto.getFinishDate().split("/")[1]);
            int persianDay = Integer.parseInt(addingCourseDto.getFinishDate().split("/")[2]);

            inputCourse.setFinishDate(JalaliCalendarUtils
                    .convertJalaliToGregorian(new JalaliDateImpl(persianYear,persianMonth,persianDay)).toDate());
        }

        Date dateToday = new Date();
        dateToday.setHours(0);
        dateToday.setMinutes(0);
        dateToday.setSeconds(0);
        if (inputCourse.getStartDate() != null && inputCourse.getFinishDate() != null
                && inputCourse.getTitle() != null && !inputCourse.getTitle().isEmpty()
                && inputCourse.getStartDate().getTime()/1000/60/60/24 >= dateToday.getTime()/1000/60/60/24
                && inputCourse.getStartDate().compareTo(inputCourse.getFinishDate()) < 0) {

            //because the course may be related to teacher or student or quiz
            if (inputCourse.getId() != null){
                Course requestedCourseBeforeUpdate = courseService.findById(inputCourse.getId());
                inputCourse.setTeacher(requestedCourseBeforeUpdate.getTeacher());
                inputCourse.setStudents(requestedCourseBeforeUpdate.getStudents());
                inputCourse.setQuizzes(requestedCourseBeforeUpdate.getQuizzes());
            }
            courseService.save(inputCourse);
        }

        String redirectUrl = "redirect:/admin/addCourse?";
        if (inputCourse.getTitle().isEmpty())
            redirectUrl += "&invalidTitleError";
        if (inputCourse.getStartDate().getTime()/1000/60/60/24 < dateToday.getTime()/1000/60/60/24)
            redirectUrl += "&invalidStartDateError";
        if (inputCourse.getStartDate().compareTo(inputCourse.getFinishDate()) > 0)
            redirectUrl += "&startDateGreaterThanFinishDateError";
        if (inputCourse.getStartDate().compareTo(inputCourse.getFinishDate()) == 0)
            redirectUrl += "&startAndFinishAtSameDateError";
        return redirectUrl;
    }


    /**
     * deletes selected course
     * related quizzes will be deleted too
     * we can delete related quizOperations and also related questios based on business logic
     * @param model model is useless now because we are using redirect
     * @param id requested course's id
     * @return redirects to add course page
     */

    @RequestMapping(value = "/deleteCourse/{id}")
    public String deleteCourse(Model model, @PathVariable Long id){
        //quizzes will be deleted
        // we also can delete questions of this course here
        // we can delete related quizOperations here
        courseService.removeById(id);
        return "redirect:/admin/addCourse";
    }


    /**
     * prepares required items to edit course
     * @param model takes items to add course page to be updated
     * @param id id of requested course
     * @return add-course-page.html
     */

    @RequestMapping(value = "/editCourse/{id}")
    public String editCourse(Model model, @PathVariable Long id){

        List<CourseDto> allCoursesDtos = new ArrayList<>();
        List<Course> allCourses = courseService.findAll();
        if (allCourses != null && allCourses.size() > 0){
            for (Course courseItem : allCourses){
                allCoursesDtos.add(
                        new CourseDto(
                                courseItem.getId(),
                                courseItem.getTitle(),
                                JalaliCalendarUtils.convertGregorianToJalali(
                                        new DateTime(courseItem.getStartDate())).toString(),
                                JalaliCalendarUtils.convertGregorianToJalali(
                                        new DateTime(courseItem.getFinishDate())).toString()
                        )
                );
            }
        }

        model.addAttribute("allCoursesDtoList", allCoursesDtos);

        Course requestedCourse = courseService.findById(id);
        CourseDto sendingCourseDto = new CourseDto(
                requestedCourse.getId(),
                requestedCourse.getTitle(),
                requestedCourse.getStartDate().toString(),
                requestedCourse.getFinishDate().toString()
        );
        model.addAttribute("courseDto", sendingCourseDto);
        return "add-course-page";
    }


    /**
     * to display members of course (teacher, student or ...) and access to add or delete members
     * @param model contains requirements
     * @param courseId id of requested course
     * @return course-members-page
     */

    @RequestMapping(value = "/courseMembers/{courseId}")
    public String getCourseMembers(Model model, @PathVariable Long courseId){
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("course", courseService.findById(courseId));
        return "course-members-page";
    }


    /**
     * to handle adding members to a course
     * @param model requirements
     * @param courseId requested course's id
     * @param roleTitleName string title of role
     * @return choose-account-to-add-to-course-page.html
     */

    @RequestMapping(value = "/addMemberToCourse/{courseId}")
    public String addMemberToCourse(Model model,
                                    @PathVariable Long courseId,
                                    @RequestParam(name = "roleTitleName") String roleTitleName){

        Course requestedCourse = courseService.findById(courseId);
        List<Account> accountsWithRequestedRole = accountService.findAll().stream()
                .filter(account -> account.getRoles()
                        .contains(roleService.findByTitle(RoleTitle.valueOf(roleTitleName))))
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
        model.addAttribute("roleTitleNamePersianHeader", "لیست "
                + RoleTitle.valueOf(roleTitleName).getPersian() + "ها");
        model.addAttribute("course", requestedCourse);
        model.addAttribute("accountsToAdd", accountsToLoad);
        return "choose-account-to-add-to-course-page";
    }


    /**
     * to submit adding member to the course
     * @param courseId id of requested course
     * @param accountId id of adding account
     * @param roleTitleName string title of role
     * @return redirects to adding members page
     */
    
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


    /**
     * handles deleting members of a course
     * @param courseId requested course's id
     * @param memberId id of member to delete from course
     * @param roleTitleName string title of role
     * @return redirects to course members page
     */

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

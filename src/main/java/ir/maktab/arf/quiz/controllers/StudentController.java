package ir.maktab.arf.quiz.controllers;

import ir.maktab.arf.quiz.repositories.AccountRepository;
import ir.maktab.arf.quiz.services.AccountService;
import ir.maktab.arf.quiz.services.CourseService;
import ir.maktab.arf.quiz.utilities.SignedInAccountTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
@Secured("ROLE_STUDENT_GENERAL_PRIVILEGE")
public class StudentController {

    private AccountService accountService;
    private CourseService courseService;
    private SignedInAccountTools signedInAccountTools;

    @Autowired
    public StudentController(CourseService courseService,
                             SignedInAccountTools signedInAccountTools,
                             AccountService accountService) {
        this.courseService = courseService;
        this.signedInAccountTools = signedInAccountTools;
        this.accountService = accountService;
    }

    @RequestMapping("/courses")
    public String getCourses(Model model){

        model.addAttribute("studentCourses", courseService.findByStudentAccount(signedInAccountTools.getAccount()));
        model.addAttribute("currentStudent", signedInAccountTools.getAccount());
        return "student-courses-page";
    }


    @RequestMapping("/{studentId}/course/{courseId}/quizzes")
    public String getCourseItemQuizzes(Model model, @PathVariable Long studentId, @PathVariable Long courseId){
        if (signedInAccountTools.getAccount().equals(accountService.findById(studentId))){
            model.addAttribute("courseQuizzes", courseService.findById(courseId).getQuizzes());
            model.addAttribute("currentStudent", signedInAccountTools.getAccount());
            return "quizzes-of-student-course-page";
        }
        else
            return "redirect:/menu";

    }
}

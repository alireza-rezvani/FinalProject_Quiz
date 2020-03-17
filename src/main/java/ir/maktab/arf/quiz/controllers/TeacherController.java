package ir.maktab.arf.quiz.controllers;

import ir.maktab.arf.quiz.services.CourseService;
import ir.maktab.arf.quiz.utilities.ServiceTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * teacher controller handles task of teacher user
 * @author Alireza
 */

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private CourseService courseService;


    /**
     * preparing class requirements using @Autowired
     * @param courseService is autowired by constructor
     */

    @Autowired
    public TeacherController(CourseService courseService) {
        this.courseService = courseService;
    }


    /**
     * teacher's main page
     * @return redirects to menu
     */

    @RequestMapping("")
    public String getTeacherPage(){
        return "redirect:/menu";
    }


    /**
     * prepares courses of the teacher to display
     * @param model contains page's requirements
     * @return teacher-courses-page.html
     */

    @RequestMapping("/courses")
    public String getTeacherCourses(Model model){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails)
            username = ((UserDetails)principal).getUsername();
        else
            username = principal.toString();

        model.addAttribute("teacherCourses", courseService.findByTeacherUsername(username));

        return "teacher-courses-page";
    }
}

package ir.maktab.arf.quiz.controllers;

import ir.maktab.arf.quiz.services.CourseService;
import ir.maktab.arf.quiz.utilities.ServiceTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

//    @Autowired
//    CourseService courseService;

    private CourseService courseService;

    @Autowired
    public TeacherController(CourseService courseService) {
        this.courseService = courseService;
    }

    @RequestMapping("")
    public String getTeacherPage(){
        return "redirect:/menu";
    }

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

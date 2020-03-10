package ir.maktab.arf.quiz.controllers;

import ir.maktab.arf.quiz.entities.QuizOperation;
import ir.maktab.arf.quiz.services.*;
import ir.maktab.arf.quiz.utilities.SignedInAccountTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@EnableScheduling
@Controller
@RequestMapping("/student")
@Secured("ROLE_STUDENT_GENERAL_PRIVILEGE")
public class StudentController {

    private AccountService accountService;
    private CourseService courseService;
    private SignedInAccountTools signedInAccountTools;
    private QuizOperationService quizOperationService;
    private QuizService quizService;

    @Autowired
    public StudentController(CourseService courseService,
                             SignedInAccountTools signedInAccountTools,
                             AccountService accountService,
                             QuizOperationService quizOperationService,
                             QuizService quizService) {
        this.courseService = courseService;
        this.signedInAccountTools = signedInAccountTools;
        this.accountService = accountService;
        this.quizOperationService = quizOperationService;
        this.quizService = quizService;
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
            model.addAttribute("idsOfFinishedQuizzes",
                    quizOperationService.findAllByStudentIdAndCourseId(studentId, courseId).stream()
                            .filter(quizOperation -> quizOperation.getIsFinished() != null && quizOperation.getIsFinished() == true)
                            .map(QuizOperation::getQuizId)
                            .collect(Collectors.toList()));
            model.addAttribute("idsOfInProgressQuizzes",
                    quizOperationService.findAllByStudentIdAndCourseId(studentId, courseId).stream()
                            .filter(quizOperation -> quizOperation.getIsFinished() == null || quizOperation.getIsFinished() == false)
                            .map(QuizOperation::getQuizId)
                            .collect(Collectors.toList()));
            return "quizzes-of-student-course-page";
        }
        else
            return "redirect:/menu";

    }

    @RequestMapping("/{studentId}/course/{courseId}/quiz/{quizId}/enterQuizOperation")
    public String startQuizOperation(Model model, @PathVariable Long studentId, @PathVariable Long courseId, @PathVariable Long quizId){
        if (signedInAccountTools.getAccount().equals(accountService.findById(studentId))) {

            QuizOperation quizOperation;
            if (quizOperationService.quizOperationExist(studentId, courseId, quizId)) {
                quizOperation = quizOperationService.findByStudentIdAndCourseIdAndQuizId(studentId, courseId, quizId);
            }
            else {
                quizOperation = new QuizOperation();
                quizOperation.setCourseId(courseId);
                quizOperation.setStudentId(studentId);
                quizOperation.setQuizId(quizId);
                Date startTime = new Date();
                quizOperation.setStartTime(startTime);
                quizOperation.setFinishDate(new Date(startTime.getTime() + quizService.findById(quizId).getTime() * 60000));
                quizOperation = quizOperationService.save(quizOperation);

                final QuizOperation finalQuizOperation = new QuizOperation(
                        quizOperation.getId(),
                        quizOperation.getStudentId(),
                        quizOperation.getCourseId(),
                        quizOperation.getQuizId(),
                        true,
                        quizOperation.getStartTime(),
                        quizOperation.getFinishDate(),
                        quizOperation.getAnswerList());

                Executors.newScheduledThreadPool(1).schedule(
                        () -> quizOperationService.save(finalQuizOperation),
                        quizService.findById(quizId).getTime(),
                        TimeUnit.MINUTES);
            }
            model.addAttribute("quizOperation", quizOperation);
            model.addAttribute("finishTime", quizOperation.getFinishDate().getTime());
            return "quiz-operation-page";
        }
        else
            return "redirect:/menu";

    }

    private void submitFinishingQuizOperation(QuizOperation quizOperation){
        quizOperation.setIsFinished(true);
        quizOperationService.save(quizOperation);
    }

    //******************************8test
    @RequestMapping("/testi")
    public String timerr(Model model){

        model.addAttribute("expireDate", new Date().getTime());
        System.out.println("khkjhkh");
        Executors.newScheduledThreadPool(1).schedule(() -> System.out.println("Hiii"), 15, TimeUnit.SECONDS);
        System.out.println("jhgjgjh");
        System.out.println("jgjhgjghjgkh" +
                "");

        return "testi";
    }
}

package ir.maktab.arf.quiz.controllers;

import ir.maktab.arf.quiz.entities.Course;
import ir.maktab.arf.quiz.entities.MultiChoiceQuestion;
import ir.maktab.arf.quiz.entities.Quiz;
import ir.maktab.arf.quiz.services.AccountService;
import ir.maktab.arf.quiz.services.CourseService;
import ir.maktab.arf.quiz.services.QuizService;
import ir.maktab.arf.quiz.utilities.QuestionTools;
import ir.maktab.arf.quiz.utilities.QuestionType;
import ir.maktab.arf.quiz.utilities.ScoresListTools;
import ir.maktab.arf.quiz.utilities.SignedInAccountTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
@RequestMapping("/course")
public class CourseController {

    private CourseService courseService;
    private AccountService accountService;
    private QuizService quizService;
    private SignedInAccountTools signedInAccountTools;

    @Autowired
    public CourseController(CourseService courseService,
                            AccountService accountService,
                            QuizService quizService,
                            SignedInAccountTools signedInAccountTools) {
        this.courseService = courseService;
        this.accountService = accountService;
        this.quizService = quizService;
        this.signedInAccountTools = signedInAccountTools;
    }

    @RequestMapping("/{courseId}/quizzes")
    public String getCourseQuizzes(Model model, @PathVariable Long courseId){

        if (signedInAccountTools.getAccount().equals(courseService.findById(courseId).getTeacher())){
            List<Quiz> requestedCourseQuizzes = courseService.findById(courseId).getQuizzes();
            model.addAttribute("courseQuizzes", requestedCourseQuizzes);
            model.addAttribute("quiz",new Quiz());
            model.addAttribute("currentTeacherAccount", signedInAccountTools.getAccount());
            return "quizzes-of-course-page";
        }
        else {
            return "redirect:/menu";
        }

    }


    @RequestMapping("/{courseId}/myQuizzes")
    public String getCourseMyQuizzes(Model model, @PathVariable Long courseId){

        if (signedInAccountTools.getAccount().equals(courseService.findById(courseId).getTeacher())){
            List<Quiz> requestedCourseQuizzes = courseService.findById(courseId).getQuizzes().stream()
                    .filter(quiz -> quiz.getCreatorTeacherId().equals(signedInAccountTools.getAccount().getId()))
                    .collect(Collectors.toList());
            model.addAttribute("courseQuizzes", requestedCourseQuizzes);
            model.addAttribute("quiz",new Quiz());
            model.addAttribute("currentTeacherAccount", signedInAccountTools.getAccount());
            return "quizzes-of-course-page";
        }
        else {
            return "redirect:/menu";
        }

    }



    @RequestMapping(value = "/{courseId}/addQuiz", method = RequestMethod.POST)
    public String addQuiz(Model model, @ModelAttribute Quiz quiz, @PathVariable Long courseId){

        if (signedInAccountTools.getAccount().equals(courseService.findById(courseId).getTeacher())) {

            Quiz enteredQuiz = quiz;
            if (!enteredQuiz.getTitle().isEmpty() && enteredQuiz.getTime() != null && enteredQuiz.getTime() != 0){
                Course updatingCourse = courseService.findById(courseId);
                if (enteredQuiz.getId() == null) {
                    enteredQuiz.setCourse(updatingCourse);
                    enteredQuiz.setCreatorTeacherId(signedInAccountTools.getAccount().getId());
                    updatingCourse.getQuizzes().add(enteredQuiz);
                }
                else {
                    Quiz updatingQuiz = updatingCourse.getQuizzes().stream().filter(q -> q.getId() == enteredQuiz.getId()).findFirst().get();
                    updatingQuiz.setTitle(enteredQuiz.getTitle());
                    updatingQuiz.setDescription(enteredQuiz.getDescription());
                    updatingQuiz.setTime(enteredQuiz.getTime());
                    quizService.save(updatingQuiz);
                }
                courseService.save(updatingCourse);
            }
            List<Quiz> requestedCourseQuizzes = courseService.findById(courseId).getQuizzes();
            model.addAttribute("courseQuizzes", requestedCourseQuizzes);
            model.addAttribute("quiz", new Quiz());
            model.addAttribute("currentTeacherAccount", signedInAccountTools.getAccount());
            return "quizzes-of-course-page";
        }
        else {
            return "redirect:/menu";
        }

    }

    @RequestMapping("/{courseId}/deleteQuiz/{quizId}")
    public String deleteQuiz(Model model, @PathVariable Long courseId, @PathVariable Long quizId){
        if (signedInAccountTools.getAccount().equals(courseService.findById(courseId).getTeacher())) {
            Course updatingCourse = courseService.findById(courseId);
            updatingCourse.getQuizzes().remove(quizService.findById(quizId));
            quizService.removeById(quizId);
            courseService.save(updatingCourse);

            List<Quiz> requestedCourseQuizzes = courseService.findById(courseId).getQuizzes();
            model.addAttribute("courseQuizzes", requestedCourseQuizzes);
            model.addAttribute("quiz", new Quiz());
            model.addAttribute("currentTeacherAccount", signedInAccountTools.getAccount());
            return "quizzes-of-course-page";
        }
        else {
            return "redirect:/menu";
        }
    }

    @RequestMapping("/{courseId}/editQuiz/{quizId}")
    public String editQuiz(Model model, @PathVariable Long courseId, @PathVariable Long quizId){
        if (signedInAccountTools.getAccount().equals(courseService.findById(courseId).getTeacher())) {

            List<Quiz> requestedCourseQuizzes = courseService.findById(courseId).getQuizzes();
            model.addAttribute("courseQuizzes", requestedCourseQuizzes);
            model.addAttribute("quiz", quizService.findById(quizId));
            model.addAttribute("currentTeacherAccount", signedInAccountTools.getAccount());
            return "quizzes-of-course-page";
        }
        else {
            return "redirect:/menu";
        }
    }

    @RequestMapping("{courseId}/quiz/{quizId}/activation")
    public String quizActivation(@PathVariable Long courseId, @PathVariable Long quizId){
        Quiz requestedQuiz = quizService.findById(quizId);
        if (requestedQuiz.getIsActive() != null && requestedQuiz.getIsActive() == true) {
            requestedQuiz.setIsActive(false);
            quizService.save(requestedQuiz);

            return "redirect:/course/" + courseId + "/quizzes";
        }
        else {
            long numberOfMultiChoiceQuestionsOfQuiz = requestedQuiz.getQuestions().stream()
                    .filter(question -> QuestionTools.getQuestionType(question).equals(QuestionType.MultiChoiceQuestion))
                    .count();
            long numberOfMultiChoiceQuestionsWithTrueChoice = requestedQuiz.getQuestions().stream()
                    .filter(question -> QuestionTools.getQuestionType(question).equals(QuestionType.MultiChoiceQuestion))
                    .filter(question -> QuestionTools.MultiChoiceQuestionContainsATrueChoice((MultiChoiceQuestion) question))
                    .count();

            boolean containsMultiChoiceQuestionWithoutTrueChoice = numberOfMultiChoiceQuestionsOfQuiz > numberOfMultiChoiceQuestionsWithTrueChoice;
            boolean containsZeroDefaultScore = ScoresListTools.stringToArrayList(requestedQuiz.getDefaultScoresList()).contains(0.0);

            if (containsMultiChoiceQuestionWithoutTrueChoice || containsZeroDefaultScore || requestedQuiz.getQuestions().size() == 0){
                String redirectUrl =  "redirect:/course/" + courseId + "/quizzes?";
                if (containsMultiChoiceQuestionWithoutTrueChoice)
                    redirectUrl += "&trueChoiceNotExistError";
                if (containsZeroDefaultScore)
                    redirectUrl += "&zeroDefaultScoreError";
                if (requestedQuiz.getQuestions().size() == 0)
                    redirectUrl += "&blankQuizError";

                return redirectUrl;
            }
            else {
                requestedQuiz.setIsActive(true);
                quizService.save(requestedQuiz);
                return "redirect:/course/" + courseId + "/quizzes";

            }
        }


    }

//    @RequestMapping(value = "/{courseId}/editQuiz/{quizId}", method = RequestMethod.POST)
//    public String SubmiteditQuiz(Model model, @ModelAttribute Quiz quiz, @PathVariable Long courseId, @PathVariable Long quizId){
//        if (signedInAccountTools.getAccount().equals(courseService.findById(courseId).getTeacher())) {
//            if (!quiz.getTitle().isEmpty() && quiz.getTime() != null && quiz.getTime() != 0){
//                quizService.save(quiz);
//            }
//            List<Quiz> requestedCourseQuizzes = courseService.findById(courseId).getQuizzes();
//            model.addAttribute("courseQuizzes", requestedCourseQuizzes);
//            model.addAttribute("quiz", new Quiz());
//            model.addAttribute("currentTeacherAccount", signedInAccountTools.getAccount());
//            return "quizzes-of-course-page";
//        }
//        else {
//            return "redirect:/menu";
//        }
//    }


}

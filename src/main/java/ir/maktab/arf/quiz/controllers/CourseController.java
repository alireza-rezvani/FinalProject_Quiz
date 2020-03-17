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

/**
 * Course controller class for handling tasks about course.
 * @author Alireza
 */

@Controller
@Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
@RequestMapping("/course")
public class CourseController {

    private CourseService courseService;
    private AccountService accountService;
    private QuizService quizService;
    private SignedInAccountTools signedInAccountTools;


    /**
     * preparing requirements for this controller using @Autowired.
     * @param courseService is autowired by constructor
     * @param accountService is autowired by constructor
     * @param quizService is autowired by constructor
     * @param signedInAccountTools is autowired by constructor
     */
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


    /**
     * this method prepares required item to display all quizzes of a course.
     * @param model contains requirements.
     * @param courseId requested course's id.
     * @return quizzes-of-course-page.html
     */
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


    /**
     * this method prepares only quizzes that are designed by signed in teacher(filters quizzes by creator).
     * because teacher of course may be changed by admin or in future we may have more than a teacher for a course.
     * @param model contains requirements.
     * @param courseId requested course's id.
     * @return quizzes-of-course-page.html
     */

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


    /**
     * this method prepares required items to save a quiz by teacher and also saves the quiz
     * @param model contains requirements
     * @param quiz input quiz
     * @param courseId requested course's id
     * @return quizzes-of-course-page.html
     */

    @RequestMapping(value = "/{courseId}/addQuiz", method = RequestMethod.POST)
    public String addQuiz(Model model, @ModelAttribute Quiz quiz, @PathVariable Long courseId){

        if (signedInAccountTools.getAccount().equals(courseService.findById(courseId).getTeacher())) {

            Quiz enteredQuiz = quiz;
            if (!enteredQuiz.getTitle().isEmpty() && enteredQuiz.getTime() != null
                    && enteredQuiz.getTime() > 0 && enteredQuiz.getTime() < 1440){
                Course updatingCourse = courseService.findById(courseId);
                if (enteredQuiz.getId() == null) {
                    enteredQuiz.setCourse(updatingCourse);
                    enteredQuiz.setCreatorTeacherId(signedInAccountTools.getAccount().getId());
                    updatingCourse.getQuizzes().add(enteredQuiz);
                }
                else {
                    Quiz updatingQuiz = updatingCourse.getQuizzes().stream()
                            .filter(q -> q.getId() == enteredQuiz.getId()).findFirst().get();
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


    /**
     * this method handles deleting a requested quiz
     * @param model contains requirements to display quizzes page updated
     * @param courseId id of requested course
     * @param quizId id of quiz item which is going to be deleted
     * @return quizzes-of-course-page.html
     */
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


    /**
     * handles editing a requested quiz (using update)
     * @param model contains requirements of quizzes page
     * @param courseId requested course's id
     * @param quizId id of quiz item which is going to be edited
     * @return quizzes-of-course-page.html
     */

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


    /**
     * this method handles quiz activation (and inactivation) by the teacher user
     * only activated quizzes will be accessible for the students of the course
     * quizzes are validated based on some items before activation
     * @param courseId id of requested course
     * @param quizId id of the quiz which is going to be activated(or inactivated)
     * @return redirects to quizzes of course page
     */

    @RequestMapping("{courseId}/quiz/{quizId}/activation")
    public String quizActivation(@PathVariable Long courseId, @PathVariable Long quizId){
        if (signedInAccountTools.getAccount().equals(courseService.findById(courseId).getTeacher())) {

            Quiz requestedQuiz = quizService.findById(quizId);
            if (requestedQuiz.getIsActive() != null && requestedQuiz.getIsActive() == true) {
                requestedQuiz.setIsActive(false);
                quizService.save(requestedQuiz);

                return "redirect:/course/" + courseId + "/quizzes";
            } else {
                long numberOfMultiChoiceQuestionsOfQuiz = requestedQuiz.getQuestions().stream()
                        .filter(question -> QuestionTools.getQuestionType(question)
                                .equals(QuestionType.MultiChoiceQuestion))
                        .count();
                long numberOfMultiChoiceQuestionsWithTrueChoice = requestedQuiz.getQuestions().stream()
                        .filter(question -> QuestionTools.getQuestionType(question)
                                .equals(QuestionType.MultiChoiceQuestion))
                        .filter(question -> QuestionTools
                                .MultiChoiceQuestionContainsATrueChoice((MultiChoiceQuestion) question))
                        .count();

                boolean containsMultiChoiceQuestionWithoutTrueChoice
                        = numberOfMultiChoiceQuestionsOfQuiz > numberOfMultiChoiceQuestionsWithTrueChoice;
                boolean containsZeroDefaultScore
                        = ScoresListTools.stringToArrayList(requestedQuiz.getDefaultScoresList()).contains(0.0);

                if (containsMultiChoiceQuestionWithoutTrueChoice
                        || containsZeroDefaultScore
                        || requestedQuiz.getQuestions().size() == 0) {

                    String redirectUrl = "redirect:/course/" + courseId + "/quizzes?";
                    if (containsMultiChoiceQuestionWithoutTrueChoice)
                        redirectUrl += "&trueChoiceNotExistError";
                    if (containsZeroDefaultScore)
                        redirectUrl += "&zeroDefaultScoreError";
                    if (requestedQuiz.getQuestions().size() == 0)
                        redirectUrl += "&blankQuizError";

                    return redirectUrl;
                } else {
                    requestedQuiz.setIsActive(true);
                    quizService.save(requestedQuiz);
                    return "redirect:/course/" + courseId + "/quizzes";

                }
            }

        }
        else
            return "redirect:/menu";
    }

}

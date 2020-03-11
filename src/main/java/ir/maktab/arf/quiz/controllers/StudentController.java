package ir.maktab.arf.quiz.controllers;

import ir.maktab.arf.quiz.dto.AnswerDto;
import ir.maktab.arf.quiz.entities.*;
import ir.maktab.arf.quiz.services.*;
import ir.maktab.arf.quiz.utilities.SignedInAccountTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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

    @RequestMapping("/{studentId}/course/{courseId}/quiz/{quizId}/enterQuizOperation/question/{questionNumber}")
    public String startQuizOperation(Model model,
//                                     HttpSession session,
                                     @PathVariable Long studentId,
                                     @PathVariable Long courseId,
                                     @PathVariable Long quizId,
                                     @PathVariable Integer questionNumber){
        if (signedInAccountTools.getAccount().equals(accountService.findById(studentId))) {

            QuizOperation quizOperation;
            if (quizOperationService.quizOperationExist(studentId, courseId, quizId)) {
                quizOperation = quizOperationService.findByStudentIdAndCourseIdAndQuizId(studentId, courseId, quizId);
            }
            else {
//                session.setAttribute("answersOfQuizDto", new AnswersOfQuizDto());

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

            if (quizOperation.getIsFinished() != null && quizOperation.getIsFinished() == true)
                return "quiz-operation-finish-page";

            Question questionItem = quizService.findById(quizId).getQuestions().get(questionNumber-1);
            if (questionItem instanceof DetailedQuestion)
                model.addAttribute("questionItem", (DetailedQuestion) questionItem);
            else if (questionItem instanceof MultiChoiceQuestion)
                model.addAttribute("questionItem", (MultiChoiceQuestion) questionItem);
            //other types of questions...


            AnswerDto answerDto;
            if (quizOperationService.findById(quizOperation.getId()).getAnswerList() != null
                    && quizOperationService.findById(quizOperation.getId()).getAnswerList().stream()
                    .map(answer -> answer.getQuestionNumberInQuiz()).filter(qNum -> qNum == questionNumber)
                    .count() > 0){
                Answer ans = quizOperationService.findById(quizOperation.getId()).getAnswerList().stream()
                        .filter(answer -> answer.getQuestionNumberInQuiz() == questionNumber).findFirst().get();
                answerDto = new AnswerDto(ans.getId(),ans.getContent(), ans.getQuestionId(), ans.getQuestionNumberInQuiz());
            }
            else {
                answerDto = new AnswerDto();
                answerDto.setQuestionId(questionItem.getId());
                answerDto.setQuestionNumberInQuiz(questionNumber);
            }
            model.addAttribute("answerDto", answerDto);

            model.addAttribute("finishTime", quizOperation.getFinishDate().getTime());
            model.addAttribute("allQuestionsCount", quizService.findById(quizId).getQuestions().size());
            return "quiz-operation-page";
        }
        else
            return "redirect:/menu";

    }


    @RequestMapping(value = "/{studentId}/course/{courseId}/quiz/{quizId}/enterQuizOperation/question/{questionNumber}", method = RequestMethod.POST)
    public String submitQuizOperation(Model model,
//                                      HttpSession session,
                                     @ModelAttribute AnswerDto bindingAnswerDto,
                                     @PathVariable Long studentId,
                                     @PathVariable Long courseId,
                                     @PathVariable Long quizId,
                                     @PathVariable Integer questionNumber){
        if (signedInAccountTools.getAccount().equals(accountService.findById(studentId))) {

//            ((AnswersOfQuizDto) session.getAttribute("answersOfQuizDto")).add(bindingAnswerDto);
        


            QuizOperation quizOperation = quizOperationService.findByStudentIdAndCourseIdAndQuizId(studentId, courseId, quizId);

            if (quizOperation.getIsFinished() != null && quizOperation.getIsFinished() == true)
                return "quiz-operation-finish-page";

            if (bindingAnswerDto.getQuestionId() != null && bindingAnswerDto.getQuestionNumberInQuiz() != null) {
                Answer savingAnswer = new Answer(
                        bindingAnswerDto.getId(),
                        bindingAnswerDto.getContent(),
                        bindingAnswerDto.getQuestionId(),
                        bindingAnswerDto.getQuestionNumberInQuiz(),
                        quizOperation);

                quizOperation.getAnswerList().add(savingAnswer);
                quizOperationService.save(quizOperation);
            }

            if (questionNumber <= quizService.findById(quizId).getQuestions().size()) {
                Question questionItem = quizService.findById(quizId).getQuestions().get(questionNumber - 1);//this is index of next question
                if (questionItem instanceof DetailedQuestion)
                    model.addAttribute("questionItem", (DetailedQuestion) questionItem);
                else if (questionItem instanceof MultiChoiceQuestion)
                    model.addAttribute("questionItem", (MultiChoiceQuestion) questionItem);


                AnswerDto answerDto;
                if (quizOperationService.findById(quizOperation.getId()).getAnswerList().stream()
                        .map(answer -> answer.getQuestionNumberInQuiz()).filter(qNum -> qNum == questionNumber)
                        .count() > 0){
                    Answer ans = quizOperationService.findById(quizOperation.getId()).getAnswerList().stream()
                            .filter(answer -> answer.getQuestionNumberInQuiz() == questionNumber).findFirst().get();
                    answerDto = new AnswerDto(ans.getId(),ans.getContent(), ans.getQuestionId(), ans.getQuestionNumberInQuiz());
                }
                else {
                    answerDto = new AnswerDto();
                    answerDto.setQuestionId(questionItem.getId());
                    answerDto.setQuestionNumberInQuiz(questionNumber);
                }
                model.addAttribute("answerDto", answerDto);

                model.addAttribute("allQuestionsCount", quizService.findById(quizId).getQuestions().size());

                model.addAttribute("finishTime", quizOperation.getFinishDate().getTime());
                return "quiz-operation-page";
            }
            else {
                quizOperation.setIsFinished(true);
                quizOperation.setFinishDate(new Date());
                quizOperationService.save(quizOperation);
                return "quiz-operation-finish-page";
            }
        }
        else
            return "redirect:/menu";
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

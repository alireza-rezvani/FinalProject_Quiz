package ir.maktab.arf.quiz.utilities;

import ir.maktab.arf.quiz.services.AccountService;
import ir.maktab.arf.quiz.services.QuestionService;
import ir.maktab.arf.quiz.services.QuizOperationService;
import ir.maktab.arf.quiz.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * this class is used to return needed service objects
 * @author Alireza
 */

@Component
public class ServiceTools {

    private static QuizService quizService;
    private static AccountService accountService;
    private static QuestionService questionService;
    private static QuizOperationService quizOperationService;

    @Autowired
    private void setQuizService(QuizService quizService){
        ServiceTools.quizService = quizService;
    }

    @Autowired
    private void setAccountService(AccountService accountService){
        ServiceTools.accountService = accountService;
    }

    @Autowired
    private void setQuestionService(QuestionService questionService){
        ServiceTools.questionService = questionService;
    }

    @Autowired
    private void setQuizOperationService(QuizOperationService quizOperationService){
        ServiceTools.quizOperationService = quizOperationService;
    }

    public static QuizService getQuizService(){
        return ServiceTools.quizService;
    }
    public static AccountService getAccountService(){
        return ServiceTools.accountService;
    }
    public static QuestionService getQuestionService(){
        return ServiceTools.questionService;
    }
    public static QuizOperationService getQuizOperationService(){
        return ServiceTools.quizOperationService;
    }
}

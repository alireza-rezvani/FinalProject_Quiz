package ir.maktab.arf.quiz.utilities;

import ir.maktab.arf.quiz.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceTools {

    private static QuizService quizService;

    @Autowired
    private void setQuizService(QuizService quizService){
        ServiceTools.quizService = quizService;
    }

    public static QuizService getQuizService(){
        return ServiceTools.quizService;
    }
}

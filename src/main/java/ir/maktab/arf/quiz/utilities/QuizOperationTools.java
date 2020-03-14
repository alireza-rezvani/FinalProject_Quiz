package ir.maktab.arf.quiz.utilities;

import ir.maktab.arf.quiz.entities.*;
import ir.maktab.arf.quiz.services.QuizService;

import java.util.ArrayList;
import java.util.List;

public class QuizOperationTools {

    public static String autoPrepareStudentScores(QuizOperation finishedQuizOperation){

        Quiz requestedQuiz = ServiceTools.getQuizService().findById(finishedQuizOperation.getQuizId());

        List<Double> defaultScoresOfQuiz = ScoresListTools.stringToArrayList(requestedQuiz.getDefaultScoresList());

        List<Answer> userAnswers = finishedQuizOperation.getAnswerList();


        ArrayList<Double> studentScoresList = new ArrayList<>();
        while (studentScoresList.size() < defaultScoresOfQuiz.size())
            studentScoresList.add(0.0);

        if (userAnswers != null) {
            for (Answer answerItem : userAnswers) {
                Question correspondingQuestion = ServiceTools.getQuestionService().findById(answerItem.getQuestionId());
                if (correspondingQuestion instanceof MultiChoiceQuestion) {
                    if (QuestionTools.isAnswerOfMultiChoiceQuestion((MultiChoiceQuestion) correspondingQuestion, answerItem.getContent()))
                        studentScoresList.set(answerItem.getQuestionNumberInQuiz() - 1, defaultScoresOfQuiz.get(answerItem.getQuestionNumberInQuiz() - 1));
                }
            }
        }


        return ScoresListTools.arrayListToString(studentScoresList);
    }
}

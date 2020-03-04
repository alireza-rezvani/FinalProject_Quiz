package ir.maktab.arf.quiz.utilities;

import ir.maktab.arf.quiz.entities.Choice;
import ir.maktab.arf.quiz.entities.MultiChoiceQuestion;

public class MultiChoiceQuestionTools {
    public static boolean trueChoiceExist(MultiChoiceQuestion question){
        if (question.getChoiceList().stream().filter(q -> q.getIsTrueChoice()).count() == 0)
            return false;
        else
            return true;
    }

    public static Choice getTrueChoice(MultiChoiceQuestion question){
        if (MultiChoiceQuestionTools.trueChoiceExist(question)){
            return question.getChoiceList().stream().filter(q -> q.getIsTrueChoice()).findFirst().get();
        }
        else
            return null;
    }
}

package ir.maktab.arf.quiz.utilities;

import ir.maktab.arf.quiz.entities.Question;
import ir.maktab.arf.quiz.entities.Choice;
import ir.maktab.arf.quiz.entities.MultiChoiceQuestion;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QuestionTools {

    public static QuestionType getQuestionType(Question question){

        QuestionType result = null;

        for (QuestionType qt : QuestionType.values()){
            if (question.getClass().getName().contains(qt.name())) {
                result = qt;
                break;
            }
        }
        return result;
    }

    public static List<String> getAllQuestionTypesNames(){

        List<String> result = new ArrayList<>();

        List<String> completeNames = QuestionTools.getAllQuestionTypesCompleteNames();
        for (String completeName : completeNames) {
            if (completeName.contains("."))
                result.add(completeName.substring(completeName.lastIndexOf(".") + 1));
            else
                result.add(completeName);
        }

        return result;
    }

    public static List<String> getAllQuestionTypesCompleteNames(){

        List<String> result = new ArrayList<>();

        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
        provider.addIncludeFilter(new AssignableTypeFilter(Question.class));
        Set<BeanDefinition> components = provider.findCandidateComponents("ir/maktab/arf/quiz/entities");
        for (BeanDefinition component : components){
            String beanName = component.getBeanClassName();
            result.add(beanName);
        }

        return result;
    }

    public static boolean MultiChoiceQuestionContainsATrueChoice(MultiChoiceQuestion question){
        if (question.getChoiceList().stream().filter(q -> q.getIsTrueChoice()).count() == 0)
            return false;
        else
            return true;
    }

    public static Choice getTrueChoiceOfMultiChoiceQuestion(MultiChoiceQuestion question){
        if (QuestionTools.MultiChoiceQuestionContainsATrueChoice(question)){
            return question.getChoiceList().stream().filter(q -> q.getIsTrueChoice()).findFirst().get();
        }
        else
            return null;
    }
}

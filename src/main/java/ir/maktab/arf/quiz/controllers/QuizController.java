package ir.maktab.arf.quiz.controllers;

import ir.maktab.arf.quiz.abstraction.Question;
import ir.maktab.arf.quiz.entities.Choice;
import ir.maktab.arf.quiz.entities.DetailedQuestion;
import ir.maktab.arf.quiz.entities.MultiChoiceQuestion;
import ir.maktab.arf.quiz.entities.Quiz;
import ir.maktab.arf.quiz.services.ChoiceService;
import ir.maktab.arf.quiz.services.QuestionService;
import ir.maktab.arf.quiz.services.QuizService;
import ir.maktab.arf.quiz.utilities.MultiChoiceQuestionTools;
import ir.maktab.arf.quiz.utilities.QuestionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

@Controller
@RequestMapping("/quiz")
@Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
public class QuizController {

    // TODO: 3/3/2020 control teacher validation
    // TODO: 3/4/2020 check null Booleans if needed
    @Autowired
    private QuizService quizService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ChoiceService choiceService;

    @RequestMapping("/{quizId}/questions")
    public String getQuizQuestions(Model model, @PathVariable Long quizId){

        model.addAttribute("questions", quizService.findById(quizId).getQuestions());
        model.addAttribute("questionTypes", QuestionType.values());
        return "quiz-questions-page";
    }

    @RequestMapping("/{quizId}/addQuestion/DetailedQuestion")
    public String addDetailedQuestion(Model model, @PathVariable Long quizId){
        model.addAttribute("question", new DetailedQuestion());
        return "add-detailed-question-page";
    }

    @RequestMapping(value = "/{quizId}/addQuestion/DetailedQuestion", method = RequestMethod.POST)
    public String submitAddDetailedQuestion(@ModelAttribute DetailedQuestion detailedQuestion, @PathVariable Long quizId){

        Question savingQuestion;
        if (!detailedQuestion.getTitle().isEmpty() && !detailedQuestion.getDefinition().isEmpty()) {
            savingQuestion = questionService.save(detailedQuestion);
            Quiz updatingQuiz = quizService.findById(quizId);
            updatingQuiz.getQuestions().add(savingQuestion);
            quizService.save(updatingQuiz);
        }
        return "redirect:/quiz/" + quizId + "/questions";
    }



    @RequestMapping("/{quizId}/addQuestion/MultiChoiceQuestion")
    public String addMultiChoiceQuestion(Model model, @PathVariable Long quizId){
        model.addAttribute("question", new MultiChoiceQuestion());
        return "add-multi-choice-question-page";
    }

    @RequestMapping(value = "/{quizId}/addQuestion/MultiChoiceQuestion", method = RequestMethod.POST)
    public String submitAddMultiChoiceQuestion(Model model, @ModelAttribute MultiChoiceQuestion multiChoiceQuestion, @PathVariable Long quizId){

        Question savingQuestion;
        if (!multiChoiceQuestion.getTitle().isEmpty() && !multiChoiceQuestion.getDefinition().isEmpty()) {
            savingQuestion = questionService.save(multiChoiceQuestion);

            Quiz updatingQuiz = quizService.findById(quizId);
            updatingQuiz.getQuestions().add(savingQuestion);
            quizService.save(updatingQuiz);

            model.addAttribute("question",(MultiChoiceQuestion) savingQuestion);
            return "add-multi-choice-question-page";
        }

        return "redirect:/quiz/" + quizId + "/addQuestion/MultiChoiceQuestion";
    }

    @RequestMapping("/{quizId}/question/{questionId}/addChoiceItem")
    public String addChoiceItemToQuestion(Model model, @PathVariable Long quizId, @PathVariable Long questionId){
        model.addAttribute("question", (MultiChoiceQuestion) questionService.findById(questionId));
        model.addAttribute("choice", new Choice());
        return "add-choice-items-page";
    }

    @RequestMapping(value = "/{quizId}/question/{questionId}/addChoiceItem", method = RequestMethod.POST)
    public String submitAddChoiceItemToQuestion(Model model, @ModelAttribute Choice choice, @PathVariable Long quizId, @PathVariable Long questionId){

        if (!choice.getTitle().isEmpty()){
            if (choice.getIsTrueChoice() != null && choice.getIsTrueChoice()
                    && MultiChoiceQuestionTools.trueChoiceExist((MultiChoiceQuestion) questionService.findById(questionId))
                    && MultiChoiceQuestionTools.getTrueChoice((MultiChoiceQuestion) questionService.findById(questionId)).getId() != choice.getId()){
                //dont save
            }
            else {

                Long choiceIdBeforeSave = choice.getId();
                Choice savedChoice = choiceService.save(choice);


                if (choiceIdBeforeSave == null) {
                    MultiChoiceQuestion updatingQuestion = (MultiChoiceQuestion) questionService.findById(questionId);
                    updatingQuestion.getChoiceList().add(savedChoice);
                    questionService.save(updatingQuestion);
                }
            }

        }
        model.addAttribute("question", (MultiChoiceQuestion) questionService.findById(questionId));
        model.addAttribute("choice", new Choice());
        return "add-choice-items-page";
    }

    @RequestMapping("/{quizId}/question/{questionId}/setTrueChoice/{trueChoiceId}")
    public String addTrueChoice(Model model, @PathVariable Long quizId, @PathVariable Long questionId, @PathVariable Long trueChoiceId){
        if (!MultiChoiceQuestionTools.trueChoiceExist((MultiChoiceQuestion) questionService.findById(questionId))){
            Choice requestedChoice = choiceService.findById(trueChoiceId);
            requestedChoice.setIsTrueChoice(true);
            choiceService.save(requestedChoice);
        }
        else{
            if (choiceService.findById(trueChoiceId).getIsTrueChoice()){
                Choice requestedChoice = choiceService.findById(trueChoiceId);
                requestedChoice.setIsTrueChoice(false);
                choiceService.save(requestedChoice);
            }
        }
        model.addAttribute("question", (MultiChoiceQuestion) questionService.findById(questionId));
        model.addAttribute("choice", new Choice());
        return "add-choice-items-page";
    }

    @RequestMapping("/{quizId}/question/{questionId}/editChoiceItem/{choiceId}")
    public String editChoiceItem(Model model, @PathVariable Long quizId, @PathVariable Long questionId, @PathVariable Long choiceId){
        model.addAttribute("question", (MultiChoiceQuestion) questionService.findById(questionId));
        model.addAttribute("choice", choiceService.findById(choiceId));
        return "add-choice-items-page";
    }



    @RequestMapping("/{quizId}/question/{questionId}/deleteChoiceItem/{choiceId}")
    public String deleteChoiceItem(Model model, @PathVariable Long quizId, @PathVariable Long questionId, @PathVariable Long choiceId){
        MultiChoiceQuestion requestedQuestion =(MultiChoiceQuestion) questionService.findById(questionId);
        requestedQuestion.getChoiceList().remove(choiceService.findById(choiceId));
        questionService.save(requestedQuestion);
        choiceService.deleteById(choiceId);
        model.addAttribute("question", (MultiChoiceQuestion) questionService.findById(questionId));
        model.addAttribute("choice", new Choice());
        return "add-choice-items-page";
    }


    @RequestMapping("/testi")
    public void test(){
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
        provider.addIncludeFilter(new AssignableTypeFilter(Question.class));

        Set<BeanDefinition> components = provider.findCandidateComponents("ir/maktab/arf/quiz/entities");
        for (BeanDefinition component : components)
        {

            try {
                System.out.println(component.getBeanClassName().contains("DetailedQuestion"));
//                Class cls = Class.forName(component.getBeanClassName());
            }catch (Exception e){}

        }
    }

}

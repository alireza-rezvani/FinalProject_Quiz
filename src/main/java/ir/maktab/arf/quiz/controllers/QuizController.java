package ir.maktab.arf.quiz.controllers;

import ir.maktab.arf.quiz.abstraction.Question;
import ir.maktab.arf.quiz.dto.Score;
import ir.maktab.arf.quiz.dto.ScoresOfQuizDto;
import ir.maktab.arf.quiz.entities.Choice;
import ir.maktab.arf.quiz.entities.DetailedQuestion;
import ir.maktab.arf.quiz.entities.MultiChoiceQuestion;
import ir.maktab.arf.quiz.entities.Quiz;
import ir.maktab.arf.quiz.services.ChoiceService;
import ir.maktab.arf.quiz.services.QuestionService;
import ir.maktab.arf.quiz.services.QuizService;
import ir.maktab.arf.quiz.utilities.QuestionTools;
import ir.maktab.arf.quiz.utilities.QuestionType;
import ir.maktab.arf.quiz.utilities.SignedInAccountTools;
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

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/quiz")
@Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
public class QuizController {

    // TODO: 3/3/2020 control teacher item validation
    // TODO: 3/4/2020 check null Booleans if needed
    @Autowired
    private QuizService quizService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ChoiceService choiceService;

    @Autowired
    private SignedInAccountTools signedInAccountTools;

    @RequestMapping("/{quizId}/questions")
    public String getQuizQuestions(Model model, @PathVariable Long quizId){



        ScoresOfQuizDto scores = new ScoresOfQuizDto();
        for (int i=0; i<quizService.findById(quizId).getQuestions().size(); i++) {
            if (quizService.findById(quizId).getDefaultScoresList() == null || quizService.findById(quizId).getDefaultScoresList().isEmpty())
                scores.add(new Score());
            else{
                String[] stringScores = quizService.findById(quizId).getDefaultScoresList().split("-");
                scores.add(new Score(Double.parseDouble(stringScores[i])));
            }

        }


        model.addAttribute("questions", quizService.findById(quizId).getQuestions());
        model.addAttribute("questionTypes", QuestionType.values());
        model.addAttribute("scoresDto", scores);
        return "quiz-questions-page";
    }

    @RequestMapping("/{quizId}/addQuestion/fromBank")
    public String addQuestionFromBank(Model model, @PathVariable Long quizId){
        Long currentTeacherId = signedInAccountTools.getAccount().getId();
        Long currentCourseId = quizService.findById(quizId).getCourse().getId();
        List<Question> bankQuestions = questionService.findBankQuestions(currentTeacherId, currentCourseId);
        List<Question> quizQuestions = quizService.findById(quizId).getQuestions();
        model.addAttribute("bankQuestions", bankQuestions);
        model.addAttribute("quizQuestions", quizQuestions);
        return "question-bank-page";
    }

    // TODO: 3/5/2020 ids validation in url
    @RequestMapping("/{quizId}/addQuestion/fromBank/{questionId}")
    public String addQuestionItemFromBankToQuiz(@PathVariable Long quizId, @PathVariable Long questionId){
        Quiz requestedQuiz = quizService.findById(quizId);
        Question requestedQuestion = questionService.findById(questionId);
        if (requestedQuiz.getQuestions().contains(requestedQuestion))
            requestedQuiz.getQuestions().remove(requestedQuestion);
        else
            requestedQuiz.getQuestions().add(requestedQuestion);

        quizService.save(requestedQuiz);
        return "redirect:/quiz/" + quizId + "/addQuestion/fromBank";
    }

    @RequestMapping("/{quizId}/addQuestion/DetailedQuestion")
    public String addDetailedQuestion(Model model, @PathVariable Long quizId){
        model.addAttribute("question", new DetailedQuestion());
        return "add-detailed-question-page";
    }

    @RequestMapping(value = "/{quizId}/addQuestion/DetailedQuestion", method = RequestMethod.POST)
    public String submitAddDetailedQuestion(@ModelAttribute DetailedQuestion detailedQuestion, @PathVariable Long quizId){

        Long questionIdBeforeSave = detailedQuestion.getId();
        Question savingQuestion;
        if (!detailedQuestion.getTitle().isEmpty() && !detailedQuestion.getDefinition().isEmpty()) {
//            if (questionIdBeforeSave == null) {
                detailedQuestion.setRelatedCourseId(quizService.findById(quizId).getCourse().getId());
                detailedQuestion.setCreatorTeacherId(signedInAccountTools.getAccount().getId());
//            }
            savingQuestion = questionService.save(detailedQuestion);
            if (questionIdBeforeSave == null) {
                Quiz updatingQuiz = quizService.findById(quizId);
                updatingQuiz.getQuestions().add(savingQuestion);
                quizService.save(updatingQuiz);
            }
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

        Long questionIdBeforeSave = multiChoiceQuestion.getId();
        Question savingQuestion;
        if (!multiChoiceQuestion.getTitle().isEmpty() && !multiChoiceQuestion.getDefinition().isEmpty()) {

//            if (questionIdBeforeSave == null) {
                multiChoiceQuestion.setRelatedCourseId(quizService.findById(quizId).getCourse().getId());
                multiChoiceQuestion.setCreatorTeacherId(signedInAccountTools.getAccount().getId());
//            }
            savingQuestion = questionService.save(multiChoiceQuestion);

            Quiz updatingQuiz = quizService.findById(quizId);
            if (questionIdBeforeSave == null) {
                updatingQuiz.getQuestions().add(savingQuestion);
                quizService.save(updatingQuiz);
            }
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
                    && QuestionTools.MultiChoiceQuestionContainsATrueChoice((MultiChoiceQuestion) questionService.findById(questionId))
                    && QuestionTools.getTrueChoiceOfMultiChoiceQuestion((MultiChoiceQuestion) questionService.findById(questionId)).getId() != choice.getId()){
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
        if (!QuestionTools.MultiChoiceQuestionContainsATrueChoice((MultiChoiceQuestion) questionService.findById(questionId))){
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


    @RequestMapping("/{quizId}/question/{questionId}/view")
    public String viewQuestion(Model model, @PathVariable Long quizId, @PathVariable Long questionId){
        if (questionService.findById(questionId) instanceof DetailedQuestion)
            model.addAttribute("question",(DetailedQuestion) questionService.findById(questionId));
        else if (questionService.findById(questionId) instanceof MultiChoiceQuestion)
            model.addAttribute("question",(MultiChoiceQuestion) questionService.findById(questionId));
        // TODO: 3/4/2020 add other type of questions here 
        
        return "view-question-page";
    }


    @RequestMapping("/{quizId}/question/{questionId}/edit")
    public String editQuestion(Model model, @PathVariable Long quizId, @PathVariable Long questionId){
        if (questionService.findById(questionId) instanceof DetailedQuestion) {
            model.addAttribute("question", (DetailedQuestion) questionService.findById(questionId));
            return "add-detailed-question-page";
        }
        else if (questionService.findById(questionId) instanceof MultiChoiceQuestion) {
            model.addAttribute("question", (MultiChoiceQuestion) questionService.findById(questionId));
            return "add-multi-choice-question-page";
        }
        // TODO: 3/4/2020 add other type of questions here

        return "not-reachable actually";
    }


    @RequestMapping("/{quizId}/question/{questionId}/delete")
    public String deleteQuestion(@PathVariable Long quizId, @PathVariable Long questionId){
            Quiz requestedQuiz = quizService.findById(quizId);
            requestedQuiz.getQuestions().remove(questionService.findById(questionId));
            quizService.save(requestedQuiz);

        return "redirect:/quiz/" + quizId + "/questions";
    }

    @RequestMapping("/{quizId}/saveQuizDefaultScores")
    public String saveQuizDefaultScores(@ModelAttribute ScoresOfQuizDto scoresOfQuizDto, @PathVariable Long quizId){
        // TODO: 3/6/2020 print error message for null score --- check being number
        if (!scoresOfQuizDto.getScores().stream().map(score -> score.getValue()).collect(Collectors.toList()).contains(null)){
            String defaultScoresOfQuiz = "";
            for (Double i : scoresOfQuizDto.getScores().stream().map(score -> score.getValue()).collect(Collectors.toList()))
                defaultScoresOfQuiz += i + "-";
            Quiz requestedQuiz = quizService.findById(quizId);
            requestedQuiz.setDefaultScoresList(defaultScoresOfQuiz);
            quizService.save(requestedQuiz);
        }

        return "redirect:/quiz/" + quizId + "/questions";

    }

//***************************************************************8test


    @RequestMapping("/testi")
    public void test(@ModelAttribute ScoresOfQuizDto scores){

        System.out.println(scores);


        System.out.println(signedInAccountTools.getAccount());


        Question question = new MultiChoiceQuestion("hi", Arrays.asList(new Choice(null,"ch",true)));
        Question question1 = question;
        System.out.println(question1.getClass().getName());
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
        provider.addIncludeFilter(new AssignableTypeFilter(Question.class));

        Set<BeanDefinition> components = provider.findCandidateComponents("ir/maktab/arf/quiz/entities");
        for (BeanDefinition component : components)
        {
            System.out.println(component);
            System.out.println(component.getBeanClassName());
            System.out.println(component.getBeanClassName().lastIndexOf("."));
            System.out.println(component.getBeanClassName().substring(component.getBeanClassName().lastIndexOf(".")+1));


            try {
                System.out.println(component.getBeanClassName().contains("DetailedQuestion"));
//                Class cls = Class.forName(component.getBeanClassName());
            }catch (Exception e){}

        }
    }

}

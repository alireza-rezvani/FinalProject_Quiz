package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.entities.Question;
import ir.maktab.arf.quiz.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * question service
 * @author Alireza
 */

@Service
public class QuestionService {

    private QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question save(Question question){
        return questionRepository.save(question);
    }

    public Question findById(Long id){
        return questionRepository.findById(id).get();
    }

    public List<Question> findBankQuestions(Long creatorTeacherId, Long relatedCourseId){
        return questionRepository.findByCreatorTeacherIdAndRelatedCourseId(creatorTeacherId, relatedCourseId);
    }

    public void deleteById(Long id){
        questionRepository.deleteById(id);
    }
}

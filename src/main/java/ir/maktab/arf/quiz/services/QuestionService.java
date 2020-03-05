package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.abstraction.Question;
import ir.maktab.arf.quiz.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question save(Question question){
        return questionRepository.save(question);
    }

    public Question findById(Long id){
        return questionRepository.findById(id).get();
    }

    public List<Question> findBankQuestions(Long creatorTeacherId, Long relatedCourseId){
        return questionRepository.findByCreatorTeacherIdAndRelatedCourseId(creatorTeacherId, relatedCourseId);
    }
}
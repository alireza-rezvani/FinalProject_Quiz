package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.entities.Quiz;
import ir.maktab.arf.quiz.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

//    @Autowired
//    private QuizRepository quizRepository;

    private QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz save(Quiz quiz){
        return quizRepository.save(quiz);
    }

    public Quiz findById(Long id){
        return quizRepository.findById(id).get();
    }

    public void removeById(Long id){
        quizRepository.deleteById(id);
    }
}

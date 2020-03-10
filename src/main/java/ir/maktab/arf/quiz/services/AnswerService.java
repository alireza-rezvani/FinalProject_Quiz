package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.entities.Answer;
import ir.maktab.arf.quiz.repositories.AnswerRepository;
import ir.maktab.arf.quiz.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AnswerService {
    private AnswerRepository answerRepository;
    private QuizRepository quizRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository, QuizRepository quizRepository) {
        this.answerRepository = answerRepository;
        this.quizRepository = quizRepository;
    }

}

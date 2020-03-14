package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.entities.QuizOperation;
import ir.maktab.arf.quiz.repositories.QuizOperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizOperationService {
    private QuizOperationRepository quizOperationRepository;

    @Autowired
    public QuizOperationService(QuizOperationRepository quizOperationRepository) {
        this.quizOperationRepository = quizOperationRepository;
    }

    public List<QuizOperation> findAllByStudentIdAndCourseId(Long studentId, Long courseId){
        return quizOperationRepository.findAllByStudentIdAndCourseId(studentId, courseId);
    }

    public QuizOperation save(QuizOperation quizOperation){
        return quizOperationRepository.save(quizOperation);
    }

    public boolean quizOperationExist(Long studentId, Long courseId, Long quizId){
        if (quizOperationRepository.findByStudentIdAndCourseIdAndQuizId(studentId, courseId, quizId) == null)
            return false;
        else
            return true;
    }

    public QuizOperation findByStudentIdAndCourseIdAndQuizId(Long studentId, Long courseId, Long quizId){
        return quizOperationRepository.findByStudentIdAndCourseIdAndQuizId(studentId, courseId, quizId);
    }

    public QuizOperation findById(Long id){
        return quizOperationRepository.findById(id).get();
    }

    public List<QuizOperation> findAllByQuizId(Long quizId){
        return quizOperationRepository.findAllByQuizId(quizId);
    }

    public QuizOperation findByQuizIdAndStudentId(Long quizId, Long studentId){
        return quizOperationRepository.findByQuizIdAndStudentId(quizId, studentId).get(0);
    }


    public void setFinished(Long quizId, Long stuId){
        findByQuizIdAndStudentId(quizId, stuId).setIsFinished(true);
    }

}

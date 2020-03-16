package ir.maktab.arf.quiz.utilities;

import ir.maktab.arf.quiz.entities.QuizOperation;

public class AutoFinishRunnable implements Runnable {

    private Long quizId;
    private Long studentId;

    public AutoFinishRunnable(Long quizId, Long studentId) {
        this.quizId = quizId;
        this.studentId = studentId;
    }

    @Override
    public void run() {
        QuizOperation requestedQuizOperation = ServiceTools.getQuizOperationService().findByQuizIdAndStudentId(quizId,studentId);
        if (requestedQuizOperation.getIsFinished() == null || requestedQuizOperation.getIsFinished() == false) {
            requestedQuizOperation.setIsFinished(true);
            ServiceTools.getQuizOperationService().save(requestedQuizOperation);
        }
    }
}

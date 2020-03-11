package ir.maktab.arf.quiz.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class QuizOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    private Long courseId;

    private Long quizId;

    private Boolean isFinished;

    private Date startTime;

    private Date finishDate;

    private String resultScores;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quizOperation")
    private List<Answer> answerList;
}

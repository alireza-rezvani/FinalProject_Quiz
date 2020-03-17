package ir.maktab.arf.quiz.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * defining quiz operation entity
 * @author Alireza
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class QuizOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private Long quizId;

    private Boolean isFinished;

    @Column(nullable = false)
    private Date startTime;

    @Column(nullable = false)
    private Date finishDate;

    private String resultScores;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quizOperation")
    private List<Answer> answerList;

    private Boolean isAutoGraded;

    private Boolean isCustomGraded;
}

package ir.maktab.arf.quiz.entities;

import ir.maktab.arf.quiz.abstraction.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private Integer time;

    @ManyToOne
    private Course course;

    @ManyToMany
    private List<Question> questions;

    private String defaultScoresList;
}

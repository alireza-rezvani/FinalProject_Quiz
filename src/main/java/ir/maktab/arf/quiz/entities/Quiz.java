package ir.maktab.arf.quiz.entities;

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

    // TODO: 3/7/2020 try another solution for default scores
    private String defaultScoresList;

    // TODO: 3/7/2020 creatorId attribute and disable quiz edit and delete button by other teachers
}

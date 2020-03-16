package ir.maktab.arf.quiz.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date finishDate;

    // we can define a list of teachers if we need more than a teacher for a course
    @ManyToOne
    private Account teacher;

    @ManyToMany
    private List<Account> students;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Quiz> quizzes;
}

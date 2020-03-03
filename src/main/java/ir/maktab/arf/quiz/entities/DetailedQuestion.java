package ir.maktab.arf.quiz.entities;

import ir.maktab.arf.quiz.abstraction.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("DetailedQuestion")
public class DetailedQuestion extends Question {

    @Column(nullable = false)
    private String Definition;

}

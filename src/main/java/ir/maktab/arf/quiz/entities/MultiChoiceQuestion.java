package ir.maktab.arf.quiz.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("MultiChoiceQuestion")
public class MultiChoiceQuestion extends Question {

    @Column(nullable = false)
    private String Definition;

    @OneToMany
    private List<Choice> choiceList;

}

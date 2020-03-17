package ir.maktab.arf.quiz.entities;

import ir.maktab.arf.quiz.utilities.StatusTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


/**
 * defining status entity
 * @author Alireza
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private StatusTitle title;
}

package ir.maktab.arf.quiz.entities;

import ir.maktab.arf.quiz.utilities.PrivilegeTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


/**
 * defining privilege entity
 * @author Alireza
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private PrivilegeTitle title;
}

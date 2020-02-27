package ir.maktab.arf.quiz.entities;

import ir.maktab.arf.quiz.utilities.RoleTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private RoleTitle title;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Privilege> privileges;

}

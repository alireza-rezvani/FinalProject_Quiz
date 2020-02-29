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
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToOne
    private PersonalInfo personalInfo;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
    // TODO: 2/29/2020 use set

    @ManyToOne
    private Status status;

    // TODO: 2/29/2020 add related attributes in both side (if needed) and refactor project
}

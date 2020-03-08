package ir.maktab.arf.quiz.repositories;

import ir.maktab.arf.quiz.entities.PersonalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, Long> {
    public PersonalInfo findByNationalCode(String nationalCode);
    public PersonalInfo findByEmail(String email);
}

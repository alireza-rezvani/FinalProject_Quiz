package ir.maktab.arf.quiz.repositories;

import ir.maktab.arf.quiz.entities.PersonalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, Long> {
    public PersonalInfo findByNationalCode(String nationalCode);
}

package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.entities.PersonalInfo;
import ir.maktab.arf.quiz.repositories.PersonalInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalInfoService {

    @Autowired
    PersonalInfoRepository personalInfoRepository;

    public PersonalInfo save(PersonalInfo personalInfo){
        return personalInfoRepository.save(personalInfo);
    }
}

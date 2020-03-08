package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.entities.PersonalInfo;
import ir.maktab.arf.quiz.repositories.PersonalInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalInfoService {

    private PersonalInfoRepository personalInfoRepository;

    @Autowired
    public PersonalInfoService(PersonalInfoRepository personalInfoRepository) {
        this.personalInfoRepository = personalInfoRepository;
    }

    public PersonalInfo save(PersonalInfo personalInfo){
        return personalInfoRepository.save(personalInfo);
    }

    public boolean isNationalCodeExisting(String nationalCode){
        if (personalInfoRepository.findByNationalCode(nationalCode) != null)
            return true;
        else
            return false;
    }

    public boolean isEmailExisting(String email){
        if (personalInfoRepository.findByEmail(email) != null)
            return true;
        else
            return false;
    }
}

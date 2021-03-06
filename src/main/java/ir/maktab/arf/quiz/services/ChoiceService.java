package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.entities.Choice;
import ir.maktab.arf.quiz.repositories.ChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * choice service
 * @author Alireza
 */

@Service
public class ChoiceService {

    private ChoiceRepository choiceRepository;

    @Autowired
    public ChoiceService(ChoiceRepository choiceRepository) {
        this.choiceRepository = choiceRepository;
    }

    public Choice save(Choice choice){
        return choiceRepository.save(choice);
    }

    public Choice findById(Long id){
        return choiceRepository.findById(id).get();
    }

    public void deleteById(Long id){
        choiceRepository.deleteById(id);
    }
}

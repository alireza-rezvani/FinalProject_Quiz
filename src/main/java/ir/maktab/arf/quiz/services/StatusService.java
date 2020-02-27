package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.entities.Status;
import ir.maktab.arf.quiz.repositories.StatusRepository;
import ir.maktab.arf.quiz.utilities.StatusTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    @Autowired
    StatusRepository statusRepository;

    public Status findByTitle(StatusTitle title){
        return statusRepository.findByTitle(title);
    }

    public List<Status> findAll(){
        return statusRepository.findAll();
    }
}

package ir.maktab.quiz.arf.services;

import ir.maktab.quiz.arf.entities.Role;
import ir.maktab.quiz.arf.entities.Status;
import ir.maktab.quiz.arf.repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    @Autowired
    StatusRepository statusRepository;

    public List<Status> findAll(){
        return statusRepository.findAll();
    }


    public Status findByTitle(String title){
        return statusRepository.findAll().stream().filter(status -> status.getTitle().equals(title)).findFirst().get();
    }
}

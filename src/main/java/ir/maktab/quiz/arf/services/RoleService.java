package ir.maktab.quiz.arf.services;

import ir.maktab.quiz.arf.entities.Role;
import ir.maktab.quiz.arf.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    public Role findById(Long id){
        return roleRepository.findById(id).get();
    }

    public Role findByTitle(String title){
        return roleRepository.findAll().stream().filter(role -> role.getTitle().equals(title)).findFirst().get();
    }
}

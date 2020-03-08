package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.entities.Role;
import ir.maktab.arf.quiz.repositories.RoleRepository;
import ir.maktab.arf.quiz.utilities.RoleTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    public Role findByTitle(RoleTitle title){
        return roleRepository.findByTitle(title);
    }
}

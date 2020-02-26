package ir.maktab.arf.quiz.services;

import ir.maktab.arf.quiz.repositories.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeService {

    @Autowired
    PrivilegeRepository privilegeRepository;
}

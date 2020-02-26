package ir.maktab.arf.quiz.initialize;

import ir.maktab.arf.quiz.entities.*;
import ir.maktab.arf.quiz.repositories.*;
import ir.maktab.arf.quiz.utilities.PrivilegeTitle;
import ir.maktab.arf.quiz.utilities.RoleTitle;
import ir.maktab.arf.quiz.utilities.StatusTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PersonalInfoRepository personalInfoRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        else {
            if (statusRepository.findByTitle(StatusTitle.ACTIVE) == null)
                statusRepository.save(new Status(null, StatusTitle.ACTIVE));
            if (statusRepository.findByTitle(StatusTitle.INACTIVE) == null)
                statusRepository.save(new Status(null, StatusTitle.INACTIVE));
            if (statusRepository.findByTitle(StatusTitle.WAITING_FOR_VERIFY) == null)
                statusRepository.save(new Status(null, StatusTitle.WAITING_FOR_VERIFY));
            // TODO: 2/26/2020 add more status if needed


            if (privilegeRepository.findByTitle(PrivilegeTitle.ACCOUNT_VERIFICATION) == null)
                privilegeRepository.save(new Privilege(null, PrivilegeTitle.ACCOUNT_VERIFICATION));
            // TODO: 2/26/2020 add other privileges when needed


            if (roleRepository.findByTitle(RoleTitle.ADMIN) == null) {
                List<Privilege> adminPrivileges = Arrays.asList(
                        privilegeRepository.findByTitle(PrivilegeTitle.ACCOUNT_VERIFICATION)
                );
                // TODO: 2/26/2020 add all privileges of admin
                roleRepository.save(new Role(null, RoleTitle.ADMIN, adminPrivileges));
            }

            if (roleRepository.findByTitle(RoleTitle.TEACHER) == null) {
                List<Privilege> teacherPrivileges = Arrays.asList(
                        // TODO: 2/26/2020 add teacher privileges here
                );
                roleRepository.save(new Role(null, RoleTitle.TEACHER, teacherPrivileges));
            }

            if (roleRepository.findByTitle(RoleTitle.STUDENT) == null) {
                List<Privilege> studentPrivileges = Arrays.asList(
                        // TODO: 2/26/2020 add student privileges here
                );
                roleRepository.save(new Role(null, RoleTitle.STUDENT, studentPrivileges));
            }


            if (personalInfoRepository.findByNationalCode("0000000000") == null)
                personalInfoRepository.save(new PersonalInfo(
                        null,
                        "admin",
                        "admin",
                        "0000000000",
                        "02100000000",
                        "admin@quiz.quiz")
                );


            if (accountRepository.findByUsername("admin") == null)
                accountRepository.save(new Account(
                        null,
                        "admin",
                        passwordEncoder.encode("1"),
                        personalInfoRepository.findByNationalCode("0000000000"),
                        Arrays.asList(roleRepository.findByTitle(RoleTitle.ADMIN)),
                        statusRepository.findByTitle(StatusTitle.ACTIVE))
                );

            alreadySetup = true;
        }
    }
}
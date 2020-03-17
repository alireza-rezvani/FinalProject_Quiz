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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * this class saves initial records in database if they are not exist
 * @author Alireza
 */

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private AccountRepository accountRepository;
    private PersonalInfoRepository personalInfoRepository;
    private RoleRepository roleRepository;
    private PrivilegeRepository privilegeRepository;
    private StatusRepository statusRepository;
    private PasswordEncoder passwordEncoder;

    /**
     * preparing class requirements using @Autowired
     * @param accountRepository is autowired by constructor
     * @param personalInfoRepository is autowired by constructor
     * @param roleRepository is autowired by constructor
     * @param privilegeRepository is autowired by constructor
     * @param statusRepository is autowired by constructor
     * @param passwordEncoder is autowired by constructor
     */

    @Autowired
    public InitialDataLoader(AccountRepository accountRepository,
                             PersonalInfoRepository personalInfoRepository,
                             RoleRepository roleRepository,
                             PrivilegeRepository privilegeRepository,
                             StatusRepository statusRepository,
                             PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.personalInfoRepository = personalInfoRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * saves initial records if not exist
     * @param event context refreshed event
     */

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


            if (privilegeRepository.findByTitle(PrivilegeTitle.ADMIN_GENERAL_PRIVILEGE) == null)
                privilegeRepository.save(new Privilege(null, PrivilegeTitle.ADMIN_GENERAL_PRIVILEGE));
            if (privilegeRepository.findByTitle(PrivilegeTitle.TEACHER_GENERAL_PRIVILEGE) == null)
                privilegeRepository.save(new Privilege(null, PrivilegeTitle.TEACHER_GENERAL_PRIVILEGE));
            if (privilegeRepository.findByTitle(PrivilegeTitle.STUDENT_GENERAL_PRIVILEGE) == null)
                privilegeRepository.save(new Privilege(null, PrivilegeTitle.STUDENT_GENERAL_PRIVILEGE));

            // TODO: 2/26/2020 add other privileges when needed


            if (roleRepository.findByTitle(RoleTitle.ADMIN) == null) {
                List<Privilege> adminPrivileges = new ArrayList<>(
                        Arrays.asList(privilegeRepository.findByTitle(PrivilegeTitle.ADMIN_GENERAL_PRIVILEGE)));
                // TODO: 2/26/2020 add all privileges of admin
                roleRepository.save(new Role(null, RoleTitle.ADMIN, adminPrivileges));
            }

            if (roleRepository.findByTitle(RoleTitle.TEACHER) == null) {
                List<Privilege> teacherPrivileges = new ArrayList<>(
                        Arrays.asList(privilegeRepository.findByTitle(PrivilegeTitle.TEACHER_GENERAL_PRIVILEGE)));
                // TODO: 2/26/2020 add all privileges of teacher
                roleRepository.save(new Role(null, RoleTitle.TEACHER, teacherPrivileges));
            }

            if (roleRepository.findByTitle(RoleTitle.STUDENT) == null) {
                List<Privilege> studentPrivileges = new ArrayList<>(
                        Arrays.asList(privilegeRepository.findByTitle(PrivilegeTitle.STUDENT_GENERAL_PRIVILEGE)));
                // TODO: 2/26/2020 add all privileges of student
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
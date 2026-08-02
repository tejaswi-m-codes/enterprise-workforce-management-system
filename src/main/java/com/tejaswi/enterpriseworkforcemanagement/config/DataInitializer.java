package com.tejaswi.enterpriseworkforcemanagement.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tejaswi.enterpriseworkforcemanagement.entity.Role;
import com.tejaswi.enterpriseworkforcemanagement.entity.RoleType;
import com.tejaswi.enterpriseworkforcemanagement.entity.User;
import com.tejaswi.enterpriseworkforcemanagement.repository.RoleRepository;
import com.tejaswi.enterpriseworkforcemanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner{

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){

        createRoleIfNotExists(RoleType.ADMIN);
        createRoleIfNotExists(RoleType.MANAGER);
        createRoleIfNotExists(RoleType.EMPLOYEE);

        createAdminUser();
        createManagerUser();

    }

    private void createRoleIfNotExists(RoleType roleType){
        roleRepository.findByName(roleType)
            .orElseGet(()->{
                Role role=new Role();
                role.setName(roleType);
                return roleRepository.save(role);
            });
    }

    private void createAdminUser(){
        if(userRepository.existsByEmail("admin@company.com")){
            return;
        }

        Role adminRole=roleRepository.findByName(RoleType.ADMIN).orElseThrow();

        User admin = new User();
        admin.setUsername("ADMIN001");
        admin.setEmail("admin@company.com");
        admin.setPassword(passwordEncoder.encode("Admin@123"));
        admin.setEnabled(true);
        admin.setPasswordChanged(true);
        admin.setRoles(Set.of(adminRole));

        userRepository.save(admin);
    }

    private void createManagerUser(){
        if(userRepository.existsByEmail("manager@company.com")){
            return;
        }

        Role managerRole = roleRepository.findByName(RoleType.MANAGER).orElseThrow();

        User manager = new User();

        manager.setUsername("MANAGER001");
        manager.setEmail("manager@company.com");
        manager.setPassword(passwordEncoder.encode("Manager@123"));
        manager.setEnabled(true);
        manager.setPasswordChanged(true);
        manager.setRoles(Set.of(managerRole));

        userRepository.save(manager);

    }

}

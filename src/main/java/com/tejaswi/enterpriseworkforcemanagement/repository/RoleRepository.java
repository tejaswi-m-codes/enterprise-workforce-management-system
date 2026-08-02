package com.tejaswi.enterpriseworkforcemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejaswi.enterpriseworkforcemanagement.entity.Role;
import com.tejaswi.enterpriseworkforcemanagement.entity.RoleType;

import java.util.Optional;



public interface RoleRepository extends JpaRepository<Role,Long>{
    Optional<Role> findByName(RoleType name);

}

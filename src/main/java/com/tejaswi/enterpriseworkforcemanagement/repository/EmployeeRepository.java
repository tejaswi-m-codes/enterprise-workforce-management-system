package com.tejaswi.enterpriseworkforcemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tejaswi.enterpriseworkforcemanagement.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long>{

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmail(String email);

}

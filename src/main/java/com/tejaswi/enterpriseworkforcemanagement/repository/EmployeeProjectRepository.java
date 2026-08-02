package com.tejaswi.enterpriseworkforcemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejaswi.enterpriseworkforcemanagement.entity.EmployeeProject;
import com.tejaswi.enterpriseworkforcemanagement.entity.EmployeeProjectId;

public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject,EmployeeProjectId>{

}

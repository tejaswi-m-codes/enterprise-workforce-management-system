package com.tejaswi.enterpriseworkforcemanagement.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.tejaswi.enterpriseworkforcemanagement.dto.EmployeeRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.EmployeeResponseDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.EmployeeUpdateRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.ProjectResponseDTO;

public interface EmployeeService {

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO request);

    EmployeeResponseDTO getEmployeeById(Long employeeId);

    Page<EmployeeResponseDTO> getAllEmployees(int page,int size);

    EmployeeResponseDTO updateEmployee(Long employeeId,EmployeeUpdateRequestDTO request);

    void deleteEmployee(Long employeeId);

    void assignProjectsToEmployee(Long employeeId, Long projectId);

    void removeProjectFromEmployee(Long employeeId,Long projectId);

    List<ProjectResponseDTO> getProjectsByEmployee(Long employeeId);

}

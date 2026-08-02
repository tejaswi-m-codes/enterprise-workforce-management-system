package com.tejaswi.enterpriseworkforcemanagement.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.tejaswi.enterpriseworkforcemanagement.dto.DepartmentRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.DepartmentResponseDTO;

public interface DepartmentService {

    DepartmentResponseDTO createDepartment(DepartmentRequestDTO department);

    DepartmentResponseDTO getDepartmentById(Long departmentId);

    Page<DepartmentResponseDTO> getAllDepartments(int page,int size);

    DepartmentResponseDTO updateDepartment(Long departmentId,DepartmentRequestDTO department);

    void deleteDepartment(Long departmentId);
    

}

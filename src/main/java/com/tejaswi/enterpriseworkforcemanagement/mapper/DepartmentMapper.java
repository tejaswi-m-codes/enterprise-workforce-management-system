package com.tejaswi.enterpriseworkforcemanagement.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.tejaswi.enterpriseworkforcemanagement.dto.DepartmentRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.DepartmentResponseDTO;
import com.tejaswi.enterpriseworkforcemanagement.entity.Department;

@Component
public class DepartmentMapper {


    public Department toEntity(DepartmentRequestDTO requestDTO) {

        Department department = new Department();

        department.setDepartmentName(requestDTO.getDepartmentName());
        department.setLocation(requestDTO.getLocation());
        department.setDepartmentCode(requestDTO.getDepartmentCode());
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        department.setBudget(requestDTO.getBudget());

        return department;
    }


    public DepartmentResponseDTO toResponseDTO(Department department) {

        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO();

        responseDTO.setDepartmentId(department.getDepartmentId());
        responseDTO.setDepartmentName(department.getDepartmentName());
        responseDTO.setLocation(department.getLocation());

        return responseDTO;
    }
}

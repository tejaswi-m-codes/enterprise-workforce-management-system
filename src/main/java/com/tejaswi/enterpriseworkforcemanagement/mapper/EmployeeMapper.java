package com.tejaswi.enterpriseworkforcemanagement.mapper;

import org.springframework.stereotype.Component;

import com.tejaswi.enterpriseworkforcemanagement.dto.EmployeeRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.EmployeeResponseDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.EmployeeUpdateRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.entity.Employee;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequestDTO request) {

        Employee employee = new Employee();

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setDateofBirth(request.getDateOfBirth());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setSalary(request.getSalary());
        employee.setEmploymentStatus(request.getEmploymentStatus());

        // Department is NOT set here.
        // Service layer will fetch Department entity and set it.

        return employee;
    }

    public EmployeeResponseDTO toResponseDTO(Employee employee) {

        EmployeeResponseDTO response = new EmployeeResponseDTO();

        response.setEmployeeId(employee.getEmployeeId());
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setPhoneNumber(employee.getPhoneNumber());
        response.setDateOfBirth(employee.getDateofBirth());
        response.setJoiningDate(employee.getJoiningDate());
        response.setSalary(employee.getSalary());
        response.setEmploymentStatus(employee.getEmploymentStatus());

        if (employee.getDepartment() != null) {
            response.setDepartmentId(employee.getDepartment().getDepartmentId());
            response.setDepartmentName(employee.getDepartment().getDepartmentName());
        }

        return response;
    }

    public void updateEntity(Employee employee, EmployeeUpdateRequestDTO request) {

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setDateofBirth(request.getDateOfBirth());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setSalary(request.getSalary());
        employee.setEmploymentStatus(request.getEmploymentStatus());

        // Department will be updated in Service if required.
    }

}

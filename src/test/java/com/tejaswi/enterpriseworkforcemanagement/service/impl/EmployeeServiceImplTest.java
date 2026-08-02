package com.tejaswi.enterpriseworkforcemanagement.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tejaswi.enterpriseworkforcemanagement.dto.EmployeeRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.EmployeeResponseDTO;
import com.tejaswi.enterpriseworkforcemanagement.entity.Department;
import com.tejaswi.enterpriseworkforcemanagement.entity.Employee;
import com.tejaswi.enterpriseworkforcemanagement.exception.DepartmentNotFoundException;
import com.tejaswi.enterpriseworkforcemanagement.exception.EmployeeAlreadyExistsException;
import com.tejaswi.enterpriseworkforcemanagement.mapper.EmployeeMapper;
import com.tejaswi.enterpriseworkforcemanagement.mapper.ProjectMapper;
import com.tejaswi.enterpriseworkforcemanagement.repository.DepartmentRepository;
import com.tejaswi.enterpriseworkforcemanagement.repository.EmployeeRepository;
import com.tejaswi.enterpriseworkforcemanagement.repository.ProjectRepository;
import com.tejaswi.enterpriseworkforcemanagement.service.AuthService;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    //creates fake objects
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    @Test
    void createEmployee_shouldCreateEmployeeSuccessfully(){

        EmployeeRequestDTO request = 
                    EmployeeRequestDTO.builder()
                            .employeeCode("EMP1")
                            .firstName("Peter")
                            .lastName("Parker")
                            .email("peterparker@company.com")
                            .phoneNumber("9999999999")
                            .dateOfBirth(LocalDate.of(2000, 1, 1))
                            .joiningDate(LocalDate.now())
                            .salary(new BigDecimal("50000"))
                            .employmentStatus("ACTIVE")
                            .departmentId(1L)
                            .build();
        
    Department department = new Department();
    department.setDepartmentId(1L);
    department.setDepartmentName("Information Technology");


    Employee employee = new Employee();
    employee.setEmployeeCode("EMP1");
    employee.setEmail("peterparker@company.com");


    EmployeeResponseDTO responseDTO =
            EmployeeResponseDTO.builder()
                    .employeeCode("EMP1")
                    .firstName("Peter")
                    .build();
    
    //mock
    when(employeeRepository.existsByEmployeeCode("EMP1"))
            .thenReturn(false);


    when(employeeRepository.existsByEmail("peterparker@company.com"))
            .thenReturn(false);


    when(departmentRepository.findById(1L))
            .thenReturn(Optional.of(department));

    when(employeeMapper.toEntity(request))
            .thenReturn(employee);


    when(employeeRepository.save(employee))
            .thenReturn(employee);


    doNothing()
        .when(authService)
        .createUserForEmployee(any(Employee.class));


    when(employeeMapper.toResponseDTO(employee))
            .thenReturn(responseDTO);

    // Act

    EmployeeResponseDTO result =
            employeeServiceImpl.createEmployee(request);
    
    // Assert

    assertEquals("EMP1", result.getEmployeeCode());

    assertEquals("Peter", result.getFirstName());


    verify(employeeRepository)
            .save(employee);


    verify(authService)
            .createUserForEmployee(employee);


    }

    @Test
    void createEmployee_shouldThrowException_WhenEmployeeCodeAlreadyExists() {

    // Arrange
    EmployeeRequestDTO request = EmployeeRequestDTO.builder()
            .employeeCode("EMP1")
            .email("peterparker@company.com")
            .departmentId(1L)
            .build();

    when(employeeRepository.existsByEmployeeCode("EMP1"))
            .thenReturn(true);

    EmployeeAlreadyExistsException exception = 
                assertThrows(EmployeeAlreadyExistsException.class, ()->employeeServiceImpl.createEmployee(request));

    assertEquals("Employee code already exists",exception.getMessage());

    //verify that bo further interactions happened
    verify(employeeRepository,never()).save(any(Employee.class));
    verify(authService,never()).createUserForEmployee(any(Employee.class));

    }

    @Test
    void createEmployee_shouldThrowException_WhenDepartmentNotFound() {

        // Arrange
    EmployeeRequestDTO request = EmployeeRequestDTO.builder()
            .employeeCode("EMP1")
            .email("peterparker@company.com")
            .departmentId(1L)
            .build();

     when(employeeRepository.existsByEmployeeCode("EMP1"))
                .thenReturn(false);

     when(employeeRepository.existsByEmail("peterparker@company.com"))
                .thenReturn(false);
     
     when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

     //Act+Assert
     DepartmentNotFoundException exception=assertThrows(DepartmentNotFoundException.class, 
        ()->employeeServiceImpl.createEmployee(request));

    assertEquals("Department not found for id : 1",exception.getMessage());

    verify(employeeRepository,never()).save(any(Employee.class));
    verify(authService,never()).createUserForEmployee(any(Employee.class));

    }

    

}

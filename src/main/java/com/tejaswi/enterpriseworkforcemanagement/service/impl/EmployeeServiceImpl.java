package com.tejaswi.enterpriseworkforcemanagement.service.impl;

import com.tejaswi.enterpriseworkforcemanagement.repository.EmployeeProjectRepository;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tejaswi.enterpriseworkforcemanagement.dto.EmployeeRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.EmployeeResponseDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.EmployeeUpdateRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.ProjectResponseDTO;
import com.tejaswi.enterpriseworkforcemanagement.entity.Employee;
import com.tejaswi.enterpriseworkforcemanagement.entity.Project;
import com.tejaswi.enterpriseworkforcemanagement.entity.Department;
import com.tejaswi.enterpriseworkforcemanagement.exception.BusinessValidationException;
import com.tejaswi.enterpriseworkforcemanagement.exception.DepartmentNotFoundException;
import com.tejaswi.enterpriseworkforcemanagement.exception.EmployeeAlreadyExistsException;
import com.tejaswi.enterpriseworkforcemanagement.exception.EmployeeNotFoundException;
import com.tejaswi.enterpriseworkforcemanagement.exception.ProjectNotFoundException;
import com.tejaswi.enterpriseworkforcemanagement.mapper.EmployeeMapper;
import com.tejaswi.enterpriseworkforcemanagement.mapper.ProjectMapper;
import com.tejaswi.enterpriseworkforcemanagement.repository.DepartmentRepository;
import com.tejaswi.enterpriseworkforcemanagement.repository.EmployeeRepository;
import com.tejaswi.enterpriseworkforcemanagement.repository.ProjectRepository;
import com.tejaswi.enterpriseworkforcemanagement.service.AuthService;
import com.tejaswi.enterpriseworkforcemanagement.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;

    private final DepartmentRepository departmentRepository;
    
    private final EmployeeMapper employeeMapper;

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;


    private final AuthService authService;


    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
        DepartmentRepository departmentRepository,EmployeeMapper employeeMapper, 
        ProjectRepository projectRepository,ProjectMapper projectMapper,AuthService authService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository=departmentRepository;
        this.employeeMapper=employeeMapper;
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.authService=authService;

    }

    //Why did you use @Transactional here
    /*
    here employee onboarding isn't complete until both the employee record and
     login account are created. Since these operations form a single business transaction, we wrapped them in a transaction so that if user creation fails, the employee record is rolled back, keeping the system consistent
     */
    @Transactional
    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO request) {

        boolean empCodeExists=employeeRepository.existsByEmployeeCode(request.getEmployeeCode());

        if(empCodeExists){
            throw new EmployeeAlreadyExistsException("Employee code already exists");
        }

        boolean emailExists=employeeRepository.existsByEmail(request.getEmail());

        if(emailExists){
            throw new EmployeeAlreadyExistsException("Email already exists");
        }

        Department d=departmentRepository.findById(request.getDepartmentId())
                            .orElseThrow(()->new DepartmentNotFoundException("Department not found for id : "+request.getDepartmentId()));

        Employee employee=employeeMapper.toEntity(request);

        employee.setDepartment(d);

        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        Employee emp=employeeRepository.save(employee);

        authService.createUserForEmployee(emp);

        EmployeeResponseDTO response =  employeeMapper.toResponseDTO(emp);

        return response;
    }


    @Override
    public EmployeeResponseDTO getEmployeeById(Long employeeId) {
        Employee employee=employeeRepository.findById(employeeId)
                        .orElseThrow(()->new EmployeeNotFoundException("Employeee not present with id : "+employeeId));
        
        return employeeMapper.toResponseDTO(employee);
    }


    @Override
    public Page<EmployeeResponseDTO> getAllEmployees(int page, int size) {
        Pageable pageable=PageRequest.of(page, size);
        Page<Employee> pageEmp = employeeRepository.findAll(pageable);
        return pageEmp.map(emp->employeeMapper.toResponseDTO(emp));

    }


    @Override
    public EmployeeResponseDTO updateEmployee(Long employeeId, EmployeeUpdateRequestDTO request) {
        
        Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(()->new EmployeeNotFoundException("Employee not present with id : "+employeeId));

        Department department=departmentRepository.findById(request.getDepartmentId())
                            .orElseThrow(()->new DepartmentNotFoundException("Department not found for id : "+request.getDepartmentId()));

        if(!employee.getEmail().equals(request.getEmail()) && employeeRepository.existsByEmail(request.getEmail())){
            throw new EmployeeAlreadyExistsException(
                "Email already exists");
        }                   

        employeeMapper.updateEntity(employee, request);


        employee.setDepartment(department);

        employee.setUpdatedAt(LocalDateTime.now());

        Employee updatedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponseDTO(updatedEmployee);


    }


    @Override
    public void deleteEmployee(Long employeeId) {
        
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() ->
                    new EmployeeNotFoundException(
                            "Employee not found with id : " + employeeId));

        employeeRepository.delete(employee);

    }


    @Override
    public void assignProjectsToEmployee(Long employeeId, Long projectId) {

        Employee employee=employeeRepository.findById(employeeId)
                                .orElseThrow(()->new EmployeeNotFoundException("Employee not found with id : "+employeeId));

        Project project=projectRepository.findById(projectId)
                                .orElseThrow(()->new ProjectNotFoundException("Project not found with id : "+projectId));

        boolean alreadyAssigned=false;
        if(employee.getProjects()!=null){
            alreadyAssigned=employee.getProjects().stream().anyMatch(p->p.getProjectId().equals(projectId));
        }
        if(alreadyAssigned){
            throw new BusinessValidationException("Employee is already assigned to this project");
        }

        employee.getProjects().add(project);

        employeeRepository.save(employee);
        
    }


    @Override
    public void removeProjectFromEmployee(Long employeeId, Long projectId) {
        
        Employee employee=employeeRepository.findById(employeeId)
                                .orElseThrow(()->new EmployeeNotFoundException("Employee not found with id : "+employeeId));

        Project project=projectRepository.findById(projectId)
                                .orElseThrow(()->new ProjectNotFoundException("Project not found with id : "+projectId));

        boolean isAssigned=employee.getProjects().stream().anyMatch(p->p.getProjectId().equals(projectId));

        if(!isAssigned){
            throw new BusinessValidationException("Project is not assigned to this Employee");
        }

        employee.getProjects().removeIf(p->p.getProjectId().equals(projectId));

        employeeRepository.save(employee);

    }


    @Override
    public List<ProjectResponseDTO> getProjectsByEmployee(Long employeeId) {
        Employee employee=employeeRepository.findById(employeeId)
                        .orElseThrow(()->new EmployeeNotFoundException("Employee not found for id : "+employeeId));

        return employee.getProjects().stream().map(projectMapper::toResponseDTO).toList();
    }


    


}

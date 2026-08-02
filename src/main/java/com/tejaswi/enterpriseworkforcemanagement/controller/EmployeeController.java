package com.tejaswi.enterpriseworkforcemanagement.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tejaswi.enterpriseworkforcemanagement.dto.EmployeeRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.EmployeeResponseDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.EmployeeUpdateRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.ProjectResponseDTO;
import com.tejaswi.enterpriseworkforcemanagement.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequest){
        EmployeeResponseDTO empResponse = employeeService.createEmployee(employeeRequest);
        return new ResponseEntity<>(empResponse,HttpStatus.CREATED);

    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long employeeId){
        EmployeeResponseDTO empResponse = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(empResponse);

    } 

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> getAllEmployees(@RequestParam int page, @RequestParam int size){
        Page<EmployeeResponseDTO> empPageResp = employeeService.getAllEmployees(page, size);
        return ResponseEntity.ok(empPageResp);

    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long employeeId , @Valid @RequestBody EmployeeUpdateRequestDTO request){

        EmployeeResponseDTO updatedEmpResp = employeeService.updateEmployee(employeeId, request);
        return ResponseEntity.ok(updatedEmpResp);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployee(Long employeeId){

        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee Deleted Successfully");

    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping("/{employeeId}/projects/{projectId}")
    public ResponseEntity<String> assignProjectsToEmployee(@PathVariable Long employeeId,@PathVariable Long projectId){
        employeeService.assignProjectsToEmployee(employeeId, projectId);
        return ResponseEntity.ok("Project Assigned Successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{employeeId}/projects/{projectId}")
    public ResponseEntity<String> removeProjectFromEmployee(@PathVariable Long employeeId,@PathVariable Long projectId){
        employeeService.removeProjectFromEmployee(employeeId, projectId);
        return ResponseEntity.ok("Project Removed Successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @GetMapping("/{employeeId}/projects")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByEmployee(@PathVariable Long employeeId){
        
        List<ProjectResponseDTO> employeeProject = employeeService.getProjectsByEmployee(employeeId);
        return ResponseEntity.ok(employeeProject);

    }

}

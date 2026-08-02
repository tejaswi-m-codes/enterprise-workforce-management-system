package com.tejaswi.enterpriseworkforcemanagement.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejaswi.enterpriseworkforcemanagement.dto.DepartmentRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.DepartmentResponseDTO;
import com.tejaswi.enterpriseworkforcemanagement.service.DepartmentService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService){
        this.departmentService=departmentService;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Valid @RequestBody DepartmentRequestDTO department){
        
        DepartmentResponseDTO returnedDepartment=departmentService.createDepartment(department);
        return new ResponseEntity<>(returnedDepartment,HttpStatus.CREATED);

    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long departmentId){
        DepartmentResponseDTO getDeptById=departmentService.getDepartmentById(departmentId);
        return ResponseEntity.ok(getDeptById);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @GetMapping
    public ResponseEntity<Page<DepartmentResponseDTO>> getAllDepartments(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Page<DepartmentResponseDTO> departments=departmentService.getAllDepartments(page,size);
        return ResponseEntity.ok(departments);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(@PathVariable Long departmentId, @Valid @RequestBody DepartmentRequestDTO department){
        DepartmentResponseDTO updatedDepartment=departmentService.updateDepartment(departmentId, department);
        return new ResponseEntity<>(updatedDepartment,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<String> deleteDepartment(@PathVariable long departmentId){
        departmentService.deleteDepartment(departmentId);
        return new ResponseEntity<>("Department deleted successfully",HttpStatus.OK);
        
    }
    

}

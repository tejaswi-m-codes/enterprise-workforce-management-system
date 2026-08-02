package com.tejaswi.enterpriseworkforcemanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tejaswi.enterpriseworkforcemanagement.dto.DepartmentRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.DepartmentResponseDTO;
import com.tejaswi.enterpriseworkforcemanagement.entity.Department;
import com.tejaswi.enterpriseworkforcemanagement.exception.DepartmentAlreadyExistsException;
import com.tejaswi.enterpriseworkforcemanagement.exception.DepartmentNotFoundException;
import com.tejaswi.enterpriseworkforcemanagement.mapper.DepartmentMapper;
import com.tejaswi.enterpriseworkforcemanagement.repository.DepartmentRepository;
import com.tejaswi.enterpriseworkforcemanagement.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository,DepartmentMapper departmentMapper){
        this.departmentRepository=departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentReq) {

        Department department=departmentMapper.toEntity(departmentReq);

        Optional<Department> getDept=departmentRepository.findByDepartmentName(department.getDepartmentName());
        if(getDept.isPresent()){
            throw new DepartmentAlreadyExistsException("department already exists");
        }
       Department savedDepartment=departmentRepository.save(department);
       return departmentMapper.toResponseDTO(savedDepartment);
    }

    @Override
    public DepartmentResponseDTO getDepartmentById(Long departmentId) {
        Department d=departmentRepository.findById(departmentId)
                .orElseThrow(()->new DepartmentNotFoundException("Department not found with id : "+departmentId));

        return departmentMapper.toResponseDTO(d);
    }

    @Override
    public Page<DepartmentResponseDTO> getAllDepartments(int page,int size) {
        Pageable pageable=PageRequest.of(page, size);
        Page<Department> pageDept= departmentRepository.findAll(pageable);
        return pageDept.map(departmentMapper::toResponseDTO);

        //Page.map() is very elegant way to convert every entity in a page into dto
    }

    @Override
    public DepartmentResponseDTO updateDepartment(Long departmentId, DepartmentRequestDTO request) {
        
       Department existingDepartment= departmentRepository.findById(departmentId)
                .orElseThrow(()->new DepartmentNotFoundException("Department not found with id : "+departmentId));

        existingDepartment.setDepartmentName(request.getDepartmentName());
        existingDepartment.setLocation(request.getLocation());
        existingDepartment.setBudget(request.getBudget());
        existingDepartment.setDepartmentCode(request.getDepartmentCode());
        Department department=departmentRepository.save(existingDepartment);
        return departmentMapper.toResponseDTO(department);
    }

    @Override
    public void deleteDepartment(Long departmentId) {

        Department department= departmentRepository.findById(departmentId)
                .orElseThrow(()->new DepartmentNotFoundException("Department not found with id : "+departmentId));

        departmentRepository.delete(department);
        
    }

}

package com.tejaswi.enterpriseworkforcemanagement.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tejaswi.enterpriseworkforcemanagement.dto.ProjectRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.ProjectResponseDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.ProjectUpdateRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.entity.Project;
import com.tejaswi.enterpriseworkforcemanagement.exception.BusinessValidationException;
import com.tejaswi.enterpriseworkforcemanagement.exception.ProjectAlreadyExistsException;
import com.tejaswi.enterpriseworkforcemanagement.exception.ProjectNotFoundException;
import com.tejaswi.enterpriseworkforcemanagement.mapper.ProjectMapper;
import com.tejaswi.enterpriseworkforcemanagement.repository.ProjectRepository;
import com.tejaswi.enterpriseworkforcemanagement.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public ProjectResponseDTO createProject(ProjectRequestDTO request) {

        boolean exists= projectRepository.existsByProjectCode(request.getProjectCode());
        if(exists){
            throw new ProjectAlreadyExistsException("Project already exists ");
        }

        if(request.getEndDate().isBefore(request.getStartDate())){
            throw new BusinessValidationException("End date cannot be before Start date");
        }
        
        Project project=projectMapper.toEntity(request);

        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        Project projEntity=projectRepository.save(project);
        return projectMapper.toResponseDTO(projEntity);

    }

    @Override
    public ProjectResponseDTO getProjectByID(Long projectId) {
        
       Project project = projectRepository.findById(projectId).
                    orElseThrow(()->new ProjectNotFoundException("No project Found with id : "+projectId));

       return projectMapper.toResponseDTO(project);

    }

    @Override
    public Page<ProjectResponseDTO> getAllProjects(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Project> project  =projectRepository.findAll(pageable);
        return project.map(p->projectMapper.toResponseDTO(p));

    }

    @Override
    public ProjectResponseDTO updateProject(Long projectId, ProjectUpdateRequestDTO request) {
        
        Project project = projectRepository.findById(projectId).
                    orElseThrow(()->new ProjectNotFoundException("No project Found with id : "+projectId));

        if(request.getEndDate().isBefore(project.getStartDate())){
            throw new BusinessValidationException("End date cannot be before Start date");
        }

       project.setDescription(request.getDescription());
       project.setBudget(request.getBudget());
       project.setProjectStatus(request.getStatus());
       project.setEndDate(request.getEndDate());
       project.setUpdatedAt(LocalDateTime.now());


       Project updatedProj = projectRepository.save(project);
       return projectMapper.toResponseDTO(updatedProj);

    }

    
    @Override
    public void deleteProject(Long projectId) {

        Project project = projectRepository.findById(projectId).
                    orElseThrow(()->new ProjectNotFoundException("No project Found with id : "+projectId));

        projectRepository.delete(project);

    }
    


}

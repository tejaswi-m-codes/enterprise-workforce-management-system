package com.tejaswi.enterpriseworkforcemanagement.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.tejaswi.enterpriseworkforcemanagement.dto.ProjectRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.ProjectResponseDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.ProjectUpdateRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.service.ProjectService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTO requestDTO){
        ProjectResponseDTO response=projectService.createProject(requestDTO);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectsByID(@PathVariable Long projectId){

        ProjectResponseDTO response=projectService.getProjectByID(projectId);
        return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @GetMapping
    public ResponseEntity<Page<ProjectResponseDTO>> getAllProjects(@RequestParam int page,@RequestParam int size){
        Page<ProjectResponseDTO> respDTO=projectService.getAllProjects(page, size);
        return ResponseEntity.ok(respDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long projectid, @Valid @RequestBody ProjectUpdateRequestDTO request){
       ProjectResponseDTO updatedProjResp= projectService.updateProject(projectid, request);
       return ResponseEntity.ok(updatedProjResp);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> deleteproject(@PathVariable Long projectId){
        projectService.deleteProject(projectId);
        return ResponseEntity.ok("Project deleted Successfully");
    }
    

}

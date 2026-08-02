package com.tejaswi.enterpriseworkforcemanagement.service;

import org.springframework.data.domain.Page;

import com.tejaswi.enterpriseworkforcemanagement.dto.ProjectRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.ProjectResponseDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.ProjectUpdateRequestDTO;

public interface ProjectService {

    ProjectResponseDTO createProject(ProjectRequestDTO request);

    ProjectResponseDTO getProjectByID(Long projectId);

    Page<ProjectResponseDTO> getAllProjects(int page,int size);

    ProjectResponseDTO updateProject(Long projectId,ProjectUpdateRequestDTO request);

    void deleteProject(Long projectId);

}

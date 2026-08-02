package com.tejaswi.enterpriseworkforcemanagement.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.tejaswi.enterpriseworkforcemanagement.dto.ProjectRequestDTO;
import com.tejaswi.enterpriseworkforcemanagement.dto.ProjectResponseDTO;
import com.tejaswi.enterpriseworkforcemanagement.entity.Project;

@Component
public class ProjectMapper {

    public Project toEntity(ProjectRequestDTO request){

        if(request==null){
            return null;
        }
        return Project.builder()
               .projectCode(request.getProjectCode())
               .projectName(request.getProjectName())
               .description(request.getDescription())
               .startDate(request.getStartDate())
               .endDate(request.getEndDate())
               .budget(request.getBudget())
               .projectStatus(request.getStatus())
               .build();

        
    }

    public ProjectResponseDTO toResponseDTO(Project project){

        if(project==null){
            return null;
        }
        return ProjectResponseDTO.builder()
                .projectId(project.getProjectId())
                .projectCode(project.getProjectCode())
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .budget(project.getBudget())
                .status(project.getProjectStatus())
                .build();
    }

}

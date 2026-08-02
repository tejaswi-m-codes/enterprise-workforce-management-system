package com.tejaswi.enterpriseworkforcemanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponseDTO {

    private Long projectId;

    private String projectCode;

    private String projectName;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal budget;

    private String status;

    
}

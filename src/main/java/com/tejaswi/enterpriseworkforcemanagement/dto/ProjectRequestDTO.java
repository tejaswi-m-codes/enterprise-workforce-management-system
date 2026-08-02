package com.tejaswi.enterpriseworkforcemanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class ProjectRequestDTO {

    @NotBlank(message="Project code is required")
    @Size(min=3,max=20)
    private String projectCode;

    @NotBlank(message="Project name is required")
    @Size(min=3,max=100)
    private String projectName;

    @NotBlank(message="Description is required")
    @Size(min=10,max=500)
    private String description;

    @NotNull(message="Start date is required")
    private LocalDate startDate;

    @NotNull(message="End date is required")
    private LocalDate endDate;

    @PositiveOrZero(message="Budget cannot be negative")
    private BigDecimal budget;

    @NotBlank(message="Project status is required")
    private String status;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}

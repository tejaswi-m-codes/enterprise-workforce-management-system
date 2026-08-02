package com.tejaswi.enterpriseworkforcemanagement.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

public class DepartmentRequestDTO {

    private String departmentCode;

    @NotBlank(message="Department Name is required")
    private String departmentName;

    @NotBlank(message="Location is required")
    private String location;

    private BigDecimal budget;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    

}

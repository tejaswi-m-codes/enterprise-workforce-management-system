package com.tejaswi.enterpriseworkforcemanagement.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class EmployeeProjectId implements Serializable{

    private static final long serialVersionUID = 1L;

    @Column(name="employee_id")
    private Long employeeId;

    @Column(name="project_id")
    private Long projectId;

}

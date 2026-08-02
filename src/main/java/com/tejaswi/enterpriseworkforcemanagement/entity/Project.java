package com.tejaswi.enterpriseworkforcemanagement.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
@Entity
@Table(name="projects")
public class Project {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="project_id")
    private Long projectId;

    @Column(name="project_code",nullable=false,unique=true,length=20)
    private String projectCode;

    @Column(name="project_name",nullable=false,length=100)
    private String projectName;

    @Column(name="description")
    private String description;

    @Column(name="start_date",nullable=false)
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @Column(name="budget",precision=15,scale=2)
    private BigDecimal budget;

    @Column(name="project_status",nullable=false,length=20)
    private String projectStatus;

    @Column(name="created_at",nullable=false,updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at",nullable=false)
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "projects")
    private List<Employee> employees;
   

}

package com.tejaswi.enterpriseworkforcemanagement.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name="departments")
public class Department {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="department_id")
    private Long departmentId;

    @Column(name="department_code",nullable=false,unique=true,length=20)
    private String departmentCode;

    @Column(name="department_name",nullable=false,length=100)
    private String departmentName;

    @Column(name="location",length=100)
    private String location;

    @Column(name="budget",precision=15,scale=2)
    private BigDecimal budget;

    @Column(name="created_at",nullable=false,updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at",nullable=false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;



}

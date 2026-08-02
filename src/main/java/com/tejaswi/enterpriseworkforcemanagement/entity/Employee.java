package com.tejaswi.enterpriseworkforcemanagement.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name="employees")
public class Employee {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="employee_id")
    private Long employeeId;

    @Column(name="employee_code",nullable=false,unique=true,length=20)
    private String employeeCode;

    @Column(name="first_name",nullable=false,length=100)
    private String firstName;

    @Column(name="last_name",nullable=false,length=100)
    private String lastName;

    @Column(name="email",nullable=false,unique=true,length=100)
    private String email;

    @Column(name="phone_number",length=15)
    private String phoneNumber;

    @Column(name="date_of_birth")
    private LocalDate dateofBirth;

    @Column(name="joining_date",nullable=false)
    private LocalDate joiningDate;

    @Column(name="salary",precision = 15,scale=2,nullable=false)
    private BigDecimal salary;

    @Column(name="employment_status",nullable=false,length=20)
    private String employmentStatus;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="department_id",nullable=false)
    private Department department;

    @Column(name="created_at",nullable=false,updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at",nullable=false)
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
        name="employee_projects",
        joinColumns = @JoinColumn(name="employee_id"),
        inverseJoinColumns = @JoinColumn(name="project_id")
    )
    private List<Project> projects;

    @OneToOne(mappedBy = "employee")
    private User user;

}

package com.tejaswi.enterpriseworkforcemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tejaswi.enterpriseworkforcemanagement.entity.Project;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project,Long>{

    Optional<Project> findByProjectCode(String projectCode);

    boolean existsByProjectCode(String projectCode);

}

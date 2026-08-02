package com.tejaswi.enterpriseworkforcemanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tejaswi.enterpriseworkforcemanagement.entity.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User,Long>{
    /* 
    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.roles
            WHERE u.email=:email
            """)
    */
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}

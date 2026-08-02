package com.tejaswi.enterpriseworkforcemanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tejaswi.enterpriseworkforcemanagement.entity.RefreshToken;
import com.tejaswi.enterpriseworkforcemanagement.entity.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUser(User user);

}

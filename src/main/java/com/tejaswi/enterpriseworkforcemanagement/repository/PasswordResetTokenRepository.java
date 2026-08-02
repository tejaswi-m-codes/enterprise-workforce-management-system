package com.tejaswi.enterpriseworkforcemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tejaswi.enterpriseworkforcemanagement.entity.PasswordResetToken;
import com.tejaswi.enterpriseworkforcemanagement.entity.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long>{

    Optional<PasswordResetToken> findByToken(String token);

    List<PasswordResetToken> findByUser(User user);

}

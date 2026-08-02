package com.tejaswi.enterpriseworkforcemanagement.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tejaswi.enterpriseworkforcemanagement.entity.User;
import com.tejaswi.enterpriseworkforcemanagement.exception.AccountLockedException;
import com.tejaswi.enterpriseworkforcemanagement.repository.UserRepository;
import com.tejaswi.enterpriseworkforcemanagement.service.LoginAttemptService;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final UserRepository userRepository;

    public LoginAttemptServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseFailedAttempts(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int attempts = user.getFailedAttempts() + 1;

        user.setFailedAttempts(attempts);

        if (attempts >= 3) {
            user.setAccountLocked(true);
            user.setLockTime(LocalDateTime.now());
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetFailedAttempts(User user) {

        user.setFailedAttempts(0);
        user.setAccountLocked(false);
        user.setLockTime(null);

        // Optional because 'user' is already a managed entity,
        // but keeping save() makes the intent explicit.
        userRepository.save(user);
    }

    @Override
    public void validateAccountLock(User user) {

        if (!user.isAccountLocked()) {
            return;
        }

        // Lock expired
        if (user.getLockTime()
                .plusMinutes(15)
                .isBefore(LocalDateTime.now())) {

            resetFailedAttempts(user);
            return;
        }

        throw new AccountLockedException(
                "Account is locked. Please try again after 15 minutes.");
    }
}
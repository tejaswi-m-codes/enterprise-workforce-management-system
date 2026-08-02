package com.tejaswi.enterpriseworkforcemanagement.service;

import com.tejaswi.enterpriseworkforcemanagement.entity.User;

public interface LoginAttemptService {

    void increaseFailedAttempts(Long userId);

    void resetFailedAttempts(User user);

    void validateAccountLock(User user);

}

package com.tejaswi.enterpriseworkforcemanagement.service;

public interface EmailService {

    void sendTemporaryPasswordEmail(
            String email,
            String username,
            String temporaryPassword
    );

    void sendPasswordResetEmail(
            String email,
            String resetLink
    );
}

package com.tejaswi.enterpriseworkforcemanagement.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.tejaswi.enterpriseworkforcemanagement.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;


    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    public void sendTemporaryPasswordEmail(
            String email,
            String username,
            String temporaryPassword) {


        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Your Workforce Management Account Created");

        message.setText(
                "Hello " + username + ",\n\n"
                + "Your account has been created.\n\n"
                + "Temporary Password: "
                + temporaryPassword
                + "\n\n"
                + "Please login and change your password immediately."
        );


        mailSender.send(message);
    }


    @Override
    public void sendPasswordResetEmail(
            String email,
            String token) {


        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Password Reset Request");

        message.setText(
            "Hello,\n\n"
            + "We received a request to reset your password.\n\n"
            + "Use the following reset token:\n\n"
            + token
            + "\n\n"
            + "This token will expire in 15 minutes.\n\n"
            + "Use this token while calling the Reset Password API.\n\n"
            + "If you did not request a password reset, please ignore this email."
        );


        mailSender.send(message);
    }
}
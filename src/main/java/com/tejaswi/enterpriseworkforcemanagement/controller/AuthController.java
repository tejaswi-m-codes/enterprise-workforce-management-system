package com.tejaswi.enterpriseworkforcemanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejaswi.enterpriseworkforcemanagement.dto.AuthenticationResponse;
import com.tejaswi.enterpriseworkforcemanagement.dto.ChangePasswordRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.ForgotPasswordRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.LoginRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.LogoutRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.RefreshTokenRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.ResetPasswordRequest;
import com.tejaswi.enterpriseworkforcemanagement.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /* 
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request) {

        authService.register(request);

        return ResponseEntity.ok("User registered successfully");
    }
    */

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginRequest request) {

        System.out.println("Inside AuthController");

        AuthenticationResponse resp=authService.login(request);

        return ResponseEntity.ok(resp);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request){
        authService.changePassword(request);
        return ResponseEntity.ok("Password changed successfully");

    }

    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@Valid @RequestBody ForgotPasswordRequest request){
        
        authService.forgotPassword(request);
        return ResponseEntity.ok("If email exists, a password reset link has been generated");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        
        authService.resetPassword(request);
        return ResponseEntity.ok("Password reset Successfully");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
        @Valid @RequestBody RefreshTokenRequest request){

        AuthenticationResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequest request){

        authService.logout(request);
        return ResponseEntity.ok("Logged out successfully");
    }

}

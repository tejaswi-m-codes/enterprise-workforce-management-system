package com.tejaswi.enterpriseworkforcemanagement.service;

import com.tejaswi.enterpriseworkforcemanagement.dto.AuthenticationResponse;
import com.tejaswi.enterpriseworkforcemanagement.dto.ChangePasswordRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.ForgotPasswordRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.LoginRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.LogoutRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.RefreshTokenRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.ResetPasswordRequest;
import com.tejaswi.enterpriseworkforcemanagement.entity.Employee;

public interface AuthService {

    //void register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

    void createUserForEmployee(Employee employee);

    void changePassword(ChangePasswordRequest request);

    void forgotPassword(ForgotPasswordRequest request);
    
    void resetPassword(ResetPasswordRequest request);

    AuthenticationResponse refreshToken(RefreshTokenRequest request);

    void logout(LogoutRequest request);

}

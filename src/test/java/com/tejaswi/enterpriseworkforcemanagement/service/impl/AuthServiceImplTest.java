package com.tejaswi.enterpriseworkforcemanagement.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tejaswi.enterpriseworkforcemanagement.dto.AuthenticationResponse;
import com.tejaswi.enterpriseworkforcemanagement.dto.LoginRequest;
import com.tejaswi.enterpriseworkforcemanagement.entity.RefreshToken;
import com.tejaswi.enterpriseworkforcemanagement.entity.User;
import com.tejaswi.enterpriseworkforcemanagement.repository.PasswordResetTokenRepository;
import com.tejaswi.enterpriseworkforcemanagement.repository.RefreshTokenRepository;
import com.tejaswi.enterpriseworkforcemanagement.repository.RoleRepository;
import com.tejaswi.enterpriseworkforcemanagement.repository.UserRepository;
import com.tejaswi.enterpriseworkforcemanagement.security.CustomUserDetails;
import com.tejaswi.enterpriseworkforcemanagement.security.JwtService;
import com.tejaswi.enterpriseworkforcemanagement.service.EmailService;
import com.tejaswi.enterpriseworkforcemanagement.service.LoginAttemptService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private LoginAttemptService loginAttemptService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_shouldReturnAuthenticationResponse_WhenCredentialsAreValid() {

        // Arrange

        LoginRequest request = new LoginRequest();
        request.setEmail("peterparker@company.com");
        request.setPassword("Temp@EMP1");

        User user = new User();
        user.setId(1L);
        user.setEmail("peterparker@company.com");
        user.setPassword("encodedPassword");
        user.setEnabled(true);

        Authentication authentication = mock(Authentication.class);

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        
        //mock repositories
        when(userRepository.findByEmail("peterparker@company.com")).thenReturn(Optional.of(user));

        //mock validate account lock
        doNothing()
            .when(loginAttemptService)
            .validateAccountLock(user);

        //mock authentication manager
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        //reset failed attempts
        doNothing()
            .when(loginAttemptService)
            .resetFailedAttempts(user);

        //generate token
        when(jwtService.generateToken(userDetails)).thenReturn("dummy-jwt-token");

        //mock refresh token save
        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation->invocation.getArgument(0));
        
        //Act
        AuthenticationResponse response=authService.login(request);

        //Assert
        assertNotNull(response);

        assertEquals("dummy-jwt-token", response.getAccessToken());

        //we are not comparing becuase refresh token is generated in business layer using random value logic - value chages everytime , so null check is fine
        assertNotNull(response.getRefreshToken());

        //verify

        verify(loginAttemptService).validateAccountLock(user);

        verify(loginAttemptService).resetFailedAttempts(user);

        verify(refreshTokenRepository).save(any(RefreshToken.class));



    }

}

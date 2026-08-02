package com.tejaswi.enterpriseworkforcemanagement.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.security.auth.login.AccountLockedException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tejaswi.enterpriseworkforcemanagement.dto.AuthenticationResponse;
import com.tejaswi.enterpriseworkforcemanagement.dto.ChangePasswordRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.ForgotPasswordRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.LoginRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.LogoutRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.RefreshTokenRequest;
import com.tejaswi.enterpriseworkforcemanagement.dto.ResetPasswordRequest;
import com.tejaswi.enterpriseworkforcemanagement.entity.Employee;
import com.tejaswi.enterpriseworkforcemanagement.entity.PasswordResetToken;
import com.tejaswi.enterpriseworkforcemanagement.entity.RefreshToken;
import com.tejaswi.enterpriseworkforcemanagement.entity.Role;
import com.tejaswi.enterpriseworkforcemanagement.entity.RoleType;
import com.tejaswi.enterpriseworkforcemanagement.entity.User;
import com.tejaswi.enterpriseworkforcemanagement.exception.BusinessValidationException;
import com.tejaswi.enterpriseworkforcemanagement.repository.PasswordResetTokenRepository;
import com.tejaswi.enterpriseworkforcemanagement.repository.RefreshTokenRepository;
import com.tejaswi.enterpriseworkforcemanagement.repository.RoleRepository;
import com.tejaswi.enterpriseworkforcemanagement.repository.UserRepository;
import com.tejaswi.enterpriseworkforcemanagement.security.CustomUserDetails;
import com.tejaswi.enterpriseworkforcemanagement.security.JwtService;
import com.tejaswi.enterpriseworkforcemanagement.service.AuthService;
import com.tejaswi.enterpriseworkforcemanagement.service.EmailService;
import com.tejaswi.enterpriseworkforcemanagement.service.LoginAttemptService;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final EmailService emailService;

    private final LoginAttemptService loginAttemptService;
    


    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService,
                        PasswordResetTokenRepository passwordResetTokenRepository,
                        RefreshTokenRepository refreshTokenRepository,
                    EmailService emailService,
                LoginAttemptService loginAttemptService) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService=jwtService;
        this.passwordResetTokenRepository=passwordResetTokenRepository;
        this.refreshTokenRepository=refreshTokenRepository;
        this.emailService=emailService;
        this.loginAttemptService=loginAttemptService;
    }

    /* 

    @Override
    public void register(RegisterRequest request) {
       if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
       }

       Employee emp=employeeRepository.findById(request.getEmployeeId())
                            .orElseThrow(()->new RuntimeException("Employee Not Found"));

        if(emp.getUser()!=null){
            throw new RuntimeException("Employee Already Registered");
        }
        
       Role role=roleRepository.findByName(RoleType.EMPLOYEE).orElseThrow(()->new RuntimeException("Role Not Found"));

       User user=new User();
       user.setUsername(request.getUsername());
       user.setEmail(request.getEmail());
       user.setPassword(passwordEncoder.encode(request.getPassword()));
       user.setEnabled(true);

       user.setRoles(Set.of(role));

       user.setEmployee(emp);
       emp.setUser(user);
       
       userRepository.save(user);
    }
    */

    /*
    
    Login Request
      │
      ▼
Find User
      │
      ▼
Validate Account Lock
      │
      ▼
Authenticate
      │
      ├── Success
      │      │
      │      ├── Reset Failed Attempts
      │      ├── Generate JWT
      │      ├── Generate Refresh Token
      │      ├── Save Refresh Token
      │      └── Return Tokens
      │
      └── Failure
             │
             ├── Increase Failed Attempts
             ├── Lock Account (after 3 attempts)
             └── Throw BadCredentialsException
    
    
    */

   @Override
    @Transactional
public AuthenticationResponse login(LoginRequest request) {

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new RuntimeException("Invalid email or password"));

    // Check if account is locked
    loginAttemptService.validateAccountLock(user);


    try {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()));


        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        // Login successful
        loginAttemptService.resetFailedAttempts(user);

        String accessToken = jwtService.generateToken(userDetails);

        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setExpiryTime(LocalDateTime.now().plusDays(7));
        refreshToken.setRevoked(false);
        refreshToken.setUser(user);

        refreshTokenRepository.save(refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .build();

    } catch (BadCredentialsException ex) {

        loginAttemptService.increaseFailedAttempts(user.getId());

        throw new BadCredentialsException("Invalid email or password");
    }
    }

    @Override
    public void createUserForEmployee(Employee employee){

        /* 
        String tempPassword = UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8);
        */

        String tempPassword="Temp@" + employee.getEmployeeCode();

        Role employeeRole=roleRepository.findByName(RoleType.EMPLOYEE)
                            .orElseThrow(()->new RuntimeException("Role Not Found"));
        
        User user=new User();
        user.setUsername(employee.getEmployeeCode());
        user.setEmail(employee.getEmail());
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setEnabled(true);
        user.setPasswordChanged(false);
        user.setRoles(Set.of(employeeRole));

        user.setEmployee(employee);
        employee.setUser(user);

        userRepository.save(user);

        emailService.sendTemporaryPasswordEmail(
        user.getEmail(),
        user.getUsername(),
        tempPassword);

    }


    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    CustomUserDetails userDetails =
            (CustomUserDetails) authentication.getPrincipal();

    String email = userDetails.getUsername();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // Verify current password
    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
        throw new BusinessValidationException("Current password is incorrect");
    }

    // Prevent using the same password again
    if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
        throw new BusinessValidationException(
                "New password cannot be the same as the current password");
    }

    // Update password
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    user.setPasswordChanged(true);

    userRepository.save(user);
    }

    public void forgotPassword(ForgotPasswordRequest request){

        Optional<User> findUserWithEmailId= userRepository.findByEmail(request.getEmail());

        // Prevent email enumeration attacks
        if(findUserWithEmailId.isEmpty()){
            return;
        }
        User user=findUserWithEmailId.get();

        // Invalidate previous unused tokens
       List<PasswordResetToken> tokens= passwordResetTokenRepository.findByUser(user);

       for(PasswordResetToken token:tokens){
            if(!token.isUsed()){
                token.setUsed(true);
            }
       }
       passwordResetTokenRepository.saveAll(tokens);

       // Generate new token
       String token = UUID.randomUUID().toString();

       PasswordResetToken resetToken=new PasswordResetToken();

       resetToken.setToken(token);
       resetToken.setExpiryTime(LocalDateTime.now().plusMinutes(15));
       resetToken.setUsed(false);
       resetToken.setUser(user);

       passwordResetTokenRepository.save(resetToken);

        // Send the token via email
        emailService.sendPasswordResetEmail(user.getEmail(), token);

        
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                                            .orElseThrow(()->new RuntimeException("Invalid reset Token"));
        
        if(resetToken.isUsed()){
            throw new RuntimeException("reset token Already used");
        }

        if(resetToken.getExpiryTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Reset token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordChanged(true);

        userRepository.save(user);
        resetToken.setUsed(true);

        passwordResetTokenRepository.save(resetToken);

    }

    @Override
    @Transactional
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {

        RefreshToken refreshToken =refreshTokenRepository.findByToken(request.getRefreshToken())
                                     .orElseThrow(() ->new RuntimeException("Invalid refresh token"));

        if(refreshToken.isRevoked()){
            throw new RuntimeException("Refresh token revoked");
        }

        if(refreshToken.getExpiryTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Refresh token expired");
        }

        User user = refreshToken.getUser();

        CustomUserDetails userDetails = new CustomUserDetails(user);

        String accessToken = jwtService.generateToken(userDetails);

        String newRefreshToken = UUID.randomUUID().toString();

        //revoking old token as we generated new one
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        RefreshToken newToken = new RefreshToken();
        newToken.setToken(newRefreshToken);
        newToken.setExpiryTime(LocalDateTime.now().plusDays(7));
        newToken.setRevoked(false);
        newToken.setUser(user);

        refreshTokenRepository.save(newToken);

        return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(newRefreshToken)
            .build();


    }

    @Override
    public void logout(LogoutRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                                        .orElseThrow(() ->new RuntimeException("Invalid refresh token"));
        
        if(refreshToken.isRevoked()){
            return;
        }

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }


    

    private String createRefreshToken(User user){

    String value = UUID.randomUUID().toString();

    RefreshToken refreshToken = new RefreshToken();

    refreshToken.setToken(value);
    refreshToken.setExpiryTime(
            LocalDateTime.now().plusDays(7));

    refreshToken.setRevoked(false);

    refreshToken.setUser(user);

    refreshTokenRepository.save(refreshToken);

    return value;
    }

}

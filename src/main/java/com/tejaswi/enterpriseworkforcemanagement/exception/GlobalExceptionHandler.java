package com.tejaswi.enterpriseworkforcemanagement.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tejaswi.enterpriseworkforcemanagement.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DepartmentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleDepartmentAlreadyExistsException(DepartmentAlreadyExistsException ex,HttpServletRequest request){
        ErrorResponse errorResponse=new ErrorResponse(
            LocalDateTime.now(), HttpStatus.CONFLICT.value(), 
            HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
        
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(DepartmentNotFoundException ex,HttpServletRequest req){
        ErrorResponse errorResp=new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value()
        , HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), req.getRequestURI());

        return new ResponseEntity<>(errorResp,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex){
        Map<String,String> errors=new HashMap<>();
        for(FieldError error:ex.getBindingResult().getFieldErrors()){
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ErrorResponse errorResp=new ErrorResponse();
        errorResp.setTimestamp(LocalDateTime.now());
        errorResp.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResp.setMessage("Validation Failed");
        errorResp.setValidationErrors(errors);

        return new ResponseEntity<>(errorResp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProjectAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProjectExistsException(ProjectAlreadyExistsException ex,HttpServletRequest req){

        ErrorResponse errorResponse=new ErrorResponse(
            LocalDateTime.now(), HttpStatus.CONFLICT.value(), 
            HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), req.getRequestURI());
        
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
        
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProjectNotFoundException(ProjectNotFoundException ex,HttpServletRequest request){
        ErrorResponse errorResp=new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value()
        , HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(errorResp,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessValidationException(BusinessValidationException ex){
        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeAlreadyExistsException(EmployeeAlreadyExistsException ex,HttpServletRequest request){

        ErrorResponse errorResponse=new ErrorResponse(
            LocalDateTime.now(), HttpStatus.CONFLICT.value(), 
            HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
        
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);

    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFoundException(EmployeeNotFoundException ex,HttpServletRequest request){

        ErrorResponse errorResponse=new ErrorResponse(
            LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), 
            HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
        
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ErrorResponse> handAccountLocked(AccountLockedException ex,HttpServletRequest request){

        ErrorResponse errorResponse=new ErrorResponse(
            LocalDateTime.now(), HttpStatus.LOCKED.value(), 
            HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
        
        return new ResponseEntity<>(errorResponse,HttpStatus.LOCKED);

    }


}

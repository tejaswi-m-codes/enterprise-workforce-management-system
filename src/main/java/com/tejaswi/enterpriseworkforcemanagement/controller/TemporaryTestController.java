package com.tejaswi.enterpriseworkforcemanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejaswi.enterpriseworkforcemanagement.service.EmailService;

@RestController
@RequestMapping("/test")
public class TemporaryTestController {

    private final EmailService emailService;


    public TemporaryTestController(EmailService emailService){
        this.emailService=emailService;
    }


    @GetMapping("/mail")
    public String sendMail(){

        emailService.sendTemporaryPasswordEmail(
                "m.teju1005@gmail.com",
                "Tejaswi",
                "temp@123"
        );

        return "Mail sent";
    }

}

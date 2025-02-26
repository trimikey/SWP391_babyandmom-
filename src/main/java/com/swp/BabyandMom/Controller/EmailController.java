package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.Service.EmailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")

public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send")
    public String sendMail(@RequestParam String email, @RequestParam String subject, @RequestParam String text ) {
        emailService.sendEmail(email, subject, text);
        return "Email sent successfully to " + email;
    }

}

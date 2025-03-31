package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.*;
import com.swp.BabyandMom.Service.EmailService;
import com.swp.BabyandMom.Service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private final UserService userService;

    @Autowired
    private final EmailService emailService;

    public AuthController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.checkLogin(loginRequestDTO);


    }

    @PostMapping("/test")
    public ResponseEntity<LoginResponseDTO> test(@RequestBody LoginRequestDTO loginRequestDTO) {
        return null;


    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return userService.checkRegister(registerRequestDTO);
    }





}

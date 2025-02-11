package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.LoginRequestDTO;
import com.swp.BabyandMom.DTO.LoginResponseDTO;
import com.swp.BabyandMom.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.checkLogin(loginRequestDTO);
    }
}

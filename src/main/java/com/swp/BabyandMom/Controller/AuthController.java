package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.LoginRequestDTO;
import com.swp.BabyandMom.DTO.LoginResponseDTO;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.checkLogin(loginRequestDTO);
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userService.register(user);
    }


}

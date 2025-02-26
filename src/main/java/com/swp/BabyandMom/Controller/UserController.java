package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.ChangePasswordRequestDTO;
import com.swp.BabyandMom.DTO.ChangePasswordResponseDTO;
import com.swp.BabyandMom.Service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin("*")

public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping
    public ResponseEntity<ChangePasswordResponseDTO> changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequestDTO){
        return userService.changePassword(changePasswordRequestDTO);
    }

}

package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.ChangePasswordRequestDTO;
import com.swp.BabyandMom.DTO.ChangePasswordResponseDTO;
import com.swp.BabyandMom.DTO.ForgotPasswordRequestDTO;
import com.swp.BabyandMom.DTO.ResetPasswordRequestDTO;
import com.swp.BabyandMom.Service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
@CrossOrigin("*")

public class UserController {
    @Autowired
    private UserService userService;

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/change")
    public ResponseEntity<ChangePasswordResponseDTO> changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequestDTO){
        return userService.changePassword(changePasswordRequestDTO);
    }

    // api dùng trước khi login, không cần authenticate
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDTO requestDTO) {
        return userService.forgotPassword(requestDTO);
    }

    // api dùng trước khi login, không cần authenticate
    @PutMapping("/reset")
    public ResponseEntity<String> resetPassowrd(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO){
        return userService.resetPassword(resetPasswordRequestDTO);
    }
}

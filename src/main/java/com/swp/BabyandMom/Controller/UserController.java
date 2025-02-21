package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.ChangePasswordRequestDTO;
import com.swp.BabyandMom.DTO.ChangePasswordResponseDTO;
import com.swp.BabyandMom.DTO.GetProfileResponseDTO;
import com.swp.BabyandMom.DTO.UpdateProfileRequestDTO;
import com.swp.BabyandMom.DTO.UpdateProfileResponseDTO;
import com.swp.BabyandMom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "api")  
@CrossOrigin("*")

public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/profile")
    public ResponseEntity<GetProfileResponseDTO> getProfile() {
        return userService.getProfile();
    }

    @PutMapping("/update-profile")
    public UpdateProfileResponseDTO updateProfile(@RequestBody UpdateProfileRequestDTO updateRequestDTO) throws Exception {
        return userService.update(updateRequestDTO);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ChangePasswordResponseDTO> changePassword(@RequestBody ChangePasswordRequestDTO request) {
        return userService.changePassword(request);
    }
}

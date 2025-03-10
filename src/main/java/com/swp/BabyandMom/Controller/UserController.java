package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.*;
import com.swp.BabyandMom.Entity.Enum.UserStatusEnum;
import com.swp.BabyandMom.Service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin("*")

public class UserController {
    @Autowired
    private UserService userService;

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/profile")
    public ResponseEntity<GetProfileResponseDTO> getProfile() {
        return userService.getProfile();
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/update-profile")
    public UpdateProfileResponseDTO updateProfile(@RequestBody UpdateProfileRequestDTO updateRequestDTO) throws Exception {
        return userService.update(updateRequestDTO);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/change")
    public ResponseEntity<ChangePasswordResponseDTO> changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequestDTO){
        return userService.changePassword(changePasswordRequestDTO);
    }

    // api dùng trước khi login, không cần authenticate
//    @PostMapping("/forgot")
//    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDTO requestDTO) {
//        return userService.forgotPassword(requestDTO);
//    }

    // api dùng trước khi login, không cần authenticate
//    @PutMapping("/reset")
//    public ResponseEntity<String> resetPassowrd(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO){
//        return userService.resetPassword(resetPasswordRequestDTO);
//    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllMembers() {
        return ResponseEntity.ok(userService.getAllMembers());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(userId, userDTO));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{userId}/status")
    public ResponseEntity<UserDTO> updateUserStatus(@PathVariable Long userId, @RequestBody UserDTO request) {
        return ResponseEntity.ok(userService.updateUserStatus(userId, request.getStatus()));
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> banUser(@PathVariable Long userId) {
        userService.banUser(userId);
        return ResponseEntity.ok("User has been banned successfully");
    }



}

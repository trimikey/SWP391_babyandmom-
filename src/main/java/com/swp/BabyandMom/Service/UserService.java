package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.*;
import com.swp.BabyandMom.Entity.Enum.RoleType;
import com.swp.BabyandMom.Entity.Enum.UserStatusEnum;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.ExceptionHandler.AuthAppException;
import com.swp.BabyandMom.ExceptionHandler.ErrorCode;
import com.swp.BabyandMom.ExceptionHandler.NotLoginException;
import com.swp.BabyandMom.Repository.UserRepository;
import com.swp.BabyandMom.Utils.UpdateUtils;
import com.swp.BabyandMom.Utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getAccountByEmail(String email) {
        Optional<User> account = userRepository.findByEmail(email);
        return account.orElse(null);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found")
        );
    }

    public ResponseEntity<LoginResponseDTO> checkLogin(LoginRequestDTO loginRequestDTO) {
        logger.info("Received login request for email: {}", loginRequestDTO.getEmail());

        try {
            User user = getAccountByEmail(loginRequestDTO.getEmail());

            if (user == null) {
                logger.warn("Login failed: Email not found - {}", loginRequestDTO.getEmail());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new LoginResponseDTO("Email not found", "Login failed", null, null)
                );
            }

            if (user.getStatus().equals(UserStatusEnum.UNVERIFIED)) {
                logger.warn("Login failed: Account not verified - {}", loginRequestDTO.getEmail());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        new LoginResponseDTO("Account not verified", "Login failed", null, null)
                );
            }

            if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
                logger.warn("Login failed: Incorrect password - {}", loginRequestDTO.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new LoginResponseDTO("Incorrect email or password", "Login failed", null, null)
                );
            }

            String accessToken = jwtService.generateToken(user.getEmail());
            String refreshToken = jwtService.generateRefreshToken(user.getEmail());

            logger.info("Login successful: {}", loginRequestDTO.getEmail());

            return ResponseEntity.ok(new LoginResponseDTO("Login successful", "Success", accessToken, refreshToken));

        } catch (AuthAppException e) {
            logger.error("Authentication error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new LoginResponseDTO(e.getMessage(), "Login failed", null, null)
            );
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new LoginResponseDTO("An unexpected error occurred", "Login failed", null, null)
            );
        }
    }

    public ResponseEntity<RegisterResponseDTO> checkRegister(RegisterRequestDTO registerRequestDTO) {
        try {
            // Kiểm tra email đã tồn tại chưa
            if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new RegisterResponseDTO((Long) null, (String) null, (String) null, (String) null, (String) null, "Email đã được sử dụng!"));
            }

            // Kiểm tra username đã tồn tại chưa
            if (userRepository.existsByUserName(registerRequestDTO.getUserName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new RegisterResponseDTO((Long) null, (String) null, (String) null, (String) null, (String) null, "Username đã tồn tại!"));
            }

            User newUser = new User();
            newUser.setFullName(registerRequestDTO.getName());
            newUser.setUserName(registerRequestDTO.getUserName());
            newUser.setPhoneNumber(registerRequestDTO.getPhoneNumber());
            newUser.setEmail(registerRequestDTO.getEmail());
            // Mã hóa password trước khi lưu
            newUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

            newUser.setRole(RoleType.MEMBER);
            newUser.setStatus(UserStatusEnum.VERIFIED);

            User savedUser = userRepository.save(newUser);

            RegisterResponseDTO responseDTO = new RegisterResponseDTO(
                    savedUser.getId(),
                    savedUser.getFullName(),
                    savedUser.getPassword(),
                    savedUser.getUserName(),
                    savedUser.getEmail(),
                    "Registered successfully"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RegisterResponseDTO((Long) null, null, null, null,null, "Lỗi server: " + e.getMessage()));
        }
    }


    public ResponseEntity<GetProfileResponseDTO> getProfile() {
        User account = userUtils.getCurrentAccount();

        GetProfileResponseDTO getProfileResponse = new GetProfileResponseDTO(
                account.getName(),
                account.getEmail(),
                account.getPhone(),
                account.getRole()
        );
        return ResponseEntity.ok(getProfileResponse);
    }

    public UpdateProfileResponseDTO update(UpdateProfileRequestDTO updateRequestDTO) throws Exception {

        User user = null;
        try {
            user = userUtils.getCurrentAccount();
        } catch (Exception ex) {
            throw new NotLoginException("Not Login");
        }

        user = UpdateUtils.updateAccount(updateRequestDTO, user);

        try {
            userRepository.save(user);
            return new UpdateProfileResponseDTO(
                    user.getName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getPassword());
        } catch (Exception ex) {
            throw new Exception("Can not update");
        }
    }

    public ResponseEntity<ChangePasswordResponseDTO> changePassword(ChangePasswordRequestDTO request) {
        logger.info("Processing change password request");
        
        try {
            User currentUser = userUtils.getCurrentAccount();
            if (currentUser == null) {
                logger.warn("Change password failed: User not logged in");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ChangePasswordResponseDTO("User not logged in", "Failed"));
            }

            // Verify old password using BCrypt
            if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
                logger.warn("Change password failed: Incorrect old password");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ChangePasswordResponseDTO("Incorrect old password", "Failed"));
            }

            // Validate new password
            if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
                logger.warn("Change password failed: New password cannot be empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ChangePasswordResponseDTO("New password cannot be empty", "Failed"));
            }

            // Validate confirm password
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                logger.warn("Change password failed: Passwords do not match");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ChangePasswordResponseDTO("New password and confirm password do not match", "Failed"));
            }

            // Update password with encryption
            currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(currentUser);

            logger.info("Password changed successfully for user: {}", currentUser.getEmail());
            return ResponseEntity.ok(new ChangePasswordResponseDTO("Password changed successfully", "Success"));

        } catch (Exception e) {
            logger.error("Unexpected error during password change: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ChangePasswordResponseDTO("An unexpected error occurred", "Failed"));
        }
    }

}

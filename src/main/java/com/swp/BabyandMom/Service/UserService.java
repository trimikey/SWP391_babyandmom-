package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.*;
import com.swp.BabyandMom.Entity.Enum.RoleType;
import com.swp.BabyandMom.Entity.Enum.UserStatusEnum;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.ExceptionHandler.AuthAppException;
import com.swp.BabyandMom.ExceptionHandler.NotLoginException;
import com.swp.BabyandMom.Repository.UserRepository;
import com.swp.BabyandMom.Utils.UpdateUtils;
import com.swp.BabyandMom.Utils.UserUtils;
import jakarta.servlet.http.HttpSession;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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
    @Autowired
    private EmailService emailService;

    private ForgotPasswordRequestDTO forgotPasswordRequestDTO;

    public User getAccountByEmail(String email) {
        Optional<User> account = userRepository.findByEmail(email);
        return account.orElse(null);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }


    public ResponseEntity<LoginResponseDTO> checkLogin(LoginRequestDTO loginRequestDTO) {
        logger.info("Received login request for email: {}", loginRequestDTO.getEmail());

        try {
            User user = getAccountByEmail(loginRequestDTO.getEmail());

            if (user == null) {
                logger.warn("Login failed: Email not found - {}", loginRequestDTO.getEmail());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new LoginResponseDTO("Email not found", "Login failed", null, null, null)
                );
            }

            if (user.getStatus().equals(UserStatusEnum.UNVERIFIED)) {
                logger.warn("Login failed: Account not verified - {}", loginRequestDTO.getEmail());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        new LoginResponseDTO("Account not verified", "Login failed", null, null, null)
                );
            }

            if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
                logger.warn("Login failed: Incorrect password - {}", loginRequestDTO.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new LoginResponseDTO("Incorrect email or password", "Login failed", null, null, null)
                );
            }

            String accessToken = jwtService.generateToken(user);


            // 🔥 Trả về Role chính xác
            RoleType role = user.getRole();

            logger.info("Login successful: {} - Role: {}", loginRequestDTO.getEmail(), role);

//            emailService.sendEmail(
//                    loginRequestDTO.getEmail(),
//                    "Login Notification",
//                    "Welcome " + user.getFullName() + " ! , you have successfully logged in!"
//            );

            return ResponseEntity.ok(new LoginResponseDTO("Login successful", "Success", accessToken, null, role));

        } catch (AuthAppException e) {
            logger.error("Authentication error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new LoginResponseDTO(e.getMessage(), "Login failed", null, null, null)
            );
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new LoginResponseDTO("An unexpected error occurred", "Login failed", null, null, null)
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

            // Tạo user mới
            User newUser = new User();
            newUser.setFullName(registerRequestDTO.getFullName());
            newUser.setUserName(registerRequestDTO.getUserName());
            newUser.setPhoneNumber(registerRequestDTO.getPhoneNumber());
            newUser.setEmail(registerRequestDTO.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
            newUser.setRole(RoleType.MEMBER);
            newUser.setStatus(UserStatusEnum.VERIFIED);

            User savedUser = userRepository.save(newUser);



            RegisterResponseDTO responseDTO = new RegisterResponseDTO(
                    savedUser.getId(),
                    savedUser.getName(),
                    savedUser.getUserName(),
                    savedUser.getPhoneNumber(),
                    savedUser.getEmail(),
                    "Registered successfully"
            );

//            emailService.sendEmail(
//                    registerRequestDTO.getEmail(),
//                    "Account Registration Confirmation",
//                    "Congratulation " + registerRequestDTO.getFullName()+ " ! , you have successfully registered an account !"
//            );

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            logger.error("Registration error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RegisterResponseDTO((Long) null, null, null, null, null, "Lỗi server: " + e.getMessage()));
        }
    }


    public ResponseEntity<GetProfileResponseDTO> getProfile() {
        User account = userUtils.getCurrentAccount();

        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        GetProfileResponseDTO response = new GetProfileResponseDTO(
                account.getFullName(),
                account.getUserName(),
                account.getEmail(),
                account.getPhoneNumber()
        );

        return ResponseEntity.ok(response);
    }



    public UpdateProfileResponseDTO update(UpdateProfileRequestDTO updateRequestDTO) throws Exception {
        User user;

        try {
            user = userUtils.getCurrentAccount();
        } catch (Exception ex) {
            throw new NotLoginException("Not Login");
        }

        user = UpdateUtils.updateAccount(updateRequestDTO, user);

        try {
            userRepository.save(user);

            return new UpdateProfileResponseDTO(
                    user.getFullName(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getPhoneNumber()
            );

        } catch (Exception ex) {
            throw new Exception("Cannot update profile");
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



    public ResponseEntity<String> resetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO, HttpSession session){

        String email = (String) session.getAttribute("forgotEmail");
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email does not exist !");
        if(!user.get().getResetCode().equals(resetPasswordRequestDTO.getCode()) || user.get().getResetCode()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reset code !");
        }
        if(user.get().getResetCodeExpiration().isBefore(LocalDateTime.now())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Reset code has expired !");
        }

        user.get().setPassword(passwordEncoder.encode(resetPasswordRequestDTO.getNewPassword()));
        user.get().setResetCode(null);
        user.get().setResetCodeExpiration(null);
        userRepository.save(user.get());

        emailService.sendEmail(
                user.get().getEmail(),
                "Password Reset Successfully",
                "Your password has been reset successfully. If you did not perform this action, please contact us immediately."
        );

        return ResponseEntity.ok("Password has been reset successfully !");
    }

    public ResponseEntity<String> forgotPassword(ForgotPasswordRequestDTO forgotPasswordRequestDTO, HttpSession session) {
        String email = forgotPasswordRequestDTO.getEmail();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email does not exist!");
        }
        String resetPasswordCode = generateCode();

        emailService.sendEmail(email, "Forgot Password Code", "Your reset code is: " + resetPasswordCode);


        session.setAttribute("forgotEmail", email);
        session.setAttribute("resetCode", resetPasswordCode);
        session.setAttribute("resetCodeExpiration", LocalDateTime.now().plusMinutes(10));

        // Update user reset code in database
        User user = userOptional.get();
        user.setResetCode(resetPasswordCode);
        user.setResetCodeExpiration(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        return ResponseEntity.ok("Reset code has been sent to your email!");
    }


    private String generateCode(){
        Random random = new Random();

        int code = random.nextInt(1000000);

        return String.valueOf(code);

    }

    private void checkAdminPermission() {
        User currentUser = userUtils.getCurrentAccount();
        if (currentUser == null || currentUser.getRole() != RoleType.ADMIN) {
            throw new RuntimeException("Access Denied: Only Admin can perform this action");
        }
    }

    public List<UserDTO> getAllMembers() {
        checkAdminPermission();
        return userRepository.findByRole(RoleType.MEMBER).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long userId) {
        checkAdminPermission();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        checkAdminPermission();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userDTO.getFullName() != null && !userDTO.getFullName().isEmpty()) {
            user.setFullName(userDTO.getFullName());
        }
        if (userDTO.getUserName() != null && !userDTO.getUserName().isEmpty()) {
            user.setUserName(userDTO.getUserName());
        } else if (user.getUserName() == null) {
            throw new RuntimeException("UserName cannot be null");
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPhone() != null && !userDTO.getPhone().isEmpty()) {
            user.setPhoneNumber(userDTO.getPhone());
        }

        userRepository.save(user);
        return convertToDTO(user);
    }


    public UserDTO updateUserStatus(Long userId, UserStatusEnum status) {
        checkAdminPermission();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(status);
        userRepository.save(user);
        return convertToDTO(user);
    }

    public void banUser(Long userId) {
        checkAdminPermission();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatusEnum.BAN);
        userRepository.save(user);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFullName(),
                user.getUserName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getStatus()
        );
    }

}

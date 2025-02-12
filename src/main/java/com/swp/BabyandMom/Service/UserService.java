package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.LoginRequestDTO;
import com.swp.BabyandMom.DTO.LoginResponseDTO;
import com.swp.BabyandMom.DTO.RegisterRequestDTO;
import com.swp.BabyandMom.DTO.RegisterResponseDTO;
import com.swp.BabyandMom.Entity.Enum.RoleType;
import com.swp.BabyandMom.Entity.Enum.UserStatusEnum;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.ExceptionHandler.AuthAppException;
import com.swp.BabyandMom.ExceptionHandler.ErrorCode;
import com.swp.BabyandMom.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private JWTService jwtService;

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

            if (!user.getPassword().equals(loginRequestDTO.getPassword())) {
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
                        .body(new RegisterResponseDTO(null, null, null, null, "Email đã được sử dụng!"));
            }

            // Kiểm tra username đã tồn tại chưa
            if (userRepository.existsByUserName(registerRequestDTO.getUserName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new RegisterResponseDTO(null, null, null, null, "Username đã tồn tại!"));
            }

            User newUser = new User();
            newUser.setFullName(registerRequestDTO.getName());
            newUser.setUserName(registerRequestDTO.getUserName());
            newUser.setEmail(registerRequestDTO.getEmail());
            newUser.setPassword(registerRequestDTO.getPassword());

            newUser.setRole(RoleType.MEMBER);
            newUser.setStatus(UserStatusEnum.VERIFIED);

            User savedUser = userRepository.save(newUser);

            RegisterResponseDTO responseDTO = new RegisterResponseDTO(
                    savedUser.getId(),
                    savedUser.getFullName(),
                    savedUser.getUserName(),
                    savedUser.getEmail(),
                    "Registered successfully"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RegisterResponseDTO(null, null, null, null, "Lỗi server: " + e.getMessage()));
        }
    }


}

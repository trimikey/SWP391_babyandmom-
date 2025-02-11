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
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    public User getAccountByEmail(String email) {
        Optional<User> account = userRepository.findByEmail(email);
        return account.orElse(null);
    }
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userDetails;
    }

    public ResponseEntity<LoginResponseDTO> checkLogin(LoginRequestDTO loginRequestDTO) {
        try {
            User user = getAccountByEmail(loginRequestDTO.getEmail());

            if (user == null) {
                throw new AuthAppException(ErrorCode.EMAIL_NOT_FOUND);
            }
            if (user.getStatus().equals(UserStatusEnum.UNVERIFIED)) {
                throw new AuthAppException(ErrorCode.ACCOUNT_NOT_VERIFY);
            }
            Authentication authentication = null;
            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequestDTO.getEmail(),
                                loginRequestDTO.getPassword()
                        )
                );
            } catch (Exception e) {
                throw new AuthAppException(ErrorCode.EMAIL_PASSWORD_NOT_CORRECT);
            }

            System.out.println(authentication);

            if (authentication != null && authentication.isAuthenticated()) {
                User returnAccount = (User) authentication.getPrincipal();
                user.setTokens(jwtService.generateToken(user.getEmail()));
                user.setRefreshToken(jwtService.generateRefreshToken(user.getEmail()));

                String responseString = "Login successful";
                LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
                        responseString,
                        null,
                        returnAccount.getTokens(),
                        returnAccount.getRefreshToken()
                );
                return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
            } else {
                throw new UsernameNotFoundException("Invalid user request");
            }

        } catch (AuthAppException e) {
            ErrorCode errorCode = e.getErrorCode();
            String errorResponse = "Login failed";
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
                    e.getMessage(),
                    errorResponse,
                    null,
                    null
            );
            return new ResponseEntity<>(loginResponseDTO, errorCode.getHttpStatus());
        }
    }

    private User getUserFromToken(String token) {
        String email = extractEmailFromToken(token);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthAppException(ErrorCode.TOKEN_INVALID));
    }

    private String extractEmailFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String email = jwtService.extractEmail(token);

        if (email == null || email.isEmpty()) {
            throw new AuthAppException(ErrorCode.TOKEN_INVALID);
        }

        return email;
    }

    //Sau nay phai doi lai 1 chut
    private List<Integer> parseListToInterger(String wishlist) {
        return Arrays.stream(wishlist.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    //Dat lai ten
    private String getString(String token) {
        String email = jwtService.extractEmail(token);
        if (email == null || email.isEmpty()) {
            throw new AuthAppException(ErrorCode.TOKEN_INVALID);
        }
        return email;
    }


    // logic for register
    public ResponseEntity<RegisterResponseDTO> checkRegister(RegisterRequestDTO registerRequestDTO) {
        // Kiểm tra xem email đã tồn tại chưa
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RegisterResponseDTO(null, null, null, null, "Email đã được sử dụng!"));
        }

        // Kiểm tra xem username đã tồn tại chưa
        if (userRepository.existsByUserName(registerRequestDTO.getUserName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RegisterResponseDTO(null, null, null, null, "Username đã tồn tại!"));
        }

        // Tạo đối tượng User mới
        User newUser = new User();
        newUser.setFullName(registerRequestDTO.getName());
        newUser.setUserName(registerRequestDTO.getUserName());
        newUser.setEmail(registerRequestDTO.getEmail());
        newUser.setPassword(registerRequestDTO.getPassword());
        newUser.setRole(RoleType.MEMBER);
        newUser.setStatus(UserStatusEnum.VERIFIED);

        // Lưu user vào database
        User savedUser = userRepository.save(newUser);

        // Tạo response DTO
        RegisterResponseDTO responseDTO = new RegisterResponseDTO(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getUserName(),
                savedUser.getEmail(),
                "Registered successfully"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


}
